#include "jni.h"
#include <string>
#include <thread>
#include "ctranslate2/translator.h"
#include "sentencepiece_processor.h"

using namespace sentencepiece;
using namespace ctranslate2;
using namespace std;

vector<vector<string>> tokenize_sentences(SentencePieceProcessor *tokenizer,
                                          const vector<string> *sentences) {
    std::vector<std::vector<std::string>> tokenized;
    for (const auto &s: *sentences) {
        std::vector<std::string> tokens;
        tokenizer->Encode(s, &tokens);
        tokenized.push_back(tokens);
    }
    return tokenized;
}

string decode(SentencePieceProcessor *tokenizer, vector<TranslationResult> results) {
    std::string full_output;
    for (const auto& result : results) {
        std::string output;
        tokenizer->Decode(result.output(), &output);
        full_output += output + " ";
    }

    full_output.pop_back();
    return full_output;
}

TranslationOptions create_translation_options(){
    ctranslate2::TranslationOptions options;
    options.beam_size = 5;
    options.length_penalty = 0.6f;
    options.sampling_temperature = 0.0f;
    options.repetition_penalty = 1.2f;
    return options;
}

unique_ptr<Translator> create_translator (string model_path, bool multi_thread = true ) {
    if (multi_thread) {
        unsigned int num_threads = std::thread::hardware_concurrency();

        if (num_threads == 0){
            num_threads = 1;
        }
        else if (num_threads >=3){
            num_threads = 3;
        }

        ReplicaPoolConfig config;
        config.num_threads_per_replica = num_threads;

        vector<int> vector = {0};

        return make_unique<Translator>(
                model_path,
                ctranslate2::Device::CPU, ctranslate2::ComputeType::INT8_FLOAT32, vector, false,
                config);
    }
    return make_unique<Translator>(
            model_path,
            ctranslate2::Device::CPU, ctranslate2::ComputeType::INT8_FLOAT32);
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

vector<std::string> toVectorString (JNIEnv* env, jobject sentences){
    std::vector<std::string> nativeSentences;

    jclass listClass = env->GetObjectClass(sentences);
    jmethodID sizeMethod = env->GetMethodID(listClass, "size", "()I");
    jmethodID getMethod  = env->GetMethodID(listClass, "get", "(I)Ljava/lang/Object;");

    jint size = env->CallIntMethod(sentences, sizeMethod);

    for (jint i = 0; i < size; ++i) {
        jstring word = (jstring) env->CallObjectMethod(sentences, getMethod, i);
        const char* utfChars = env->GetStringUTFChars(word, nullptr);
        nativeSentences.emplace_back(utfChars);
        env->ReleaseStringUTFChars(word, utfChars);
        env->DeleteLocalRef(word);
    }
    return nativeSentences;
}
}
