package com.khiemnv.cinezone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khiemnv.cinezone.activities.StartActivity;
import com.khiemnv.cinezone.fragments.HomeFragment;
import com.khiemnv.cinezone.fragments.ProfileFragment;
import com.khiemnv.cinezone.fragments.SearchFragment;

public class MainActivity extends BaseActivity {
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kiểm tra trạng thái đăng nhập
//        boolean isLoggedIn = getSharedPreferences("MyPrefs", MODE_PRIVATE)
//                .getBoolean("isLoggedIn", false);
//
//        if (!isLoggedIn) {
//            // Nếu chưa đăng nhập, chuyển đến StartActivity
//            Intent intent = new Intent(MainActivity.this, StartActivity.class);
//            startActivity(intent);
//            finish();
//            return; // Kết thúc onCreate nếu chưa đăng nhập
//        }

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
}
