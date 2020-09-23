package com.github.obelieve.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.github.obelieve.repository.cache.PreferenceUtil;
import com.github.obelieve.repository.cache.constant.PreferenceConst;
import com.github.obelieve.repository.cache.constant.SystemValue;

import java.util.Locale;


public class LanguageUtil {

    public final static String LANGUAGE_NONE = "";//
    public final static String LANGUAGE_CHINESE = "zh-rCN";//简体中文
    public final static String LANGUAGE_TAIWAN = "zh-rTW";//繁体中文
    public final static String LANGUAGE_ENGLISH = "en";//英语
    public final static String LANGUAGE_JAPANESE = "ja";//日语
    public final static String LANGUAGE_GERMAN = "de";//德语
    public final static String LANGUAGE_RUSSIAN = "ru";//俄语

    public final static int LANGUAGE_NONE_INDEX = 0;//跟随系统语言
    public final static int LANGUAGE_CHINESE_INDEX = 1;//简体中文
    public final static int LANGUAGE_TAIWAN_INDEX = 2;//繁体中文
    public final static int LANGUAGE_ENGLISH_INDEX = 3;//英语
    public final static int LANGUAGE_JAPANESE_INDEX = 4;//日语
    public final static int LANGUAGE_GERMAN_INDEX = 5;//德语
    public final static int LANGUAGE_RUSSIAN_INDEX = 6;//俄语


    private static LanguageUtil instance;

    private LanguageUtil() {

    }

    public static LanguageUtil getInstance() {
        if (instance == null) {
            synchronized (LanguageUtil.class) {
                if (instance == null) {
                    instance = new LanguageUtil();
                }
            }
        }
        return instance;
    }

    private void changleLocale(Context context) {
        String languageOption = getLanguagePreference(context);
        if (languageOption != LANGUAGE_NONE) {
            Locale locale;
            switch (languageOption) {
                case LANGUAGE_ENGLISH:
                    locale = Locale.ENGLISH;
                    break;
                case LANGUAGE_CHINESE:
                    locale = Locale.SIMPLIFIED_CHINESE;
                    break;
                case LANGUAGE_TAIWAN:
                    locale = Locale.TAIWAN;
                    break;
                case LANGUAGE_JAPANESE:
                    locale = Locale.JAPAN;;
                    break;
                case LANGUAGE_GERMAN:
                    locale = Locale.GERMAN;
                    break;
                case LANGUAGE_RUSSIAN:
                    locale = new Locale("ru", "RU");
                    break;
                default:
                    locale = Locale.getDefault();
                    break;
            }
            Configuration config = context.getResources().getConfiguration();
            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            if (languageOption == LANGUAGE_TAIWAN){
                SystemValue.language = "zh-Hant";
            }else {
                SystemValue.language = locale.getLanguage();
            }
        } else {

        }
    }

    public void changeLanguage(Context context) {
        changleLocale(context);
    }

    public String getLanguagePreference(Context context) {
        return PreferenceUtil.getString(context, PreferenceConst.LANGUAGE_INDEX, LANGUAGE_NONE);
    }

    public String getLanguage(int index) {
        String language = LANGUAGE_NONE;
        switch (index) {
            case LANGUAGE_ENGLISH_INDEX:
                language = LANGUAGE_ENGLISH;
                break;
            case LANGUAGE_CHINESE_INDEX:
                language = LANGUAGE_CHINESE;
                break;
            case LANGUAGE_TAIWAN_INDEX:
                language = LANGUAGE_TAIWAN;
                break;
            case LANGUAGE_JAPANESE_INDEX:
                language = LANGUAGE_JAPANESE;
                break;
            case LANGUAGE_GERMAN_INDEX:
                language = LANGUAGE_GERMAN;
                break;
            case LANGUAGE_RUSSIAN_INDEX:
                language = LANGUAGE_RUSSIAN;
                break;
            default:
                language = Locale.getDefault().getLanguage();
                break;
        }
        return language;
    }

    public int getLanguageIndex(Context context, String str) {
        int index = LANGUAGE_NONE_INDEX;
        switch (str) {
            case LANGUAGE_ENGLISH:
                index = LANGUAGE_ENGLISH_INDEX;
                break;
            case LANGUAGE_CHINESE:
                index = LANGUAGE_CHINESE_INDEX;
                break;
            case LANGUAGE_TAIWAN:
                index = LANGUAGE_TAIWAN_INDEX;
                break;
            case LANGUAGE_JAPANESE:
                index = LANGUAGE_JAPANESE_INDEX;
                break;
            case LANGUAGE_GERMAN:
                index = LANGUAGE_GERMAN_INDEX;
                break;
            case LANGUAGE_RUSSIAN:
                index = LANGUAGE_RUSSIAN_INDEX;
                break;
        }
        return index;
    }

    public void setLanguagePreference(Context context, String languagePreference) {
        if (languagePreference == LANGUAGE_TAIWAN){
            SystemValue.language = "zh-Hant";
        }else {
            SystemValue.language = languagePreference;
        }
        PreferenceUtil.putString(context, PreferenceConst.LANGUAGE_INDEX, languagePreference);
    }

    public int getCurrentLanguageIndex(Context context) {
        String currentLanguagePreference = LanguageUtil.getInstance().getLanguagePreference(context);
//        if(currentLanguageIndex == LanguageUtil.LANGUAGE_NONE){
//            Configuration config = context.getResources().getConfiguration();
//            Locale locale = config.locale;
//            String language = locale.getLanguage();
//            String languageArray[] = context.getResources().getStringArray(R.array.language_short);
//            if(languageArray == null || TextUtils.isEmpty(language)){
//                return LANGUAGE_NONE_INDEX;
//            }
//            for(int index = 0 ; index< languageArray.length;index++){
//                if(language.equals(languageArray[index])){
//                    return index;
//                }
//            }
//            return LANGUAGE_NONE_INDEX;
//        }else{
        return LanguageUtil.getInstance().getLanguageIndex(context, currentLanguagePreference);
//        }
    }

    public String getCurrentLanguage(Context context) {
        String currentLanguagePreference = LANGUAGE_NONE;
        currentLanguagePreference = LanguageUtil.getInstance().getLanguagePreference(context);
        if (TextUtils.isEmpty(currentLanguagePreference)) {
            String lan = Locale.getDefault().getLanguage();
            String country = Locale.getDefault().getCountry();
            currentLanguagePreference = lan;
        }
        if (currentLanguagePreference == LANGUAGE_TAIWAN || currentLanguagePreference.equals("zh-rTW")){
            currentLanguagePreference = "zh-Hant";
        }
        return currentLanguagePreference;
    }
}
