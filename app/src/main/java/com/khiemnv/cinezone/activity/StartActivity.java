package com.khiemnv.cinezone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.khiemnv.cinezone.BaseActivity;
import com.khiemnv.cinezone.R;

public class StartActivity extends BaseActivity {
    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        showLoading();
//
//        new Handler().postDelayed(() -> {
//            hideLoading();
//            setContentLayout(R.layout.activity_start);
//
//            // Thiết lập top header và nút Start
//            findViewById(R.id.topHeaderLayout);
//
//            MaterialButton btnStart = findViewById(R.id.btnStart);
//            btnStart.setOnClickListener(v -> {
//                // Điều hướng tới AuthActivity
//                Intent intent = new Intent(StartActivity.this, AuthActivity.class);
//                startActivity(intent);
//
//                // Hiệu ứng chuyển màn hình
//                overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
//            });
//        }, 2000);

        setContentView(R.layout.activity_start);

        // Thiết lập top header
        findViewById(R.id.topHeaderLayout);

        MaterialButton btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(v -> {
            // Điều hướng tới AuthActivity
            Intent intent = new Intent(StartActivity.this, AuthActivity.class);
            startActivity(intent);

            // Hiệu ứng chuyển màn hình
            overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
        });
    }
};

