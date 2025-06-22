#include "jni.h"
#include <android/log.h>
#include <ctranslate2/translator.h>
#include <sentencepiece_processor.h>
#include <iostream>
#include <vector>
#include <string>
#include <stdexcept>
#include <mutex>

using namespace std;
using namespace sentencepiece;
using namespace ctranslate2;

extern TranslationOptions create_translation_options();
extern shared_ptr<Translator> create_translator (string model_path);

static std::shared_ptr<ctranslate2::Translator> translator = nullptr;
static std::shared_ptr<SentencePieceProcessor> sp_source = nullptr;
static std::shared_ptr<SentencePieceProcessor> sp_target = nullptr;

static mutex translator_mutex;

static std::string Translate (std::string input){
    if (input.empty()) {
        return "";
    }

    std::lock_guard<std::mutex> lock(translator_mutex);

    if (!translator || !sp_source || !sp_target) {
        return input;
    }

    try {
        std::vector<std::string> source_tokens;
        sp_source->Encode(input, &source_tokens);

        auto results = translator->translate_batch_async({source_tokens},
                                                         create_translation_options())[0].get();
        std::string output;
        sp_target->Decode(results.output(), &output);
        __android_log_print(ANDROID_LOG_INFO, "CTranslate2", "TRANSLATED VALUE = %s", output.c_str());
        return output;
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

JNIEXPORT void JNICALL
Java_com_mobilerpgpack_ctranslate2proxy_OpusMtTranslator_initializeFromJni
        (JNIEnv *env, jobject thisObject, jstring pathToTranslationModel,
         jstring pathToSourceProcessor,
         jstring pathToTargetProcessor) {
    if (translator!= nullptr) {
        return;
    }
    sp_source = make_shared<SentencePieceProcessor>();
    sp_target = make_shared<SentencePieceProcessor>();

    sp_source->Load(jstringToStdString(env, pathToSourceProcessor));
    sp_target->Load(jstringToStdString(env, pathToTargetProcessor));

    translator = create_translator(jstringToStdString(env, pathToTranslationModel));
}

JNIEXPORT jstring JNICALL Java_com_mobilerpgpack_ctranslate2proxy_OpusMtTranslator_translateFromJni
        (JNIEnv *env, jobject thisObject, jstring text) {
    if (translator == nullptr){
        return  text;
    }
    return toJString(env,Translate(jstringToStdString(env, text)));
}

JNIEXPORT void JNICALL Java_com_mobilerpgpack_ctranslate2proxy_OpusMtTranslator_releaseFromJni
        (JNIEnv *env, jobject thisObject) {
    std::lock_guard<std::mutex> lock(translator_mutex);
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
