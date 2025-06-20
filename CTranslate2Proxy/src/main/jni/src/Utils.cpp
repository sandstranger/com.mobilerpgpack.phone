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

TranslationOptions options = create_translation_options();

unique_ptr<Translator> create_translator (string model_path){
    unsigned int num_threads = std::thread::hardware_concurrency();
    if (num_threads == 0) {
        num_threads = 1;
    } else if (num_threads > 1){
        num_threads = std::lround(num_threads/1.7f);
    }
    return make_unique<Translator>(
            model_path,
            ctranslate2::Device::CPU,ctranslate2::ComputeType::INT8_FLOAT32,
            std::vector<int>(num_threads, 0));
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
