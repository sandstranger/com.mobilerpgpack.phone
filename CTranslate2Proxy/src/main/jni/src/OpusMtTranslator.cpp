#include "jni.h"
#include <android/log.h>
#include <ctranslate2/translator.h>
#include <sentencepiece_processor.h>
#include <iostream>
#include <vector>
#include <string>
#include <stdexcept>
#include <regex>

using namespace std;
using namespace sentencepiece;
using namespace ctranslate2;

extern TranslationOptions create_translation_options();
extern unique_ptr<Translator> create_translator (string model_path);

static std::unique_ptr<ctranslate2::Translator> translator = nullptr;
static std::unique_ptr<SentencePieceProcessor> sp_source = nullptr;
static std::unique_ptr<SentencePieceProcessor> sp_target = nullptr;

std::vector<std::string> split_into_sentences(const std::string& input) {
    std::regex re(R"(([^.!?]+[.!?]))");
    std::sregex_iterator it(input.begin(), input.end(), re), end;
    std::vector<std::string> sentences;

    for (; it != end; ++it)
        sentences.push_back(it->str());

    return sentences;
}

static std::string Translate (std::string input){
    if (input.empty()) {
        return "";
    }

    if (!translator || !sp_source || !sp_target) {
        return input;
    }

    try {

        auto sentences = split_into_sentences(input);

        std::vector<std::vector<std::string>> tokenized;
        for (const auto& s : sentences) {
            std::vector<std::string> tokens;
            sp_source->Encode(s, &tokens);
            tokenized.push_back(tokens);
        }

        auto results = translator->translate_batch({tokenized}, create_translation_options());

        std::string full_output;
        for (const auto& result : results) {
            std::string output;
            sp_target->Decode(result.output(), &output);
            full_output += output + " ";
        }

        full_output.pop_back();
        __android_log_print(ANDROID_LOG_INFO, "CTranslate2", "TRANSLATED VALUE = %s", full_output.c_str());
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
    sp_source = make_unique<SentencePieceProcessor>();
    sp_target = make_unique<SentencePieceProcessor>();

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
