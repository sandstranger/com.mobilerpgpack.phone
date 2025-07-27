package com.zxw.bingtranslateapi;

import com.google.gson.*;
import com.zxw.bingtranslateapi.entity.RawTranslationResponse;
import com.zxw.bingtranslateapi.entity.TranslateConfig;
import com.zxw.bingtranslateapi.entity.TranslationParams;
import com.zxw.bingtranslateapi.entity.TranslationResult;
import com.zxw.bingtranslateapi.exception.TranslationException;
import com.zxw.bingtranslateapi.exception.TranslationConfigLoadException;
import com.zxw.bingtranslateapi.exception.TranslationOverLimitException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * bing 翻译器 <br>
 */
@Slf4j
public class BingTranslator {

    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36";

    /**
     * okHttpClient instance
     */
    private final OkHttpClient okHttpClient;
    /**
     * 翻译配置管理器
     */
    private final TranslationConfigManager translationConfigManager;

    public BingTranslator(OkHttpClient okHttpClient) {
        this(okHttpClient, false);
    }

    public BingTranslator(OkHttpClient okHttpClient, boolean renewable) {
        this.okHttpClient = okHttpClient;
        this.translationConfigManager = new TranslationConfigManager(okHttpClient, renewable);
    }

    /**
     * 将文本翻译成指定类型语言
     *
     * @param params 翻译相关参数
     * @return
     * @throws TranslationException 当翻译时出现任何错误时，抛出该异常。
     *                              当响应为 401 时抛出该异常，代表请求过快，应适当放慢请求频率
     *                              当响应为 {"ShowCaptcha": true} 时抛出该异常，代表请求过快需要验证码验证，应适当放慢请求频率
     * @throws TranslationConfigLoadException 当获取翻译配置时出现错误，抛出该异常
     * @throws IllegalArgumentException 当待翻译文本为空，或者来源、目标语言类型不支持时抛出该异常
     */
    public TranslationResult translate(TranslationParams params) throws TranslationException, TranslationConfigLoadException {
        String text = params.getText();

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text must not blank.");
        }

        if (!Languages.isSupport(params.getFromLang()) || !Languages.isSupport(params.getToLang())) {
            throw new IllegalArgumentException("Unsupported lang, fromLang: " + params.getFromLang() + ", toLang: " + params.getToLang());
        }

        // TODO 优化 json 处理代码
        String responseBody = doTranslateRequest(params);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(responseBody);

        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonPrimitive field = jsonObject.getAsJsonPrimitive("ShowCaptcha");

            if (field != null && field.getAsBoolean()) {
                throw new TranslationOverLimitException("Sorry that bing translator seems to be asking for the captcha, " +
                        "please take care not to request too frequently.");
            }
        }

        if (!jsonElement.isJsonArray()) {
            throw new TranslationException("Translation result schema illegal : " + responseBody);
        }

        RawTranslationResponse[] rawTranslationResponses = new Gson().fromJson(jsonElement, RawTranslationResponse[].class);
        TranslationResult result = new TranslationResult();
        result.setRawResponse(responseBody);
        result.setText(params.getText());

        if (rawTranslationResponses == null || rawTranslationResponses.length == 0) {
            return result;
        }

        RawTranslationResponse translationResponse = rawTranslationResponses[0];
        List<RawTranslationResponse.Translation> translations = translationResponse.getTranslations();
        RawTranslationResponse.DetectedLanguage detectedLanguage = translationResponse.getDetectedLanguage();

        result.setTranslation(translations.get(0).getText());
        TranslationResult.LanguageInfo languageInfo = TranslationResult.LanguageInfo
                .builder()
                .from(detectedLanguage.getLanguage())
                .to(translations.get(0).getTo())
                .score(detectedLanguage.getScore())
                .build();
        result.setLanguageInfo(languageInfo);

        return result;
    }

    private String doTranslateRequest(TranslationParams params) throws TranslationException, TranslationConfigLoadException {
        TranslateConfig translateConfig = translationConfigManager.getTranslateConfig();
        String requestUrl = createRequestUrl(translateConfig);
        RequestBody requestBody = createRequestBody(translateConfig, params);
        String userAgent = params.getUserAgent() == null || params.getUserAgent().isBlank() ?
                DEFAULT_USER_AGENT :
                params.getToLang();

        Request request = new Request.Builder()
                .url(requestUrl)
                .method("POST", requestBody)
                .addHeader("user-agent", userAgent)
                .addHeader("origin", translationConfigManager.getTranslateDomain())
                .addHeader("referer", translationConfigManager.getTranslatePageUrl())
                .addHeader("cookie", translateConfig.getCookie())
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.code() == 401) {
                throw new TranslationOverLimitException("Translation limit exceeded. Please try it again later.");
            }

            return response.body().string();
        } catch (IOException e) {
            throw new TranslationException("Translate occur a error.", e);
        }
    }

    private String createRequestUrl(TranslateConfig translateConfig) {
        return String.format("%s&&IG=%s&IID=%s", translationConfigManager.getTranslateApiUrl(), translateConfig.getIG(), translateConfig.getIID());
    }

    private RequestBody createRequestBody(TranslateConfig translateConfig, TranslationParams params) {
        Map<String, String> paramMap = new HashMap<>();

        paramMap.put("fromLang", params.getFromLang());
        paramMap.put("text", params.getText().trim());
        paramMap.put("token", translateConfig.getToken());
        paramMap.put("key", translateConfig.getKey().toString());
        paramMap.put("to", params.getToLang());
        paramMap.put("tryFetchingGenderDebiasedTranslations", "true");

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String paramString = paramMap.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));

        return RequestBody.create(paramString, mediaType);
    }

    public void close() {
        translationConfigManager.close();
    }
}
