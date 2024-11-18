package com.khiemnv.cinezone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khiemnv.cinezone.fragment.HomeFragment;
import com.khiemnv.cinezone.fragment.NotificationsFragment;
import com.khiemnv.cinezone.fragment.ProfileFragment;
import com.khiemnv.cinezone.fragment.SearchFragment;

public class MainActivity extends BaseActivity {
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kiểm tra Intent để xem có yêu cầu chuyển tới HomeFragment không
        if (getIntent().getBooleanExtra("navigate_to_home", false)) {
            navigateToHomeFragment();
        }

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
                selectedFragment = new NotificationsFragment();  // Fragment thông báo
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

    private void navigateToHomeFragment() {
        // Tạo đối tượng HomeFragment
        HomeFragment homeFragment = new HomeFragment();

        // Thực hiện thay thế fragment hiện tại bằng HomeFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit();
    }
}