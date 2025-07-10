package com.zxw.bingtranslateapi.exception;

/**
 * <p>翻译异常</p>
 * 当调用 bing 翻译接口出现 IO 异常或 http 异常时抛出该异常
 */
public class TranslationException extends RuntimeException {

    public TranslationException(String message) {
        super(message);
    }

    public TranslationException(String message, Throwable cause) {
        super(message, cause);
    }
}
