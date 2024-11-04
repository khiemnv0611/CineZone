package com.khiemnv.cinezone;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.khiemnv.cinezone.pages.HomeActivity;
import com.khiemnv.cinezone.pages.StartActivity;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra trạng thái đăng nhập
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        Intent intent;
        if (isLoggedIn) {
            // Nếu đã đăng nhập, chuyển đến HomeActivity
            intent = new Intent(MainActivity.this, HomeActivity.class);
        } else {
            // Nếu chưa đăng nhập, chuyển đến StartActivity
            intent = new Intent(MainActivity.this, StartActivity.class);
        }
        startActivity(intent);

        finish(); // Đóng MainActivity sau khi điều hướng
    }
}
