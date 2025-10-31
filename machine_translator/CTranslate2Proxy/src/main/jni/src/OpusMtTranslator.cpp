#include "jni.h"
#include <android/log.h>
#include <ctranslate2/translator.h>
#include <sentencepiece_processor.h>
#include <iostream>
#include <vector>
#include <string>
#include <stdexcept>

using namespace std;
using namespace sentencepiece;
using namespace ctranslate2;

extern TranslationOptions create_translation_options();
extern unique_ptr<Translator> create_translator (string model_path, bool multi_thread = true );
extern vector<vector<string>> tokenize_sentences(SentencePieceProcessor *tokenizer,
                                                 const vector<string> *sentences);
extern string decode(SentencePieceProcessor *tokenizer, vector<TranslationResult> results);

static std::unique_ptr<ctranslate2::Translator> translator = nullptr;
static std::unique_ptr<SentencePieceProcessor> sp_source = nullptr;
static std::unique_ptr<SentencePieceProcessor> sp_target = nullptr;


static std::string Translate (string input, const vector<string> *sentences){
    if (input.empty()) {
        return "";
    }

    if (!translator || !sp_source || !sp_target) {
        return input;
    }

    try {

        auto tokenized = tokenize_sentences(sp_source.get(), sentences);
        auto results = translator->translate_batch(tokenized,
                                                   create_translation_options());

        return decode(sp_target.get(), results);
    }
    catch (const std::exception& e) {
        return input;
    }
    catch (...) {
        return input;
    }
}

extern "C" {
extern vector<string> toVectorString (char** sentences);

void OpusMtTranslator_initializeFromJni(const char* pathToTranslationModel,const char* pathToSourceProcessor,
                                        const char* pathToTargetProcessor) {
    if (translator!= nullptr) {
        return;
    }
    sp_source = make_unique<SentencePieceProcessor>();
    sp_target = make_unique<SentencePieceProcessor>();

    sp_source->Load(pathToSourceProcessor);
    sp_target->Load(pathToTargetProcessor);

    translator = create_translator(pathToTranslationModel, true);
}

const char* OpusMtTranslator_translateFromJni (const char* text, char** sentences) {
    if (translator == nullptr){
        return text;
    }
    vector<string> nativeSentences = toVectorString(sentences);
    return Translate( text, &nativeSentences).c_str();
}

void OpusMtTranslator_releaseFromJni() {
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
