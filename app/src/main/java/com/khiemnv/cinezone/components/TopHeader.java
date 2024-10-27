package com.khiemnv.cinezone.components;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import com.khiemnv.cinezone.R;

import java.util.Locale;

public class TopHeader extends LinearLayout {
    private boolean isEnglish;
    private boolean isNightMode;
    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_IS_ENGLISH = "isEnglish";
    private static final String KEY_IS_NIGHT_MODE = "isNightMode";

    private final SharedPreferences prefs;

    public TopHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.top_header, this, true);

        isEnglish = prefs.getBoolean(KEY_IS_ENGLISH, true);
        isNightMode = prefs.getBoolean(KEY_IS_NIGHT_MODE, false);

        Button changeLanguageButton = findViewById(R.id.change_language);
        changeLanguageButton.setOnClickListener(v -> {
            switchLanguage(context);
            Toast.makeText(context, isEnglish ? "Switched to English" : "Đã chuyển sang tiếng Việt", Toast.LENGTH_SHORT).show();
        });

        Button changeThemeButton = findViewById(R.id.change_theme);
        changeThemeButton.setOnClickListener(v -> {
            toggleTheme(context);
            Toast.makeText(context, isNightMode ? "Night Mode On" : "Day Mode On", Toast.LENGTH_SHORT).show();
        });
    }

    private void switchLanguage(Context context) {
        isEnglish = !isEnglish;
        setLocale(context, isEnglish ? "en" : "vi");

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_IS_ENGLISH, isEnglish).apply();

        // Notify Activity to refresh UI if needed
        if (context instanceof Activity) {
            ((Activity) context).recreate();
        }
    }

    private void setLocale(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    private void toggleTheme(Context context) {
        isNightMode = !isNightMode;

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_IS_NIGHT_MODE, isNightMode);
        editor.apply();

        AppCompatDelegate.setDefaultNightMode(
                isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        // If needed, force Activity to recreate to apply the theme
        if (context instanceof Activity) {
            ((Activity) context).recreate();
        }
    }
}
