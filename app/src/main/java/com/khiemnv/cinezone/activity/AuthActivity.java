package com.khiemnv.cinezone.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;

import com.khiemnv.cinezone.BaseActivity;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.fragment.SignInFragment;

public class AuthActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

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