package com.khiemnv.cinezone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.ViewCompat;

import com.khiemnv.cinezone.pages.LoginActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private boolean isEnglish;
    private boolean isNightMode;
    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_IS_ENGLISH = "isEnglish";
    private static final String KEY_IS_NIGHT_MODE = "isNightMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load ngôn ngữ và theme từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isEnglish = prefs.getBoolean(KEY_IS_ENGLISH, true);
        isNightMode = prefs.getBoolean(KEY_IS_NIGHT_MODE, false);

        // Thiết lập ngôn ngữ và theme ngay khi khởi chạy
        setLocale(isEnglish ? "en" : "vi");
        AppCompatDelegate.setDefaultNightMode(isNightMode ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lấy LinearLayout từ bố cục và tham chiếu các nút bên trong nó
        LinearLayout topHeaderLayout = findViewById(R.id.topHeaderLayout);
        Button changeLanguageButton = topHeaderLayout.findViewById(R.id.change_language);
        Button changeThemeButton = topHeaderLayout.findViewById(R.id.change_theme);


        // Thay đổi màu nền thanh trạng thái
        getWindow().setStatusBarColor(getResources().getColor(R.color.black)); // Đặt màu cho thanh trạng thái
        // Cài đặt hệ thống UI để không cho thanh trạng thái bị ảnh hưởng
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        // Lưu các giá trị vào SharedPreferences khi người dùng thay đổi
        changeLanguageButton.setOnClickListener(v -> {
            isEnglish = !isEnglish;
            setLocale(isEnglish ? "en" : "vi");
            prefs.edit().putBoolean(KEY_IS_ENGLISH, isEnglish).apply();
            recreate();  // Tải lại Activity để áp dụng ngôn ngữ
        });

        changeThemeButton.setOnClickListener(v -> {
            isNightMode = !isNightMode;
            AppCompatDelegate.setDefaultNightMode(
                    isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            prefs.edit().putBoolean(KEY_IS_NIGHT_MODE, isNightMode).apply();
            recreate();  // Tải lại Activity để áp dụng theme
        });

        Button buttonLogin = findViewById(R.id.start);
        buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
