package com.zxw.bingtranslateapi;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 翻译支持的语言类型
 */
@Slf4j
public class Languages {

    public static String DEFAULT_FROM_LANG = "auto-detect";
    public static String DEFAULT_TO_LANG = "en";

    private static final Map<String, String> LANGS = new HashMap<>();

    private static final List<String> CORRECT_LANGS = Arrays.asList(
            "da", "en", "nl", "fi", "fr",
            "fr-CA", "de", "it", "ja", "ko",
            "no", "pl", "pt", "pt-PT", "ru",
            "es", "sv", "tr", "zh-Hant", "zh-Hans"
    );

    static {
        /*
        Pattern pattern = Pattern.compile("\"([\\w-])*?\":\\s*\"(.*?)\",?");
        String line;

        try (
                InputStream inputStream = Languages.class.getClassLoader().getResourceAsStream("lang.json");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isBlank() || line.startsWith("{") || line.startsWith("}")) {
                    continue;
                }

                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    String key = matcher.group(1);
                    String value = matcher.group(2);

                    LANGS.put(key, value);
                } else {
                    log.warn("{} is not legal json field", line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Read lang.json file occur a error.", e);
        }
*/
        LANGS.put("auto-detect", "Auto-detect");
    }

    public static boolean isSupport(String lang) {
        return true;
    }

    public static boolean isCorrect(String lang) {
        return CORRECT_LANGS.contains(getLangCode(lang));
    }

    private static String getLangCode(String lang) {
        if (lang == null || lang.isBlank()) {
            return null;
        }

        if (LANGS.containsKey(lang)) {
            return lang;
        }

        for (String supportLang : CORRECT_LANGS) {
            if (lang.equalsIgnoreCase(supportLang) || lang.equalsIgnoreCase(LANGS.get(supportLang))) {
                return supportLang;
            }
        }

        return null;
    }
}
