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

static JMethod<jboolean(alias_ref<JArrayByte>)> g_IsTranslatedMethodID;
static JMethod<JString (alias_ref<JArrayByte>)> g_GetTranslationMethodID;
static JMethod<JString (alias_ref<JArrayByte>,jboolean)> g_TranslateMethodID;
static global_ref<jobject> g_Instance;

static std::unordered_map<std::string, std::string> translationCache;

JNIEXPORT void JNICALL
Java_com_mobilerpgpack_phone_translator_TranslationManager_registerTranslationManagerInstance (JNIEnv* env, jobject obj) {
    g_Instance = make_global(obj);
    const auto g_TranslationManagerClass = g_Instance->getClass();
    g_IsTranslatedMethodID = g_TranslationManagerClass->getMethod<jboolean(alias_ref<JArrayByte>)>("isTranslated");
    g_GetTranslationMethodID = g_TranslationManagerClass->getMethod<JString (alias_ref<JArrayByte>)>("getTranslation");
    g_TranslateMethodID = g_TranslationManagerClass->getMethod<JString (alias_ref<JArrayByte>, jboolean)>("translate");
}

const char *translate(const char *input, bool textFromDialog) {

    if (translationCache.contains(input)) {
        return translationCache[input].c_str();
    }

    const auto len = static_cast<jsize>(strlen(input));
    const auto jInput = JArrayByte::newArray(len);
    const auto classInstance = g_Instance.get();

    Environment::current()->SetByteArrayRegion(jInput.get(),0,len,reinterpret_cast<const jbyte*>(input));
    const auto isTrans = g_IsTranslatedMethodID(classInstance,jInput);

    if (!isTrans) {
        jboolean isFromDialog = textFromDialog;
        g_TranslateMethodID(classInstance,jInput, isFromDialog);
        return input;
    }

    const auto jOutput = g_GetTranslationMethodID(classInstance,jInput);

    if (jOutput == nullptr) {
        return input;
    }

    translationCache[input] = jOutput->toStdString();
    return translationCache[input].c_str();
}
#ifdef __cplusplus
}
#endif