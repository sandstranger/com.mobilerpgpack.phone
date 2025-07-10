package com.zxw.bingtranslateapi;

import com.zxw.bingtranslateapi.entity.TranslateConfig;
import com.zxw.bingtranslateapi.exception.TranslationConfigLoadException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 翻译配置管理器 <br>
 * 可自动在翻译配置过期前进行续约
 */
@Slf4j
public class TranslationConfigManager {

    /**
     * 重新加载配置的阈值 <br>
     * 当配置过期时间 < reloadThreshold 时，触发重新加载配置逻辑
     */
    private final int reloadThreshold = 1000;
    /**
     * 保证翻译配置线程安全的锁 <br>
     * 获取翻译配置或写入翻译配置时，需要先获取该锁
     */
    private final Lock lock = new ReentrantLock();
    /**
     * 配置加载完毕 condition <br>
     * 当用户线程获取翻译配置时，翻译配置为 null 或者已过期，则在该 condition 上等待。
     * 直到定时线程获取翻译配置成功后唤醒等待的线程
     */
    private final Condition loadConfigCondition = lock.newCondition();
    /**
     * okHttpClient instance
     */
    private final OkHttpClient okHttpClient;
    /**
     * 是否自动续约翻译配置
     */
    private final boolean renewable;

    /**
     * 定时线程池<br>
     * 用于定时更新翻译配置
     */
    private ScheduledExecutorService scheduledExecutorService;
    /**
     * 翻译配置 <br>
     * 该对象线程安全由 {@link #lock} 守护
     */
    private volatile TranslateConfig translateConfig;
    /**
     * 最近加载配置时出现的异常 <br>
     * 当该异常不为空时，代表最近一次加载配置时出现了异常，此时调用 {@link #getTranslateConfig}
     * 方法时会抛出 {@link TranslationConfigLoadException} 异常
     */
    private volatile TranslationConfigLoadException latestConfigLoadException = null;
    /**
     * bing translator 域名 <br>
     *
     * <p>不同地区访问 <a href="https://www.bing.com/translator">https://www.bing.com/translator</a> 会被重定向到不同子域名，
     * 比如：在中国访问该网站，会被重定向到 <a href="https://cn.bing.com/translator">https://cn.bing.com/translator</a>。</p>
     *
     * 所以在开始翻译之前，需要先判断当前地区对应的 bing translator 域名，以此作为后续请求的域名，以防止后续请求被重定向。
     */
    @Getter
    private String translateDomain;
    /**
     * bing translator 页面地址 = {@link #translateDomain} + /translator
     */
    @Getter
    private String translatePageUrl;
    /**
     * bing translate api 地址 = {@link #translateDomain} + /ttranslatev3?isVertical=1
     */
    @Getter
    private String translateApiUrl;

    /**
     * TranslationConfigManager construct
     *
     * @param okHttpClient {@link OkHttpClient}
     * @throws TranslationConfigLoadException 当初始化翻译参数时出现错误时，抛出该异常
     */
    public TranslationConfigManager(OkHttpClient okHttpClient) throws TranslationConfigLoadException {
        this(okHttpClient, false);
    }

    /**
     * TranslationConfigManager construct
     *
     * @param okHttpClient {@link OkHttpClient}
     * @param renewable 是否自动续约翻译配置
     * @throws TranslationConfigLoadException 当初始化翻译参数时出现错误时，抛出该异常
     */
    public TranslationConfigManager(OkHttpClient okHttpClient, boolean renewable) throws TranslationConfigLoadException {
        this.okHttpClient = okHttpClient;
        this.renewable = renewable;

        determineTranslateDomain();

        if (renewable) {
            scheduledExecutorService = Executors.newScheduledThreadPool(1);
            scheduledExecutorService.scheduleAtFixedRate(this::loadConfig, 0L, 1000L, TimeUnit.MILLISECONDS);
        }
    }

    private void determineTranslateDomain() throws TranslationConfigLoadException {
        Request request = new Request.Builder()
                .addHeader("user-agent", BingTranslator.DEFAULT_USER_AGENT)
                .url("https://bing.com/translator")
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new TranslationConfigLoadException("Load bing translator page failed.");
            }

            // okhttp 自动处理重定向
            String url = response.request().url().url().toString();

