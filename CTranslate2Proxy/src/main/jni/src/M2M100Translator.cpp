#include <jni.h>
#include <string>
#include <vector>
#include <ctranslate2/translator.h>
#include "sentencepiece_processor.h"

using namespace std;
using namespace sentencepiece;
using namespace ctranslate2;

extern TranslationOptions options;
extern unique_ptr<Translator> create_translator (string model_path);

static unique_ptr<SentencePieceProcessor> sp = nullptr;
static std::unique_ptr<ctranslate2::Translator> translator = nullptr;

string translate(string input, string source_locale, string target_locale) {
    if (input.empty()){
        return "";
    }
    try {
        std::vector<std::string> tokens;
        sp->Encode(input, &tokens);

        string source_prefix = "__" + source_locale + "__";
        tokens.insert(tokens.begin(), source_prefix);

        const std::vector<std::vector<std::string>> batch = {tokens};
        const std::vector<std::vector<std::string>> target_prefix = {
                {"__" + target_locale + "__"}};

        auto results =
                translator->translate_batch_async(batch, target_prefix, options)[0].get();

        const std::vector<std::string> translatedTokens = results.output();

        std::string result;
        sp->Decode(translatedTokens, &result);

        result = result.substr(7);

        return result;
    }
    catch (...) {
        return input;
    }
}


extern "C" {
extern jstring toJString(JNIEnv *env, const std::string &str);
extern std::string jstringToStdString(JNIEnv *env, jstring jStr);

JNIEXPORT void JNICALL
Java_com_mobilerpgpack_ctranslate2proxy_M2M100Translator_initializeFromJni
        (JNIEnv *env, jobject thisObject, jstring pathToTranslationModel,
         jstring pathToSourceProcessor) {
    if (translator!= nullptr) {
        return;
    }
    sp = make_unique<SentencePieceProcessor>();
    sp->Load(jstringToStdString(env, pathToSourceProcessor));
    translator = create_translator(jstringToStdString(env, pathToTranslationModel));
}

JNIEXPORT jstring JNICALL Java_com_mobilerpgpack_ctranslate2proxy_M2M100Translator_translateFromJni
        (JNIEnv *env, jobject thisObject, jstring text, jstring sourceLocale,
         jstring targetLocale) {
    if (translator == nullptr){
        return  text;
    }

    string textString = jstringToStdString(env,text);
    string sourceLocaleString = jstringToStdString(env, sourceLocale);
    string targetLocaleString = jstringToStdString(env,targetLocale);

    return toJString(env,translate(textString, sourceLocaleString,
                                   targetLocaleString));
}

JNIEXPORT void JNICALL Java_com_mobilerpgpack_ctranslate2proxy_M2M100Translator_releaseFromJni
        (JNIEnv *env, jobject thisObject) {
    if (translator == nullptr){
        return;
    }
    sp.reset();
    translator.reset();
    sp = nullptr;
    translator = nullptr;
}
}