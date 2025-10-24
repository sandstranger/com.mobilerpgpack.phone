#include "Translator.h"
#include <jni.h>
#include <string>
#include <unordered_map>
#include "fbjni/fbjni.h"
#include "fbjni/ByteBuffer.h"

#ifdef __cplusplus
extern "C" {
#endif
using namespace facebook::jni;

static alias_ref<JClass> g_TranslationManagerClass = nullptr;
static JStaticMethod<jboolean(alias_ref<JArrayByte>)> g_IsTranslatedMethodID;
static JStaticMethod<JString (alias_ref<JArrayByte>)> g_GetTranslationMethodID;
static JStaticMethod<JString (alias_ref<JArrayByte>,jboolean)> g_TranslateMethodID;

static std::unordered_map<std::string, std::string> translationCache;

const char *translate(const char *input, bool textFromDialog) {
    if (g_TranslationManagerClass == nullptr){
        g_TranslationManagerClass = findClassStatic("com/mobilerpgpack/phone/translator/TranslationManager");
        g_IsTranslatedMethodID = g_TranslationManagerClass->getStaticMethod<jboolean(alias_ref<JArrayByte>)>("isTranslated");
        g_GetTranslationMethodID = g_TranslationManagerClass->getStaticMethod<JString (alias_ref<JArrayByte>)>("getTranslation");
        g_TranslateMethodID = g_TranslationManagerClass->getStaticMethod<JString (alias_ref<JArrayByte>,jboolean)>("translate");
    }

    const auto len = static_cast<jsize>(strlen(input));
    const auto jInput = JArrayByte::newArray(len);

    Environment::current()->SetByteArrayRegion(jInput.get(),0,len,reinterpret_cast<const jbyte*>(input));
    const auto isTrans = g_IsTranslatedMethodID(g_TranslationManagerClass,jInput);

    if (!isTrans) {
        jboolean isFromDialog = textFromDialog;
        g_TranslateMethodID(g_TranslationManagerClass,jInput, isFromDialog);
        return input;
    }

    const auto jOutput = g_GetTranslationMethodID(g_TranslationManagerClass,jInput);

    if (jOutput == nullptr) {
        return input;
    }

    translationCache[input] = jOutput->toStdString();
    return translationCache[input].c_str();
}
#ifdef __cplusplus
}
#endif