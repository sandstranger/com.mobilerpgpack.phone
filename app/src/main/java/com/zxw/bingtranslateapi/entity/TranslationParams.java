package com.zxw.bingtranslateapi.entity;

import com.zxw.bingtranslateapi.Languages;
import lombok.Builder;
import lombok.Data;

/**
 * 翻译参数
 */
@Data
@Builder
public class TranslationParams {
    private String text;
    private String fromLang = Languages.DEFAULT_FROM_LANG;
    private String toLang = Languages.DEFAULT_TO_LANG;
    private String userAgent;
}
