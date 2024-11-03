package com.khiemnv.cinezone.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;
import com.khiemnv.cinezone.BaseActivity;
import com.khiemnv.cinezone.R;

public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.topHeaderLayout);

//        MaterialButton btnLogin = findViewById(R.id.btnSignUp);
//        btnLogin.setOnClickListener(v -> {
//            // Logic xác thực đăng nhập ở đây
//            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//            startActivity(intent);
//        });
    }
}

