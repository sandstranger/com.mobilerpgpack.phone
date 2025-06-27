#include "Translator.h"
#include <jni.h>
#include <string>
#include <unordered_map>

#ifdef __cplusplus
extern "C" {
#endif
static JavaVM *g_JavaVM = nullptr;
static jclass g_TranslationManagerClass = nullptr;
static jmethodID g_IsTranslatedMethodID = nullptr;
static jmethodID g_GetTranslationMethodID = nullptr;
static jmethodID g_TranslateMethodID = nullptr;

static std::unordered_map<std::string, std::string> translationCache;

jint JNI_OnLoad(JavaVM *vm, void *) {
    g_JavaVM = vm;
    JNIEnv *env = nullptr;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }

    jclass localClass = env->FindClass("com/mobilerpgpack/phone/translator/TranslationManager");
    if (!localClass) return JNI_ERR;

    g_TranslationManagerClass = (jclass) env->NewGlobalRef(localClass);
    env->DeleteLocalRef(localClass);

    g_IsTranslatedMethodID = env->GetStaticMethodID(
            g_TranslationManagerClass,
            "isTranslated",
            "([B)Z"
    );
    g_GetTranslationMethodID = env->GetStaticMethodID(
            g_TranslationManagerClass,
            "getTranslation",
            "([B)Ljava/lang/String;"
    );
    g_TranslateMethodID = env->GetStaticMethodID(
            g_TranslationManagerClass,
            "translate",
            "([BZ)Ljava/lang/String;"
    );

    if (!g_IsTranslatedMethodID || !g_GetTranslationMethodID || !g_TranslateMethodID)
        return JNI_ERR;

    return JNI_VERSION_1_6;
}

const char *translate(const char *input, bool textFromDialog) {
    if (!g_JavaVM || !g_TranslationManagerClass || !g_IsTranslatedMethodID ||
        !g_GetTranslationMethodID || !g_TranslateMethodID) {
        return input; // fallback
    }

    JNIEnv *env = nullptr;
    bool didAttach = false;

    if (g_JavaVM->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        if (g_JavaVM->AttachCurrentThread(&env, nullptr) != JNI_OK) {
            return input;
        }
        didAttach = true;
    }

    if (translationCache.contains(input)) {
        if (didAttach) g_JavaVM->DetachCurrentThread();
        return translationCache[input].c_str();
    }

    jsize len = strlen(input);
    jbyteArray jInput = env->NewByteArray(len);
    env->SetByteArrayRegion(jInput, 0, len, reinterpret_cast<const jbyte*>(input));

    jboolean isTrans = env->CallStaticBooleanMethod(
            g_TranslationManagerClass,
            g_IsTranslatedMethodID,
            jInput
    );

    if (!isTrans) {
        jboolean isFromDialog = textFromDialog;
        env->CallStaticObjectMethod(
                g_TranslationManagerClass,
                g_TranslateMethodID,
                jInput, isFromDialog
        );

        env->DeleteLocalRef(jInput);
        if (didAttach) g_JavaVM->DetachCurrentThread();
        return input;
    }

    auto jOutput = (jstring) env->CallStaticObjectMethod(
            g_TranslationManagerClass,
            g_GetTranslationMethodID,
            jInput
    );
    env->DeleteLocalRef(jInput);

    if (jOutput == nullptr) {
        if (didAttach) g_JavaVM->DetachCurrentThread();
        return input;
    }

    const char *utfChars = env->GetStringUTFChars(jOutput, nullptr);
    if (!utfChars) {
        env->DeleteLocalRef(jOutput);
        if (didAttach) g_JavaVM->DetachCurrentThread();
        return input;
    }

    std::string translationCopy(utfChars);
    env->ReleaseStringUTFChars(jOutput, utfChars);
    env->DeleteLocalRef(jOutput);

    {
        translationCache[input] = std::move(translationCopy);
        if (didAttach) g_JavaVM->DetachCurrentThread();
        return translationCache[input].c_str();
    }
}
#ifdef __cplusplus
}
#endif