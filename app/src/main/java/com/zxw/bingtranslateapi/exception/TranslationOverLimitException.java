package com.zxw.bingtranslateapi.exception;

/**
 * <p>调用翻译频繁异常</p>
 * 当调用 bing 翻译接口过于频繁时（确认频繁的阈值无法确认），响应码会返回 401，
 * 或者响应内容为 {"ShowCaptcha": true}，此时抛出该异常
 */
public class TranslationOverLimitException extends TranslationException {
    public TranslationOverLimitException(String message) {
        super(message);
    }
}
