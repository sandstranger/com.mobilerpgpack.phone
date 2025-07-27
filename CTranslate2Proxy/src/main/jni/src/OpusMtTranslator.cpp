#include "jni.h"
#include <android/log.h>
#include <ctranslate2/translator.h>
#include <sentencepiece_processor.h>
#include <iostream>
#include <vector>
#include <string>
#include <stdexcept>

using namespace std;
using namespace sentencepiece;
using namespace ctranslate2;

extern TranslationOptions create_translation_options();
extern unique_ptr<Translator> create_translator (string model_path, bool multi_thread = true );
extern vector<vector<string>> tokenize_sentences(SentencePieceProcessor *tokenizer,
                                                 const vector<string> *sentences);
extern string decode(SentencePieceProcessor *tokenizer, vector<TranslationResult> results);

static std::unique_ptr<ctranslate2::Translator> translator = nullptr;
static std::unique_ptr<SentencePieceProcessor> sp_source = nullptr;
static std::unique_ptr<SentencePieceProcessor> sp_target = nullptr;


static std::string Translate (string input, const vector<string> *sentences){
    if (input.empty()) {
        return "";
    }

    if (!translator || !sp_source || !sp_target) {
        return input;
    }

    try {

        auto tokenized = tokenize_sentences(sp_source.get(), sentences);
        auto results = translator->translate_batch(tokenized,
                                                   create_translation_options());

        return decode(sp_target.get(), results);
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
Java_com_mobilerpgpack_ctranslate2proxy_OpusMtTranslator_initializeFromJni
        (JNIEnv *env, jobject thisObject, jstring pathToTranslationModel,
         jstring pathToSourceProcessor,
         jstring pathToTargetProcessor) {
    if (translator!= nullptr) {
        return;
    }
    sp_source = make_unique<SentencePieceProcessor>();
    sp_target = make_unique<SentencePieceProcessor>();

    sp_source->Load(jstringToStdString(env, pathToSourceProcessor));
    sp_target->Load(jstringToStdString(env, pathToTargetProcessor));

    translator = create_translator(jstringToStdString(env, pathToTranslationModel), true);
}

JNIEXPORT jstring JNICALL Java_com_mobilerpgpack_ctranslate2proxy_OpusMtTranslator_translateFromJni
        (JNIEnv *env, jobject thisObject,jstring text, jobject sentences) {
    if (translator == nullptr){
        return text;
    }
    vector<string> nativeSentences = toVectorString(env,sentences);
    return toJString(env,Translate(jstringToStdString(env, text), &nativeSentences));
}

JNIEXPORT void JNICALL Java_com_mobilerpgpack_ctranslate2proxy_OpusMtTranslator_releaseFromJni
        (JNIEnv *env, jobject thisObject) {
    if (translator == nullptr){
        return;
    }
    sp_target.reset();
    sp_source.reset();
    translator->detach_models();
    translator.reset();

    sp_target = nullptr;
    sp_source = nullptr;
    translator = nullptr;
}
}
