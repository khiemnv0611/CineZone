package com.khiemnv.cinezone.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.khiemnv.cinezone.BaseActivity;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.utils.FullScreenHelper;
import com.khiemnv.cinezone.utils.YouTubePlayerHelper;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class TrailerActivity extends BaseActivity {
    private YouTubePlayerView trailerPlayerView;
    private ImageView fullScreenButton, collapseButton;
    private YouTubePlayerHelper youTubePlayerHelper;
    private FullScreenHelper fullScreenHelper;
    private TextView movieTitleWithTrailer;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        // Ánh xạ các thành phần UI
        trailerPlayerView = findViewById(R.id.trailerPlayerView);
        fullScreenButton = findViewById(R.id.fullScreenButton);
        collapseButton = findViewById(R.id.collapseButton);
        movieTitleWithTrailer = findViewById(R.id.movieTitleWithTrailer);
        backButton = findViewById(R.id.back_button);

        String title = getIntent().getStringExtra("title");
        if (title != null) {
            movieTitleWithTrailer.setText(title + " - Trailer");
        }

        // Khởi tạo YouTubePlayerHelper
        youTubePlayerHelper = new YouTubePlayerHelper();

        // Khởi tạo FullScreenHelper
        fullScreenHelper = new FullScreenHelper(this, trailerPlayerView, fullScreenButton, collapseButton);

        String trailerUrl = getIntent().getStringExtra("trailerUrl");
        if (trailerUrl != null) {
            youTubePlayerHelper.playVideo(trailerPlayerView, trailerUrl);
        }

        // Xử lý phóng to và thu nhỏ
        fullScreenButton.setOnClickListener(v -> fullScreenHelper.toggleFullScreen(true));
        collapseButton.setOnClickListener(v -> fullScreenHelper.toggleFullScreen(false));

        // Quản lý vòng đời của YouTubePlayerView
        getLifecycle().addObserver(trailerPlayerView);

        // Thiết lập giao diện ban đầu
        fullScreenHelper.updateButtonVisibility(getResources().getConfiguration().orientation);

        // Nếu là chế độ ngang ngay từ đầu, gọi toggleFullScreen(true)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fullScreenHelper.toggleFullScreen(true);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        fullScreenHelper.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (fullScreenHelper.isFullScreen()) {
            fullScreenHelper.toggleFullScreen(false); // Thu nhỏ video trước khi thoát
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        youTubePlayerHelper.onPause(); // Dừng video khi ứng dụng không hoạt động
    }

    @Override
    protected void onResume() {
        super.onResume();
        youTubePlayerHelper.onResume(); // Tiếp tục video khi quay lại ứng dụng
    }
}