            lock.lock();
            try {
                translateConfig = parseTranslatorPage(response);
                translateDomain = url.substring(0, url.lastIndexOf('/'));
                translatePageUrl = url;
                translateApiUrl = translateDomain + "/ttranslatev3?isVertical=1";
            } finally {
                lock.unlock();
            }
        } catch (IOException e) {
            throw new TranslationConfigLoadException("Load translation config occur a error.", e);
        }
    }

    private void loadConfig() throws TranslationConfigLoadException {
        if (translateConfig != null && !isExpirationSoon(translateConfig)) {
            return;
        }

        log.debug("Reload translation config!");

        IOException occuredIOException = null;

        for (int i = 0, maxRetryTimes = 3; i < maxRetryTimes; i++) {
            Request request = new Request.Builder()
                    .addHeader("user-agent", BingTranslator.DEFAULT_USER_AGENT)
                    .url(translatePageUrl)
                    .build();

            try (Response response = okHttpClient.newCall(request).execute()) {
                TranslateConfig config = parseTranslatorPage(response);

                log.info("Load bing translator config success");

                lock.lock();
                try {
                    translateConfig = config;
                    latestConfigLoadException = null;
                    loadConfigCondition.signalAll();
                    return;
                } finally {
                    lock.unlock();
                }
            } catch (IOException e) {
                log.error("Load bing translator config failed.", e);
                occuredIOException = e;
            }
        }

        latestConfigLoadException = new TranslationConfigLoadException("Load bing translator config failed, retry 3 times.", occuredIOException);
    }

    private boolean isExpirationSoon(TranslateConfig config) {
        return new Date().getTime() - reloadThreshold - config.getTokenTs() > config.getTokenExpiryInterval();
    }

    private TranslateConfig parseTranslatorPage(Response response) throws IOException {
        Pattern IGPattern = Pattern.compile("IG:\"([^\"]+)\"");
        Pattern IIDPattern = Pattern.compile("data-iid=\"([^\"]+)\"");
        Pattern paramsPattern = Pattern.compile("params_AbusePreventionHelper\\s*=\\s*\\[\\s*(\\d+),\\s*\"(.*?)\",\\s*(\\d+)\\s*\\]");
        ResponseBody responseBody = response.body();
        String bodyContent = Objects.nonNull(responseBody) ? responseBody.string() : "";
        TranslateConfig config = new TranslateConfig();

        if (bodyContent.isBlank()) {
            throw new IOException("Bing translator page is blank.");
        }

        Matcher IGMatcher = IGPattern.matcher(bodyContent);
        if (IGMatcher.find()) config.setIG(IGMatcher.group(1));

        Matcher IIDMatcher = IIDPattern.matcher(bodyContent);
        if (IIDMatcher.find()) config.setIID(IIDMatcher.group(1));

        Matcher paramsMatcher = paramsPattern.matcher(bodyContent);
        if (paramsMatcher.find()) {
            Long key = Long.valueOf(paramsMatcher.group(1));
            config.setKey(key);
            config.setTokenTs(key);
            config.setToken(paramsMatcher.group(2));
            config.setTokenExpiryInterval(Long.valueOf(paramsMatcher.group(3)));
        }

        Headers responseHeaders = response.headers();
        List<String> cookies = new ArrayList<>(responseHeaders.size());
        for (int i = 0, size = responseHeaders.size(); i < size; i++) {
            if ("set-cookie".equalsIgnoreCase(responseHeaders.name(i))) {
                String headerValue = responseHeaders.value(i);
                cookies.add(headerValue.split(";")[0]);
            }
        }

        config.setCookie(String.join("; ", cookies));
        config.setCount(0);

        return config;
    }

    /**
     * 获取翻译配置
     *
     * @return TranslateConfig 翻译配置，获取配置时被中断返回 null
     * @throws TranslationConfigLoadException 当获取翻译配置失败时，抛出该异常
     */
    public TranslateConfig getTranslateConfig() throws TranslationConfigLoadException {
        if (latestConfigLoadException != null) {
            throw latestConfigLoadException;
        }

        while (translateConfig.isTokenExpired()) {
            lock.lock();

            try {
                // 不自动续约时，加载新配置由当前请求线程完成
                if (!renewable) {
                    loadConfig();
                }
                // 自动续约时，加载新配置由定时线程完成
                // 完成后唤醒在该 condition 上等待的所有线程
                else {
                    loadConfigCondition.await();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            } finally {
                lock.unlock();
            }
        }

        return translateConfig;
    }

    /**
     * 关闭翻译配置管理器
     */
    public void close() {
        if (Objects.nonNull(scheduledExecutorService)) {
            scheduledExecutorService.shutdown();

            try {
                scheduledExecutorService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
