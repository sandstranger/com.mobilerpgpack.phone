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

string nllb_200_translate(string input, vector<string > *sentences, string *source_locale, string *target_locale) {
    if (input.empty()){
        return "";
    }

    if (!translator || !sp) {
        return input;
    }

    try {
        const vector<string> target_prefix = {*target_locale};
        auto target_locale_size = target_locale->size();

        vector<vector<string>> target_prefixes;
        target_prefixes.reserve(sentences->size());
        for (size_t i = 0; i < sentences->size(); ++i) {
            target_prefixes.push_back(target_prefix);
        }

        auto tokenized_sentences = tokenize_sentences(sp.get(), sentences);

        for (auto& sentence : tokenized_sentences) {
            sentence.insert(sentence.begin(), *source_locale);
            sentence.insert(sentence.end(),"</s>");
        }

        auto translation_options = create_translation_options();
        auto results = translator->translate_batch(tokenized_sentences,
                                                   target_prefixes, translation_options);

        string full_output;
        for (const auto& result : results) {
            string decoded;
            sp->Decode(result.output(),&decoded);
            full_output += decoded.substr(target_locale_size);
        }
        return full_output.substr( 1);
    }
    catch (const std::exception& e) {
        return input;
    }
    catch (...) {
        return input;
    }
}


extern "C" {
extern jstring toJString(JNIEnv *env, const std::string &str);
extern std::string jstringToStdString(JNIEnv *env, jstring jStr);
extern vector<string> toVectorString (JNIEnv* env, jobject sentences);

JNIEXPORT void JNICALL
Java_com_mobilerpgpack_ctranslate2proxy_NLLB200Translator_initializeFromJni
        (JNIEnv *env, jobject thisObject, jstring pathToTranslationModel,
         jstring pathToSourceProcessor) {
    if (translator!= nullptr) {
        return;
    }
    sp = make_unique<SentencePieceProcessor>();
    sp->Load(jstringToStdString(env, pathToSourceProcessor));
    translator = create_translator(jstringToStdString(env, pathToTranslationModel), true);
}

JNIEXPORT jstring JNICALL Java_com_mobilerpgpack_ctranslate2proxy_NLLB200Translator_translateFromJni
        (JNIEnv *env, jobject thisObject, jstring text, jobject sentences, jstring sourceLocale,
         jstring targetLocale) {
    if (translator == nullptr){
        return  text;
    }

    string textString = jstringToStdString(env,text);
    string sourceLocaleString = jstringToStdString(env, sourceLocale);
    string targetLocaleString = jstringToStdString(env,targetLocale);
    auto native_sentences = toVectorString(env, sentences);

    return toJString(env,nllb_200_translate(textString,&native_sentences, &sourceLocaleString,
                                   &targetLocaleString));
}

JNIEXPORT void JNICALL Java_com_mobilerpgpack_ctranslate2proxy_NLLB200Translator_releaseFromJni
        (JNIEnv *env, jobject thisObject) {
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