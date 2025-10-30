#include "Translator.h"
#include <string>
#include <unordered_map>
#include <unordered_set>

#ifdef __cplusplus
extern "C" {
#endif

using namespace std;

typedef const bool (*is_translated_delegate)(const char *input, const int inputLength);
typedef void (*translate_delegate)(const char *input, const int inputLength, const bool textFromDialog);
typedef const char *(*get_translation_delegate)(const char *input, const int inputLength);

static is_translated_delegate is_translated_instance = nullptr;
static translate_delegate translate_instance = nullptr;
static get_translation_delegate get_translation_instance = nullptr;

static unordered_map<string, string> translationCache;
static unordered_set<string> translatedEntries;

void registerTranslateDelegate(translate_delegate instance) {
    translate_instance = instance;
}

void registerIsTranslatedDelegate(is_translated_delegate instance) {
    is_translated_instance = instance;
}

void registerGetTranslationDelegate(get_translation_delegate instance) {
    get_translation_instance = instance;
}

const char *translate(const char *input, bool textFromDialog) {

    if (is_translated_instance == nullptr || translate_instance == nullptr ||
        get_translation_instance == nullptr) {
        return input;
    }

    if (translationCache.contains(input)) {
        return translationCache[input].c_str();
    }

    const int inputLength = strlen(input);
    const auto isTranslated = is_translated_instance(input, inputLength);

    if (!isTranslated && translatedEntries.contains(input)) {
        return input;
    }

    if (!isTranslated) {
        translate_instance(input, inputLength, textFromDialog);
        translatedEntries.insert(input);
        return input;
    }

    translatedEntries.erase(input);

    string translatedText = get_translation_instance(input, inputLength);

    if (translatedText.empty()) {
        return input;
    }

    translationCache[input] = std::move(translatedText);
    return translationCache[input].c_str();
}
#ifdef __cplusplus
}
#endif