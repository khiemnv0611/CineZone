package com.khiemnv.cinezone.components;

import android.annotation.SuppressLint;
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

import com.google.android.material.button.MaterialButton;
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

    public TopHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.top_header, this, true);

        isEnglish = prefs.getBoolean(KEY_IS_ENGLISH, true);
        isNightMode = prefs.getBoolean(KEY_IS_NIGHT_MODE, false);

        changeLanguageButton = findViewById(R.id.change_language); // Khởi tạo nút thay đổi ngôn ngữ
        updateLanguageIcon(); // Cập nhật biểu tượng ngôn ngữ ban đầu
        changeLanguageButton.setOnClickListener(v -> {
            switchLanguage(context);
            Toast.makeText(context, isEnglish ? "Switched to English" : "Đã chuyển sang tiếng Việt", Toast.LENGTH_SHORT).show();
        });

        changeThemeButton = findViewById(R.id.change_theme); // Khởi tạo nút thay đổi theme
        updateThemeIcon(); // Cập nhật biểu tượng ban đầu
        changeThemeButton.setOnClickListener(v -> {
            toggleTheme(context);
            Toast.makeText(context, isNightMode ? context.getString(R.string.night_mode) : context.getString(R.string.day_mode), Toast.LENGTH_SHORT).show();
        });
    }

    private void switchLanguage(Context context) {
        isEnglish = !isEnglish;
        setLocale(context, isEnglish ? "en" : "vi");

        prefs.edit().putBoolean(KEY_IS_ENGLISH, isEnglish).apply();

        updateLanguageIcon(); // Cập nhật biểu tượng ngôn ngữ khi chuyển đổi

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

        prefs.edit().putBoolean(KEY_IS_NIGHT_MODE, isNightMode).apply();
        AppCompatDelegate.setDefaultNightMode(
                isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        updateThemeIcon(); // Cập nhật biểu tượng khi thay đổi theme

        // If needed, force Activity to recreate to apply the theme
        if (context instanceof Activity) {
            ((Activity) context).recreate();
        }
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
            changeLanguageButton.setForeground(getResources().getDrawable(R.drawable.ic_united_states_flag, null));
        } else {
            changeLanguageButton.setForeground(getResources().getDrawable(R.drawable.ic_vietnam_flag, null));
        }
    }

}
