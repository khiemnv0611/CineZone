package com.khiemnv.cinezone;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
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

        // Hiển thị layout loading chỉ một lần
        if (savedInstanceState == null) {
            setContentView(R.layout.loading_screen); // Layout loading

            // Đặt màu cho thanh trạng thái luôn là màu đen
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));

            // Tạo loading
            CircularProgressIndicator progressIndicator = findViewById(R.id.progress_bar);
            progressIndicator.setVisibility(View.VISIBLE);

            // Tải StartPageFragment sau 2 giây
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Sau khi tải xong, quay lại MainActivity
                runOnUiThread(() -> {
                    setContentView(R.layout.activity_main); // Chuyển sang layout chính

                    // Đặt màu cho thanh trạng thái luôn là màu đen
                    getWindow().setStatusBarColor(getResources().getColor(R.color.black));

                    // Thêm StartPageFragment vào fragment_container
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, new StartPageFragment());
                    transaction.commit();

                    // Lấy LinearLayout từ bố cục và tham chiếu các nút bên trong nó
                    LinearLayout topHeaderLayout = findViewById(R.id.topHeaderLayout);
                    Button changeLanguageButton = topHeaderLayout.findViewById(R.id.change_language);
                    Button changeThemeButton = topHeaderLayout.findViewById(R.id.change_theme);

                    // Cài đặt hệ thống UI để không cho thanh trạng thái bị ảnh hưởng
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

                    // Lưu các giá trị vào SharedPreferences khi người dùng thay đổi
                    changeLanguageButton.setOnClickListener(v -> {
                        isEnglish = !isEnglish;
                        setLocale(isEnglish ? "en" : "vi");
                        prefs.edit().putBoolean(KEY_IS_ENGLISH, isEnglish).apply();
                        recreateFragment();  // Cập nhật lại fragment
                    });

                    changeThemeButton.setOnClickListener(v -> {
                        isNightMode = !isNightMode;
                        AppCompatDelegate.setDefaultNightMode(
                                isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                        // Không thay đổi màu sắc thanh trạng thái ở đây
                        prefs.edit().putBoolean(KEY_IS_NIGHT_MODE, isNightMode).apply();
                    });
                });
            }).start();
        } else {
            // Nếu Activity đã được khởi tạo, hiển thị activity_main
            setContentView(R.layout.activity_main);
            // Đặt màu cho thanh trạng thái luôn là màu đen
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }
    }

    // Phương thức cập nhật lại fragment
    private void recreateFragment() {
        // Cập nhật fragment mà không cần khởi động lại activity
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new StartPageFragment());
        transaction.commit();
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
