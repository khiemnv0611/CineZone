package com.khiemnv.cinezone.utils;

import android.content.res.Configuration;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;

import com.khiemnv.cinezone.R;

public class FullScreenHelper {

    private boolean isFullScreen = false;
    private Activity activity;
    private ImageView fullScreenButton, collapseButton;
    private View playerView;

    public FullScreenHelper(Activity activity, View playerView, ImageView fullScreenButton, ImageView collapseButton) {
        this.activity = activity;
        this.playerView = playerView;
        this.fullScreenButton = fullScreenButton;
        this.collapseButton = collapseButton;
    }

    // Phương thức phóng to và thu nhỏ
    public void toggleFullScreen(boolean enterFullScreen) {
        isFullScreen = enterFullScreen;

        if (enterFullScreen) {
            // Phóng to video (toàn màn hình ngang hoặc dọc)
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );

            playerView.post(() -> {
                playerView.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_PARENT;
                playerView.requestLayout();
            });

            fullScreenButton.setVisibility(View.GONE);
            collapseButton.setVisibility(View.VISIBLE);
        } else {
            // Thu nhỏ video về trạng thái bình thường
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

            playerView.post(() -> {
                playerView.getLayoutParams().height = (int) activity.getResources().getDimension(R.dimen.youtube_player_height);
                playerView.requestLayout();
            });

            fullScreenButton.setVisibility(View.VISIBLE);
            collapseButton.setVisibility(View.GONE);
        }
    }

    // Phương thức để xử lý trạng thái của các nút
    public void updateButtonVisibility(int orientation) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            fullScreenButton.setVisibility(View.VISIBLE); // Hiện nút phóng to
            collapseButton.setVisibility(View.GONE); // Ẩn nút thu nhỏ
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fullScreenButton.setVisibility(View.GONE); // Ẩn nút phóng to
            collapseButton.setVisibility(View.VISIBLE); // Hiện nút thu nhỏ
        }
    }

    // Phương thức để xử lý khi thay đổi cấu hình
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            playerView.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_PARENT;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            playerView.getLayoutParams().height = (int) activity.getResources().getDimension(R.dimen.youtube_player_height);
        }
        playerView.requestLayout();

        updateButtonVisibility(newConfig.orientation);

        // Nếu đang ở chế độ ngang và không phải fullscreen, tự động phóng to
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && !isFullScreen) {
            toggleFullScreen(true);
        }
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }
}
