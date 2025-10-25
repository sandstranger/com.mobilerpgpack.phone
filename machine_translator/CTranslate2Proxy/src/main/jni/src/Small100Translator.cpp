#include <jni.h>
#include <string>
#include <vector>
#include <ctranslate2/translator.h>
#include "sentencepiece_processor.h"
#include "android/log.h"
using namespace std;
using namespace sentencepiece;
using namespace ctranslate2;

extern TranslationOptions create_translation_options();
extern unique_ptr<Translator> create_translator (string model_path, bool multi_thread = true );
extern vector<vector<string>> tokenize_sentences(SentencePieceProcessor *tokenizer,
                                                 const vector<string> *sentences);
static unique_ptr<SentencePieceProcessor> sp = nullptr;
static std::unique_ptr<ctranslate2::Translator> translator = nullptr;

string translate(string input,vector<string > *sentences, const string *target_locale) {
    if (input.empty()){
        return "";
    }

    if (!translator || !sp) {
        return input;
    }

    try {
        string target_prefix = "__" + *target_locale + "__";
        auto tokenized_sentences = tokenize_sentences (sp.get(), sentences);

        std::vector<std::vector<std::string>> batch;
        batch.reserve(tokenized_sentences.size());
        for (auto& sentence : tokenized_sentences) {
            sentence.insert(sentence.begin(), target_prefix);
            sentence.insert(sentence.end(),"</s>");
            batch.push_back(std::move(sentence));
        }

        auto results = translator->translate_batch(batch,
                                                   create_translation_options());

        std::string full_output;
        for (const auto& result : results) {
            string decode;
            sp->Decode(result.output(), &decode);
            full_output += decode + " ";
        }

        if (!full_output.empty()) {
            full_output.pop_back();
        }

        return full_output;
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

 void Small100Translator_initializeFromJni  (const char* pathToTranslationModel,
                                             const char* pathToSourceProcessor) {
    if (translator!= nullptr) {
        return;
    }
    sp = make_unique<SentencePieceProcessor>();
    sp->Load(pathToSourceProcessor);
    translator = create_translator( pathToTranslationModel, true);
}

const char* Small100Translator_translateFromJni (const char* text, char** sentences,const char* targetLocale) {
    if (translator == nullptr){
        return  text;
    }

    string textString = text;
    string targetLocaleString = targetLocale;
    auto native_sentences = toVectorString(sentences);

    return translate(textString,&native_sentences, &targetLocaleString).c_str();
}

void Small100Translator_releaseFromJni() {
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