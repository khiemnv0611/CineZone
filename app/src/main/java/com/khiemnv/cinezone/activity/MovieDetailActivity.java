package com.khiemnv.cinezone.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.khiemnv.cinezone.BaseActivity;
import com.khiemnv.cinezone.R;

public class MovieDetailActivity extends BaseActivity {
    private TextView movieTitle, movieGenre, movieType, movieAgeRating, movieStatus, movieDescription,
            movieCountry, movieProductionCompanies, movieReleaseDate, movieDuration, movieViewCount, movieAverageRating, movieTotalRatings;
    private ImageView moviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Ánh xạ các view
        movieTitle = findViewById(R.id.movieTitle);
        movieGenre = findViewById(R.id.movieGenre);
//        movieType = findViewById(R.id.movieType);
        movieAgeRating = findViewById(R.id.movieAgeRating);
//        movieStatus = findViewById(R.id.movieStatus);
        movieDescription = findViewById(R.id.movieDescription);
//        movieCountry = findViewById(R.id.movieCountry);
//        movieProductionCompanies = findViewById(R.id.movieProductionCompanies);
        movieReleaseDate = findViewById(R.id.movieReleaseDate);
        movieDuration = findViewById(R.id.movieDuration);
//        movieViewCount = findViewById(R.id.movieViewCount);
        movieAverageRating = findViewById(R.id.movieAverageRating);
        movieTotalRatings = findViewById(R.id.movieTotalRatings);
        moviePoster = findViewById(R.id.moviePoster);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String genre = intent.getStringExtra("genre");
//        movieType.setText(intent.getStringExtra("type"));
        String ageRating = intent.getStringExtra("ageRating");
        String description = intent.getStringExtra("description");
        String releaseDate = intent.getStringExtra("releaseDate");
        String duration = intent.getStringExtra("duration");
        double averageRating = intent.getDoubleExtra("averageRating", 0);
        String totalRatings = intent.getStringExtra("totalRatings");
        String imageUrl = intent.getStringExtra("imageUrl");

        // Hiển thị dữ liệu lên giao diện
        movieTitle.setText(title);
        movieGenre.setText(genre);
//        movieType.setText(type);
        movieAgeRating.setText(ageRating);
        movieDescription.setText(description);
        movieReleaseDate.setText(releaseDate);
        movieDuration.setText(duration);
        movieAverageRating.setText("⭐ " + averageRating + "/10");
        movieTotalRatings.setText("( " + totalRatings + " reviews )");

        // Tải ảnh poster của phim (nếu có)
        Glide.with(this).load(imageUrl).into(moviePoster);
    }
}