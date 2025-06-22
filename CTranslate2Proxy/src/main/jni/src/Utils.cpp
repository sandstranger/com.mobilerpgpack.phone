#include "jni.h"
#include <string>
#include <thread>
#include "ctranslate2/translator.h"

using namespace ctranslate2;
using namespace std;

TranslationOptions create_translation_options(){
    ctranslate2::TranslationOptions options;
    options.beam_size = 5;
    return options;
}

unique_ptr<Translator> create_translator (string model_path){
    return make_unique<Translator>(
            model_path,
            ctranslate2::Device::CPU,ctranslate2::ComputeType::INT8_FLOAT32);
}

extern "C" {
jstring toJString(JNIEnv *env, const std::string &str) {
    return env->NewStringUTF(str.c_str());
}

std::string jstringToStdString(JNIEnv *env, jstring jStr) {
    const char *chars = env->GetStringUTFChars(jStr, nullptr);
    std::string result(chars);
    env->ReleaseStringUTFChars(jStr, chars);
    return result;
}
}
