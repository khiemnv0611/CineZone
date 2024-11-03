package com.khiemnv.cinezone.pages;

import android.os.Bundle;

import com.khiemnv.cinezone.BaseActivity;
import com.khiemnv.cinezone.R;

public class AuthActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Hiển thị fragment ban đầu
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_container, new SignUpFragment())
                .commit();
    }
}