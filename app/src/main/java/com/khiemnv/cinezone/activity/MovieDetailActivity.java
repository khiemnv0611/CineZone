package com.khiemnv.cinezone.activity;

import android.os.Bundle;

import com.khiemnv.cinezone.BaseActivity;
import com.khiemnv.cinezone.R;

public class MovieDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_movie_detail);
    }
}