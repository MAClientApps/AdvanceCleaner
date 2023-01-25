package com.lakshita.suman.advancecleaner.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.preference.PreferenceManager;

import androidx.annotation.RequiresApi;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/*
****** In Application
* @Override
    protected void attachBaseContext(Context base) {
        localeManager = new LocaleManager(base);
        super.attachBaseContext(localeManager.setLocale(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        localeManager.setLocale(this);
    }
*
****** In Activity
* @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(App.localeManager.setLocale(base));
    }
* */

public class LocaleManager {

    public static final String LANGUAGE_DEFAULT = "en";
    private static final String LANGUAGE_KEY = "KEY_LANGUAGE";
    private static LocaleManager localeManager;
    private final SharedPreferences prefs;
    private Context context;

    public static LocaleManager getInstance(Context context) {
        if (localeManager == null) {
            localeManager = new LocaleManager(context);
        }
        return localeManager;
    }

    public LocaleManager(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Context setLocale() {
        return updateResources(getPrefLanguage());
    }

    public Context setNewLocale(String language) {
        setPrefLanguage(language);
        return updateResources(language);
    }

    public String getPrefLanguage() {
        return prefs.getString(LANGUAGE_KEY, LANGUAGE_DEFAULT);
    }

    @SuppressLint("ApplySharedPref")
    public void setPrefLanguage(String language) {
        prefs.edit().putString(LANGUAGE_KEY, language).commit();
    }

    private Context updateResources(String language) {
//        Locale myLocale = new Locale(language);
//        Resources res = context.getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Configuration config = res.getConfiguration();
//        config.locale = myLocale;
//        context = context.createConfigurationContext(config);
//        res.updateConfiguration(config, dm);

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setLocaleForApi24(config, locale);
            context = context.createConfigurationContext(config);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setLocaleForApi24(Configuration config, Locale target) {
        Set<Locale> set = new LinkedHashSet<>();
        set.add(target);
        LocaleList all = LocaleList.getDefault();
        for (int i = 0; i < all.size(); i++) {
            set.add(all.get(i));
        }
        Locale[] locales = set.toArray(new Locale[0]);
        config.setLocales(new LocaleList(locales));
    }

    public static String[] lstLanguage = new String[]{
            "English",
            "Português", /*Bồ Đào Nha*/
            "Tiếng Việt",
            "русский", /*Nga*/
            "हिन्दी",/*Hindi*/
            "日本語",/*Nhật*/
            "한국어", /*Hàn*/
            "Türk", /*Turkish*/
            "French",
            "Spanish"
    };

    public static String[] lstCodeLanguage = new String[]{
            "en",
            "pt",
            "vi",
            "ru",
            "hi",
            "ja",
            "ko",
            "tr",
            "fr",
            "es"
    };

    public void restart(Activity activity) {
        Intent i = new Intent(activity, activity.getClass());
        activity.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}

