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
extern unique_ptr<Translator> create_translator (string model_path, bool multi_thread = false );
extern vector<vector<string>> tokenize_sentences(SentencePieceProcessor *tokenizer,
                                                 const vector<string> *sentences);
extern string decode(SentencePieceProcessor *tokenizer, vector<TranslationResult> results);

static unique_ptr<SentencePieceProcessor> sp = nullptr;
static std::unique_ptr<ctranslate2::Translator> translator = nullptr;

string translate(string input,vector<string > *sentences, string target_locale) {
    if (input.empty()){
        return "";
    }

    if (!translator || !sp) {
        return input;
    }

    try {
        string target_prefix = "__" + target_locale + "__";
        auto tokenized_sentences = tokenize_sentences (sp.get(), sentences);

        auto translation_options = create_translation_options();
        string full_output;
        for (auto &sentence: tokenized_sentences){
            sentence.insert(sentence.begin(), target_prefix);
            sentence.insert(sentence.end(), "</s>");

            const vector<vector<string>> batch = {sentence};
            auto results = translator->translate_batch(batch, translation_options);
            auto decode_output = decode(sp.get(), results);
            full_output += decode_output + " ";
        }

        full_output.pop_back();
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
extern vector<string> toVectorString (JNIEnv* env, jobject sentences);

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
        (JNIEnv *env, jobject thisObject, jstring text, jobject sentences,
         jstring targetLocale) {
    if (translator == nullptr){
        return  text;
    }

    string textString = jstringToStdString(env,text);
    string targetLocaleString = jstringToStdString(env,targetLocale);
    auto native_sentences = toVectorString(env, sentences);

    return toJString(env,translate(textString,&native_sentences, targetLocaleString));
}

JNIEXPORT void JNICALL Java_com_mobilerpgpack_ctranslate2proxy_Small100Translator_releaseFromJni
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