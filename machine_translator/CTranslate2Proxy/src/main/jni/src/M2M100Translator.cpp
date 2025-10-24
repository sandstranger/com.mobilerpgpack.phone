#include <jni.h>
#include <string>
#include <vector>
#include <ctranslate2/translator.h>
#include "sentencepiece_processor.h"

using namespace std;
using namespace sentencepiece;
using namespace ctranslate2;

extern TranslationOptions create_translation_options();
extern unique_ptr<Translator> create_translator (string model_path, bool multi_thread = true );
extern vector<vector<string>> tokenize_sentences(SentencePieceProcessor *tokenizer,
                                                 const vector<string> *sentences);
static unique_ptr<SentencePieceProcessor> sp = nullptr;
static unique_ptr<ctranslate2::Translator> translator = nullptr;

string translate(string input, vector<string > *sentences, string *source_locale, string *target_locale) {
    if (input.empty()){
        return "";
    }

    if (!translator || !sp) {
        return input;
    }

    try {

        string source_prefix = "__" + *source_locale + "__";
        const vector<string> target_prefix = {"__" + *target_locale + "__"};

        vector<vector<string>> target_prefixes;
        target_prefixes.reserve(sentences->size());
        for (size_t i = 0; i < sentences->size(); ++i) {
            target_prefixes.push_back(target_prefix);
        }

        auto tokenized_sentences = tokenize_sentences(sp.get(), sentences);

        for (auto& sentence : tokenized_sentences) {
            sentence.insert(sentence.begin(), source_prefix);
        }

        auto translation_options = create_translation_options();
        auto results = translator->translate_batch(tokenized_sentences,
                                                   target_prefixes, translation_options);

        string full_output;
        for (const auto& result : results) {
            string decoded;
            sp->Decode(result.output(),&decoded);
            full_output += decoded.substr(6);
        }
        return full_output.substr(1);
    }
    catch (const std::exception& e) {
        return input;
    }
    catch (...) {
        return input;
    }
}


extern "C" {
extern vector<string> toVectorString (char** sentences);

 void M2M100Translator_initializeFromJni (const char*  pathToTranslationModel,const char*  pathToSourceProcessor) {
    if (translator!= nullptr) {
        return;
    }
    sp = make_unique<SentencePieceProcessor>();
    sp->Load(pathToSourceProcessor);
    translator = create_translator( pathToTranslationModel, true);
}

const char* M2M100Translator_translateFromJni(const char* text, char** sentences,
                                              const char* sourceLocale,const char* targetLocale) {
    if (translator == nullptr){
        return  text;
    }

    string textString = text;
    string sourceLocaleString =  sourceLocale;
    string targetLocaleString = targetLocale;
    auto native_sentences = toVectorString( sentences);

    return translate(textString,&native_sentences, &sourceLocaleString,
                                   &targetLocaleString).c_str();
}

void M2M100Translator_releaseFromJni () {
    if (translator == nullptr){
        return;
    }
    sp.reset();
    translator->detach_models();
    translator.reset();
    sp = nullptr;
    translator = nullptr;
}
}