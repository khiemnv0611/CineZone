package com.khiemnv.cinezone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khiemnv.cinezone.activity.AuthActivity;
import com.khiemnv.cinezone.fragment.HomeFragment;
import com.khiemnv.cinezone.fragment.NotificationsFragment;
import com.khiemnv.cinezone.fragment.ProfileFragment;
import com.khiemnv.cinezone.fragment.SearchFragment;

public class MainActivity extends BaseActivity {

    private long backPressedTime = 0;
    private Toast backToast;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra trạng thái đăng nhập từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        if (token == null) {
            // Chuyển đến AuthActivity nếu không có token
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Đã đăng nhập, hiển thị MainActivity
            setContentView(R.layout.activity_main);

            // Kiểm tra Intent để xem có yêu cầu chuyển tới HomeFragment không
            if (getIntent().getBooleanExtra("navigate_to_home", false)) {
                navigateToHomeFragment();
            }

            // Nhận trạng thái admin từ Intent
            boolean isAdmin = getIntent().getBooleanExtra("isAdmin", false);

            // Cấu hình BottomNavigationView
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

            // Hiển thị HomeFragment mặc định nếu đây là lần đầu tiên tạo activity
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
            }

            // Thiết lập listener cho BottomNavigationView
            bottomNavigationView.setOnItemSelectedListener(item -> {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                } else if (item.getItemId() == R.id.nav_search) {
                    selectedFragment = new SearchFragment();
                } else if (item.getItemId() == R.id.nav_notifications) {
                    selectedFragment = new NotificationsFragment();
                } else if (item.getItemId() == R.id.nav_profile) {
                    selectedFragment = new ProfileFragment();
                }

                // Thực hiện thay đổi fragment nếu có item hợp lệ được chọn
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                    return true;
                }

                return false; // Nếu selectedFragment null, không thay đổi
            });
        }

        // Đăng ký callback để xử lý phím Back
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Xử lý khi nhấn Back
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    if (backToast != null) backToast.cancel();
                    moveTaskToBack(true);
                } else {
                    backToast = Toast.makeText(MainActivity.this, R.string.back_press_exit_message, Toast.LENGTH_SHORT);
                    backToast.show();
                }
                backPressedTime = System.currentTimeMillis();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (backToast != null) backToast.cancel();
        super.onDestroy();
    }

    // Hàm điều hướng tới HomeFragment
    private void navigateToHomeFragment() {
        // Tạo đối tượng HomeFragment
        HomeFragment homeFragment = new HomeFragment();

        // Thực hiện thay thế fragment hiện tại bằng HomeFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit();
    }
}
