package com.khiemnv.cinezone.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.button.MaterialButton;
import com.khiemnv.cinezone.BaseActivity;
import com.khiemnv.cinezone.R;

import java.util.Locale;

public class TopHeader extends LinearLayout {
    private boolean isEnglish;
    private boolean isNightMode;
    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_IS_ENGLISH = "isEnglish";
    private static final String KEY_IS_NIGHT_MODE = "isNightMode";

    private final SharedPreferences prefs;
    private MaterialButton changeThemeButton;
    private MaterialButton changeLanguageButton;
    private BaseActivity baseActivity;

    public TopHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        baseActivity = (BaseActivity) context;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.top_header, this, true);

        // Read state from SharedPreferences
        isEnglish = prefs.getBoolean(KEY_IS_ENGLISH, true);
        isNightMode = prefs.getBoolean(KEY_IS_NIGHT_MODE, false);

        changeLanguageButton = findViewById(R.id.change_language);
        updateLanguageIcon();
        changeLanguageButton.setOnClickListener(v -> {
            baseActivity.switchLanguage(); // Call switchLanguage from BaseActivity
            updateLanguageIcon(); // Update icon based on new language state
            Toast.makeText(context, isEnglish ? "Switched to English" : "Đã chuyển sang tiếng Việt", Toast.LENGTH_SHORT).show();
        });

        changeThemeButton = findViewById(R.id.change_theme);
        updateThemeIcon();
        changeThemeButton.setOnClickListener(v -> {
            toggleTheme(context);
            Toast.makeText(context, isNightMode ? context.getString(R.string.night_mode) : context.getString(R.string.day_mode), Toast.LENGTH_SHORT).show();
        });
    }

    private void toggleTheme(Context context) {
        isNightMode = !isNightMode;
        prefs.edit().putBoolean(KEY_IS_NIGHT_MODE, isNightMode).apply();
        AppCompatDelegate.setDefaultNightMode(
                isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        updateThemeIcon();
    }

    private void updateThemeIcon() {
        if (isNightMode) {
            changeThemeButton.setIconResource(R.drawable.ic_night_mode);
        } else {
            changeThemeButton.setIconResource(R.drawable.ic_light_mode);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateLanguageIcon() {
        if (isEnglish) {
            changeLanguageButton.setForeground(AppCompatResources.getDrawable(getContext(), R.drawable.ic_united_states_flag));
        } else {
            changeLanguageButton.setForeground(AppCompatResources.getDrawable(getContext(), R.drawable.ic_vietnam_flag));
        }
    }
}
