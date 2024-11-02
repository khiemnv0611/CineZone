package com.khiemnv.cinezone;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_IS_ENGLISH = "isEnglish";
    private static final String KEY_IS_NIGHT_MODE = "isNightMode";

    protected boolean isEnglish;
    protected boolean isNightMode;
    protected SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize SharedPreferences
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isEnglish = prefs.getBoolean(KEY_IS_ENGLISH, true);
        isNightMode = prefs.getBoolean(KEY_IS_NIGHT_MODE, false);

        // Apply theme and language
        applyTheme();
        setLocale(isEnglish ? "en" : "vi"); // Ensure the correct language is set

        setStatusBarColor();
    }

    private void setStatusBarColor() {
        // Đặt màu cho thanh trạng thái
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));
    }

    private void applyTheme() {
        // Apply the saved theme
        AppCompatDelegate.setDefaultNightMode(
                isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    // Method can be called from TopHeader to save the language state
    public void switchLanguage() {
        isEnglish = !isEnglish;
        prefs.edit().putBoolean(KEY_IS_ENGLISH, isEnglish).apply();
        setLocale(isEnglish ? "en" : "vi"); // Set the new language
        recreate(); // Restart activity to apply changes
    }
}
