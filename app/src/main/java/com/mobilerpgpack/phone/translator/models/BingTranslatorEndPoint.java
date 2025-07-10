package com.mobilerpgpack.phone.translator.models;

import com.zxw.bingtranslateapi.BingTranslator;
import com.zxw.bingtranslateapi.entity.TranslationParams;

import lombok.Synchronized;
import okhttp3.OkHttpClient;

public class BingTranslatorEndPoint {
    private final OkHttpClient httpClient = new OkHttpClient().newBuilder().build();
    private BingTranslator translator;

    public String translate(String text, String sourceLocale,String targetLocale){
        TranslationParams params = TranslationParams.builder()
                .text(text)
                .fromLang(sourceLocale)
                .toLang(targetLocale)
                .build();
        createTranslator();
        return translator.translate(params).getTranslation();
    }
    
    @Synchronized
    private void createTranslator (){
        if (translator == null){
             translator = new BingTranslator(httpClient, true);
        }
    }
}
