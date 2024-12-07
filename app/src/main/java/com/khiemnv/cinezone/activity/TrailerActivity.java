package com.khiemnv.cinezone.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.khiemnv.cinezone.BaseActivity;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.utils.YouTubePlayerHelper;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class TrailerActivity extends BaseActivity {
    private YouTubePlayerView trailerPlayerView;
    private ImageView fullScreenButton, collapseButton;
    private YouTubePlayerHelper youTubePlayerHelper;
    private boolean isFullScreen = false; // Kiểm tra trạng thái full-screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        // Ánh xạ các thành phần UI
        trailerPlayerView = findViewById(R.id.trailerPlayerView);
        fullScreenButton = findViewById(R.id.fullScreenButton);
        collapseButton = findViewById(R.id.collapseButton);

        // Khởi tạo YouTubePlayerHelper
        youTubePlayerHelper = new YouTubePlayerHelper();

        // Lấy URL video từ Intent
        String trailerUrl = getIntent().getStringExtra("trailerUrl");
        if (trailerUrl != null) {
            youTubePlayerHelper.playVideo(trailerPlayerView, trailerUrl);
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
