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
extern shared_ptr<Translator> create_translator (string model_path);

static shared_ptr<SentencePieceProcessor> sp = nullptr;
static std::shared_ptr<ctranslate2::Translator> translator = nullptr;

static mutex translator_mutex;

string translate(string input, string target_locale) {
    if (input.empty()){
        return "";
    }

    std::lock_guard<std::mutex> lock(translator_mutex);

    if (!translator || !sp) {
        return input;
    }

    try {
        std::vector<std::string> tokens;
        sp->Encode(input, &tokens);

        string target_prefix = "__" + target_locale + "__";
        tokens.insert(tokens.begin(), target_prefix);
        tokens.insert(tokens.end(), "</s>");

        const std::vector<std::vector<std::string>> batch = {tokens};

        auto results =
                translator->translate_batch_async(batch, create_translation_options())[0].get();

        const std::vector<std::string> translatedTokens = results.output();

        std::string result;
        sp->Decode(translatedTokens, &result);
        return result;
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
Java_com_mobilerpgpack_ctranslate2proxy_Small100Translator_initializeFromJni
        (JNIEnv *env, jobject thisObject, jstring pathToTranslationModel,
         jstring pathToSourceProcessor) {
    if (translator!= nullptr) {
        return;
    }
    sp = make_unique<SentencePieceProcessor>();
    sp->Load(jstringToStdString(env, pathToSourceProcessor));
    translator = create_translator(jstringToStdString(env, pathToTranslationModel));
}

JNIEXPORT jstring JNICALL Java_com_mobilerpgpack_ctranslate2proxy_Small100Translator_translateFromJni
        (JNIEnv *env, jobject thisObject, jstring text,
         jstring targetLocale) {
    if (translator == nullptr){
        return  text;
    }

    string textString = jstringToStdString(env,text);
    string targetLocaleString = jstringToStdString(env,targetLocale);

    return toJString(env,translate(textString, targetLocaleString));
}

JNIEXPORT void JNICALL Java_com_mobilerpgpack_ctranslate2proxy_Small100Translator_releaseFromJni
        (JNIEnv *env, jobject thisObject) {
    std::lock_guard<std::mutex> lock(translator_mutex);
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