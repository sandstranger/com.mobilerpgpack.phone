package com.zxw.bingtranslateapi.exception;

/**
 * <p>翻译配置加载异常</p>
 * 当加载 https://{subDomain}.bing.com/translator 页面（以获取调用翻译接口所需的配置）
 * 出现 IO 异常或 http 请求异常时，抛出该异常
 */
public class TranslationConfigLoadException extends RuntimeException {

    public TranslationConfigLoadException(String message) {
        super(message);
    }

    public TranslationConfigLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
