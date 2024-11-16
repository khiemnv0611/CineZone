package com.khiemnv.cinezone.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.khiemnv.cinezone.BaseActivity;
import com.khiemnv.cinezone.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class TrailerActivity extends BaseActivity {
    private YouTubePlayerView trailerPlayerView;
    private ImageView fullScreenButton, collapseButton;
    private YouTubePlayer youTubePlayerInstance;
    private float currentSecond = 0f; // Lưu thời gian hiện tại của video
    private boolean isPlaying = false; // Trạng thái phát video
    private boolean isFullScreen = false; // Kiểm tra trạng thái full-screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        // Ánh xạ các thành phần UI
        trailerPlayerView = findViewById(R.id.trailerPlayerView);
        fullScreenButton = findViewById(R.id.fullScreenButton);
        collapseButton = findViewById(R.id.collapseButton);

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

        // Xử lý phóng to
        fullScreenButton.setOnClickListener(v -> toggleFullScreen(true));

        // Xử lý thu nhỏ
        collapseButton.setOnClickListener(v -> toggleFullScreen(false));

        // Quản lý vòng đời của YouTubePlayerView
        getLifecycle().addObserver(trailerPlayerView);

        // Thiết lập giao diện ban đầu
        updateButtonVisibility(getResources().getConfiguration().orientation);

        // Nếu là chế độ ngang ngay từ đầu, gọi toggleFullScreen(true)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            toggleFullScreen(true);
        }
    }

    // Phương thức để lấy video ID từ URL YouTube
    private String extractVideoId(String youtubeUrl) {
        String videoId = "";
        try {
            Uri uri = Uri.parse(youtubeUrl);
            if (uri.getHost().contains("youtube.com")) {
                videoId = uri.getQueryParameter("v");
            } else if (uri.getHost().contains("youtu.be")) {
                videoId = uri.getLastPathSegment();
            }
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
            trailerPlayerView.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_PARENT;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            trailerPlayerView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.youtube_player_height);
        }
        trailerPlayerView.requestLayout(); // Đảm bảo layout được cập nhật ngay lập tức

        // Cập nhật trạng thái các nút (nếu có thay đổi)
        updateButtonVisibility(newConfig.orientation);

        // Nếu đang ở chế độ ngang và người dùng không phóng to thì phải tự động phóng to
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && !isFullScreen) {
            toggleFullScreen(true); // Tự động phóng to nếu ở chế độ ngang và không phải fullscreen
        }
    }

    // Phương thức phóng to và thu nhỏ
    private void toggleFullScreen(boolean enterFullScreen) {
        isFullScreen = enterFullScreen;

        if (enterFullScreen) {
            // Phóng to video (toàn màn hình ngang hoặc dọc)
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );

            trailerPlayerView.post(() -> {
                // Phóng to video cho dù là dọc hay ngang
                trailerPlayerView.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_PARENT;
                trailerPlayerView.requestLayout();
            });

            fullScreenButton.setVisibility(View.GONE);
            collapseButton.setVisibility(View.VISIBLE);
        } else {
            // Thu nhỏ video về trạng thái bình thường
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

            trailerPlayerView.post(() -> {
                trailerPlayerView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.youtube_player_height);
                trailerPlayerView.requestLayout();
            });

            fullScreenButton.setVisibility(View.VISIBLE); // Hiện nút phóng to
            collapseButton.setVisibility(View.GONE); // Ẩn nút thu nhỏ
        }
    }

    // Phương thức để xử lý trạng thái của các nút
    private void updateButtonVisibility(int orientation) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            fullScreenButton.setVisibility(View.VISIBLE); // Hiện nút phóng to
            collapseButton.setVisibility(View.GONE); // Ẩn nút thu nhỏ
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fullScreenButton.setVisibility(View.GONE); // Ẩn nút phóng to
            collapseButton.setVisibility(View.VISIBLE); // Hiện nút thu nhỏ
        }
    }

    @Override
    public void onBackPressed() {
        if (isFullScreen) {
            // Nếu đang ở chế độ full-screen, thu nhỏ video trước khi thoát
            toggleFullScreen(false); // Thu nhỏ về trạng thái bình thường
        } else {
            // Nếu không ở chế độ full-screen, thực hiện hành động back bình thường
            super.onBackPressed();
        }
    }

    // Quản lý video khi ứng dụng không hoạt động
    @Override
    protected void onPause() {
        super.onPause();
        if (youTubePlayerInstance != null && isPlaying) {
            youTubePlayerInstance.pause(); // Dừng video khi ứng dụng không hoạt động
        }
    }

    // Quản lý video khi quay lại ứng dụng
    @Override
    protected void onResume() {
        super.onResume();
        if (youTubePlayerInstance != null && !isPlaying) {
            youTubePlayerInstance.play(); // Tiếp tục video khi quay lại ứng dụng
        }
    }
}
