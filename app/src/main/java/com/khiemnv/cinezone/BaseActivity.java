package com.khiemnv.cinezone;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_IS_ENGLISH = "isEnglish";
    private static final String KEY_IS_NIGHT_MODE = "isNightMode";

    public boolean isEnglish;
    protected boolean isNightMode;
    protected SharedPreferences prefs;

    private CircularProgressIndicator progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Khởi tạo SharedPreferences
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isEnglish = prefs.getBoolean(KEY_IS_ENGLISH, true);
        isNightMode = prefs.getBoolean(KEY_IS_NIGHT_MODE, false);

        // Áp dụng chủ đề và ngôn ngữ
        applyTheme();
        setLocale(isEnglish ? "en" : "vi");

        setStatusBarColor();

        // Loading
        progressBar = findViewById(R.id.progress_bar);
    }

    protected void setStatusBarColor() {
        // Đặt màu cho thanh trạng thái
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));
    }

    private void applyTheme() {
        // Áp dụng chủ đề đã lưu
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

    // Phương thức có thể được gọi từ TopHeader để lưu trạng thái ngôn ngữ
    public void switchLanguage() {
        isEnglish = !isEnglish;
        prefs.edit().putBoolean(KEY_IS_ENGLISH, isEnglish).apply();
        setLocale(isEnglish ? "en" : "vi"); // Thiết lập ngôn ngữ mới
        recreate(); // Khởi động lại activity để áp dụng thay đổi
    }

    // Hiển thị loading
    public void showLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    // Ẩn loading
    public void hideLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    // Phương thức cho phép nạp layout riêng cho từng Activity kế thừa
    protected void setContentLayout(int layoutResID) {
        ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
        getLayoutInflater().inflate(layoutResID, rootView, true);
    }
}
