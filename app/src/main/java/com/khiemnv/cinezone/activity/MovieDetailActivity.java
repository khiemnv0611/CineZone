package com.khiemnv.cinezone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.khiemnv.cinezone.BaseActivity;
import com.khiemnv.cinezone.R;

public class MovieDetailActivity extends BaseActivity {
    private TextView movieTitle, movieGenre, movieRating;
    private ImageView moviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_movie_detail);

        // Ánh xạ các view
        movieTitle = findViewById(R.id.movieTitle);
        movieGenre = findViewById(R.id.movieGenre);
        movieRating = findViewById(R.id.movieRating);
        moviePoster = findViewById(R.id.moviePoster);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String genre = intent.getStringExtra("genre");
        double rating = intent.getDoubleExtra("rating", 0);
        String imageUrl = intent.getStringExtra("imageUrl");

        // Hiển thị dữ liệu lên giao diện
        movieTitle.setText(title);
        movieGenre.setText(genre);
        movieRating.setText("⭐ " + rating);

        // Tải ảnh poster của phim (nếu có)
        Glide.with(this).load(imageUrl).into(moviePoster);
    }
}