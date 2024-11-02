package com.khiemnv.cinezone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.khiemnv.cinezone.pages.HomeActivity;
import com.khiemnv.cinezone.pages.StartActivity;

import java.util.Locale;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra trạng thái đăng nhập
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // Nếu đã đăng nhập, chuyển đến HomeActivity
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            // Nếu chưa đăng nhập, chuyển đến StartActivity
            Intent intent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(intent);
        }

        finish(); // Đóng MainActivity sau khi điều hướng
    }
}
