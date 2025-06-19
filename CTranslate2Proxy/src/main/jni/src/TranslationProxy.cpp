#include "jni.h"
#include <android/log.h>
#include <ctranslate2/translator.h>
#include <sentencepiece_processor.h>
#include <iostream>
#include <vector>
#include <string>
#include <thread>
#include <cmath>

static sentencepiece::SentencePieceProcessor sp_source;
static sentencepiece::SentencePieceProcessor sp_target;
static std::unique_ptr<ctranslate2::Translator> translator = nullptr;
static bool wasInit = false;

static std::string Translate (std::string input){
    if (input.empty()) return "";
    std::vector<std::string> source_tokens;
    sp_source.Encode(input, &source_tokens);

    auto results = translator->translate_batch_async({source_tokens})[0].get();
    std::string output;
    sp_target.Decode(results.output(), &output);
    __android_log_print(ANDROID_LOG_INFO, "CTranslate2", "TRANSLATED VALUE = %s", output.c_str());
    return output;
}

extern "C" {
jstring toJString(JNIEnv* env, const std::string& str)  {
    return env->NewStringUTF(str.c_str());
}

std::string jstringToStdString(JNIEnv *env, jstring jStr) {
    const char *chars = env->GetStringUTFChars(jStr, nullptr);
    std::string result(chars);
    env->ReleaseStringUTFChars(jStr, chars);
    return result;
}

JNIEXPORT void JNICALL
Java_com_mobilerpgpack_ctranslate2proxy_CTranslate2TranslationProxy_initializeTranslation
        (JNIEnv *env, jobject thisObject, jstring pathToTranslationModel,
         jstring pathToSourceProcessor,
         jstring pathToTargetProcessor) {
    if (wasInit) {
        return;
    }
    unsigned int num_threads = std::thread::hardware_concurrency();
    if (num_threads == 0) {
        num_threads = 1;
    } else{
        num_threads = std::lround(num_threads/1.7f);
    }

    sp_source.Load(jstringToStdString(env, pathToSourceProcessor));
    sp_target.Load(jstringToStdString(env, pathToTargetProcessor));

    translator = std::make_unique<ctranslate2::Translator>(
            jstringToStdString(env, pathToTranslationModel),
            ctranslate2::Device::CPU,ctranslate2::ComputeType::INT8_FLOAT32,
            std::vector<int>(num_threads, 0));

    wasInit = true;

}

JNIEXPORT jstring JNICALL Java_com_mobilerpgpack_ctranslate2proxy_CTranslate2TranslationProxy_translate
        (JNIEnv *env, jobject thisObject, jstring text) {
    if (!wasInit){
        return text;
    }
    return toJString(env,Translate(jstringToStdString(env, text)));
}
}