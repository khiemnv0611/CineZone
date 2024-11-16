package com.khiemnv.cinezone.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khiemnv.cinezone.BaseActivity;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.adapter.ActorAdapter;
import com.khiemnv.cinezone.adapter.MovieAdapter;
import com.khiemnv.cinezone.model.Actor;
import com.khiemnv.cinezone.model.MovieModel;
import com.khiemnv.cinezone.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieDetailActivity extends BaseActivity {
    private TextView movieTitle, movieGenre, movieType, movieAgeRating, movieStatus, movieDescription,
            movieCountry, movieSeason, movieDirectors, movieProductionCompanies, movieReleaseDate, movieDuration,
            movieViewCount, movieAverageRating, movieTotalRatings;
    private ImageView moviePoster;
    private RecyclerView recyclerViewActors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khóa màn hình dọc
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Ẩn thanh trạng thái (status bar) và cho giao diện tràn lên
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie_detail);

        // Ẩn padding hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.movie_detail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các view
        movieTitle = findViewById(R.id.movieTitle);
        movieGenre = findViewById(R.id.movieGenre);
        movieType = findViewById(R.id.movieType);
        movieAgeRating = findViewById(R.id.movieAgeRating);
        movieStatus = findViewById(R.id.movieStatus);
        movieDescription = findViewById(R.id.movieDescription);
        movieCountry = findViewById(R.id.movieCountry);
        movieSeason = findViewById(R.id.movieSeason);
        movieDirectors = findViewById(R.id.movieDirectors);
        movieProductionCompanies = findViewById(R.id.movieProductionCompanies);
        movieReleaseDate = findViewById(R.id.movieReleaseDate);
        movieDuration = findViewById(R.id.movieDuration);
        movieViewCount = findViewById(R.id.movieViewCount);
        movieAverageRating = findViewById(R.id.movieAverageRating);
        movieTotalRatings = findViewById(R.id.movieTotalRatings);
        moviePoster = findViewById(R.id.moviePoster);

        // Ánh xạ RecyclerView
        recyclerViewActors = findViewById(R.id.recyclerViewActors);
        recyclerViewActors.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String currentMovieId = intent.getStringExtra("movieId");
        String title = intent.getStringExtra("title");
        String genre = intent.getStringExtra("genre");
        String type = intent.getStringExtra("type");
        int ageRating = intent.getIntExtra("ageRating", -1);
        String status = intent.getStringExtra("status");
        String description = intent.getStringExtra("description");
        String country = intent.getStringExtra("country");
        String season = intent.getStringExtra("season");
        String directors = intent.getStringExtra("directors");
        String productionCompanies = intent.getStringExtra("productionCompanies");
        String releaseDate = intent.getStringExtra("releaseDate");
        String duration = intent.getStringExtra("duration");
        String viewCount = intent.getStringExtra("viewCount");
        double averageRating = intent.getDoubleExtra("averageRating", 0);
        String totalRatings = intent.getStringExtra("totalRatings");
        String imageUrl = intent.getStringExtra("imageUrl");
        String trailerUrl = intent.getStringExtra("trailerUrl");
        List<Actor> actors = (List<Actor>) getIntent().getSerializableExtra("actors");

        // Hiển thị dữ liệu lên giao diện
        movieTitle.setText(title);
        List<String> genreList;

        if (!"N/A".equals(genre)) {
            genreList = Arrays.asList(genre.split(",\\s*")); // Tách bởi dấu phẩy và khoảng trắng
        } else {
            genreList = new ArrayList<>(); // Nếu không có thể loại, tạo danh sách trống
        }
        movieGenre.setText(genre);

        movieType.setText(type);

        if (ageRating > 0) {
            movieAgeRating.setText(ageRating + "+");
        } else {
            movieAgeRating.setText(String.valueOf(ageRating));
        }

        movieStatus.setText(status);
        if ("Hoàn thành".equals(status)) {
            movieStatus.setBackgroundResource(R.drawable.border_status_green);
        } else if ("Sắp ra mắt".equals(status)) {
            movieStatus.setBackgroundResource(R.drawable.border_status_red);
        } else {
            movieStatus.setBackgroundResource(R.drawable.border_status_blue);
        }

        movieDescription.setText(description);
        movieCountry.setText(country);
        movieSeason.setText(season);
        movieDirectors.setText(directors);
        movieProductionCompanies.setText(productionCompanies);
        movieReleaseDate.setText(releaseDate);
        movieDuration.setText(duration);

        String views = getString(R.string.views);
        movieViewCount.setText(viewCount + " " + views);

        movieAverageRating.setText("⭐ " + averageRating + "/10");

        String ratings = getString(R.string.ratings);
        movieTotalRatings.setText("( " + totalRatings + " " + ratings + " )");

        if (actors != null && !actors.isEmpty()) {
            ActorAdapter actorAdapter = new ActorAdapter(this, actors);
            recyclerViewActors.setAdapter(actorAdapter);
        }

        // Tải ảnh poster của phim (nếu có)
        Glide.with(this).load(imageUrl).into(moviePoster);

        // Lấy RecyclerView cho danh sách phim cùng thể loại
        RecyclerView recyclerViewSimilarMovies = findViewById(R.id.recyclerViewSimilarMovies);
        recyclerViewSimilarMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Tạo Adapter
        MovieAdapter similarMoviesAdapter = new MovieAdapter(this, true);
        recyclerViewSimilarMovies.setAdapter(similarMoviesAdapter);

        // Lấy ViewModel và gọi phương thức
        MovieViewModel movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        movieViewModel.getSuggestedMoviesByGenres(genreList, currentMovieId).observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                similarMoviesAdapter.setMovieList(movieModels);
            }
        });

        // Nút trailer
        LinearLayout trailerButton = findViewById(R.id.trailer_btn);
        trailerButton.setOnClickListener(v -> openTrailerActivity(trailerUrl));
    }

    private void openTrailerActivity(String trailerUrl) {
        if (trailerUrl == null || trailerUrl.isEmpty()) {
            Toast.makeText(this, "Trailer không khả dụng", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, TrailerActivity.class);
        intent.putExtra("trailerUrl", trailerUrl); // Truyền URL video
        startActivity(intent);
    }
}