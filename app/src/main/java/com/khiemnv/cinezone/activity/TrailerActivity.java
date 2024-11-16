package com.khiemnv.cinezone.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.khiemnv.cinezone.BaseActivity;
import com.khiemnv.cinezone.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class TrailerActivity extends BaseActivity {
    private YouTubePlayerView trailerPlayerView;
    private ImageView rotateToLandscapeButton, fullScreenButton, collapseButton, rotateToPortraitButton;
    private YouTubePlayer youTubePlayerInstance;
    private float currentSecond = 0f; // Lưu thời gian hiện tại của video
    private boolean isPlaying = false; // Trạng thái phát video
    private boolean isFullScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        // Ánh xạ các thành phần UI
        trailerPlayerView = findViewById(R.id.trailerPlayerView);
        fullScreenButton = findViewById(R.id.fullScreenButton);
        collapseButton = findViewById(R.id.collapseButton);
        rotateToLandscapeButton = findViewById(R.id.rotateToLandscapeButton);
        rotateToPortraitButton = findViewById(R.id.rotateToPortraitButton);

        // Lấy URL video từ Intent
        String trailerUrl = getIntent().getStringExtra("trailerUrl");
        if (trailerUrl != null) {
            String videoId = extractVideoId(trailerUrl);

            trailerPlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    youTubePlayerInstance = youTubePlayer;
                    youTubePlayer.loadVideo(videoId, currentSecond); // Phát video từ vị trí đã lưu
                }

                @Override
                public void onCurrentSecond(YouTubePlayer youTubePlayer, float second) {
                    currentSecond = second; // Lưu lại thời gian hiện tại
                }

                @Override
                public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                    isPlaying = (state == PlayerConstants.PlayerState.PLAYING); // Lưu trạng thái phát video
                }
            });
        }

        // Xử lý xoay màn hình ngang
        rotateToLandscapeButton.setOnClickListener(v -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE));

        // Xử lý xoay về màn hình dọc
        rotateToPortraitButton.setOnClickListener(v -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT));

        // Xử lý phóng to
        fullScreenButton.setOnClickListener(v -> toggleFullScreen(true));

        // Xử lý thu nhỏ
        collapseButton.setOnClickListener(v -> toggleFullScreen(false));

        // Quản lý vòng đời của YouTubePlayerView
        getLifecycle().addObserver(trailerPlayerView);

        // Thiết lập giao diện ban đầu
        updateButtonVisibility(getResources().getConfiguration().orientation);
    }

    private String extractVideoId(String youtubeUrl) {
        String videoId = "";
        try {
            Uri uri = Uri.parse(youtubeUrl);
            videoId = uri.getQueryParameter("v");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoId;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Cập nhật trạng thái layout khi xoay màn hình
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Chiều cao toàn màn hình khi ở ngang
            trailerPlayerView.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_PARENT;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Chiều cao cố định khi ở dọc
            trailerPlayerView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.youtube_player_height);
        }
        trailerPlayerView.requestLayout(); // Đảm bảo layout được cập nhật

        // Cập nhật trạng thái các nút
        updateButtonVisibility(newConfig.orientation);
    }


    // Phương thức phóng to và thu nhỏ
    private void toggleFullScreen(boolean enterFullScreen) {
        isFullScreen = enterFullScreen;

        if (enterFullScreen) {
            // Phóng to
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
            trailerPlayerView.post(() -> {
                trailerPlayerView.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_PARENT;
                trailerPlayerView.requestLayout();
            });

            fullScreenButton.setVisibility(View.GONE);
            collapseButton.setVisibility(View.VISIBLE);
            rotateToPortraitButton.setVisibility(View.GONE); // Ẩn nút xoay về dọc
        } else {
            // Thu nhỏ (vẫn giữ ngang)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

            trailerPlayerView.post(() -> {
                trailerPlayerView.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_PARENT; // Vẫn giữ nguyên ở màn hình ngang
                trailerPlayerView.requestLayout();
            });

            fullScreenButton.setVisibility(View.VISIBLE); // Hiện nút phóng to
            collapseButton.setVisibility(View.GONE); // Ẩn nút thu nhỏ
            rotateToPortraitButton.setVisibility(View.VISIBLE); // Hiện nút xoay về dọc
        }
    }

    // Phương thức để xử lý trạng thái của các nút
    private void updateButtonVisibility(int orientation) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            rotateToLandscapeButton.setVisibility(View.VISIBLE); // Hiện nút xoay ngang
            rotateToPortraitButton.setVisibility(View.GONE); // Ẩn nút xoay dọc
            fullScreenButton.setVisibility(View.GONE); // Ẩn nút phóng to
            collapseButton.setVisibility(View.GONE); // Ẩn nút thu nhỏ
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rotateToLandscapeButton.setVisibility(View.GONE); // Ẩn nút xoay ngang
            rotateToPortraitButton.setVisibility(isFullScreen ? View.GONE : View.VISIBLE); // Ẩn nếu fullscreen
            fullScreenButton.setVisibility(isFullScreen ? View.GONE : View.VISIBLE); // Hiện phóng to nếu không fullscreen
            collapseButton.setVisibility(isFullScreen ? View.VISIBLE : View.GONE); // Hiện thu nhỏ nếu fullscreen
        }
    }
}