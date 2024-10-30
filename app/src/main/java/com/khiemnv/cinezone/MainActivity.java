package com.khiemnv.cinezone;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.khiemnv.cinezone.pages.LoginFragment;
import com.khiemnv.cinezone.pages.StartPageFragment;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private boolean isEnglish;
    private boolean isNightMode;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_IS_ENGLISH = "isEnglish";
    private static final String KEY_IS_NIGHT_MODE = "isNightMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Khởi tạo SharedPreferences
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load ngôn ngữ và theme từ SharedPreferences
        isEnglish = prefs.getBoolean(KEY_IS_ENGLISH, true);
        isNightMode = prefs.getBoolean(KEY_IS_NIGHT_MODE, false);

        // Thiết lập ngôn ngữ và theme ngay khi khởi chạy
        setLocale(isEnglish ? "en" : "vi");
        AppCompatDelegate.setDefaultNightMode(isNightMode ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);

        // Hiển thị layout loading chỉ một lần
        if (savedInstanceState == null) {
            setContentView(R.layout.loading_screen); // Layout loading

            // Đặt màu cho thanh trạng thái luôn là màu đen
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));

            // Tạo loading
            CircularProgressIndicator progressIndicator = findViewById(R.id.progress_bar);
            progressIndicator.setVisibility(View.VISIBLE);

            // Chuyển sang layout chính sau 2 giây
            new Handler().postDelayed(() -> loadMainActivity(), 2000);
        } else {
            loadMainActivity();
        }
    }

    private void loadMainActivity() {
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        // Thêm fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new StartPageFragment())
                .commit();

        // Lấy các nút từ TopHeader
        LinearLayout topHeaderLayout = findViewById(R.id.topHeaderLayout);
        Button changeLanguageButton = topHeaderLayout.findViewById(R.id.change_language);
        Button changeThemeButton = topHeaderLayout.findViewById(R.id.change_theme);

        changeLanguageButton.setOnClickListener(v -> {
            isEnglish = !isEnglish;
            prefs.edit().putBoolean(KEY_IS_ENGLISH, isEnglish).apply();
            setLocale(isEnglish ? "en" : "vi");
            recreate(); // Khởi động lại Activity để áp dụng ngôn ngữ mới
        });

        changeThemeButton.setOnClickListener(v -> {
            isNightMode = !isNightMode;
            prefs.edit().putBoolean(KEY_IS_NIGHT_MODE, isNightMode).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    // Login
    public void showLoginFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Sử dụng hiệu ứng slide
        transaction.setCustomAnimations(
                R.anim.slide_in_right,    // Hiệu ứng khi vào
                R.anim.slide_out_left,  // Hiệu ứng khi ra
                R.anim.slide_in_left,   // Hiệu ứng khi quay lại
                R.anim.slide_out_right);  // Hiệu ứng khi quay lại

        transaction.replace(R.id.fragment_container, new LoginFragment());
        transaction.addToBackStack(null); // Cho phép quay lại fragment trước đó
        transaction.commit();
    }
}
