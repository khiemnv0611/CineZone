package com.khiemnv.cinezone;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.khiemnv.cinezone.activities.StartActivity;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra trạng thái đăng nhập
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        Intent intent;
        if (!isLoggedIn) {
            // Nếu chưa đăng nhập, chuyển đến StartActivity
            intent = new Intent(MainActivity.this, StartActivity.class);
        } else {
            // Nếu đã đăng nhập, vào MainActivity
            intent = new Intent(MainActivity.this, MainActivity.class);
        }
        startActivity(intent);

        finish(); // Đóng MainActivity sau khi điều hướng
    }
}
