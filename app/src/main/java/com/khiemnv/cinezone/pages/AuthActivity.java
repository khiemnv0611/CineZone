package com.khiemnv.cinezone.pages;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;

import com.khiemnv.cinezone.BaseActivity;
import com.khiemnv.cinezone.R;

public class AuthActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_auth);

        // Hiển thị fragment ban đầu
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_container, new SignInFragment())
                .commit();

        // Thiết lập OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                // Hiệu ứng quay lại
                overridePendingTransition(R.anim.stay, R.anim.slide_out_down);
            }
        });

        // Close btn
        ImageView closeButton = findViewById(R.id.btnClose);
        closeButton.setOnClickListener(v -> {
            finish();
            // Hiệu ứng quay lại
            overridePendingTransition(R.anim.stay, R.anim.slide_out_down);
        });
    }
}