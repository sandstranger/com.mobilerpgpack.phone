package com.mobilerpgpack.phone.translator.models;

import com.zxw.bingtranslateapi.BingTranslator;
import com.zxw.bingtranslateapi.entity.TranslationParams;
import org.koin.java.KoinJavaComponent;
import lombok.Synchronized;

public class BingTranslatorEndPoint  {
    private BingTranslator translator;

    public String translate(String text, String sourceLocale,String targetLocale){
        createTranslator();
        TranslationParams params = TranslationParams.builder()
                .text(text)
                .fromLang(sourceLocale)
                .toLang(targetLocale)
                .build();

        return translator.translate(params).getTranslation();
    }
    
    @Synchronized
    private void createTranslator (){
        if (translator == null){
            translator = KoinJavaComponent.get(BingTranslator.class);
        }
    }
}
