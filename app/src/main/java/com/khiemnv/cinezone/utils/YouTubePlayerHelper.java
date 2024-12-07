package com.khiemnv.cinezone.utils;

import android.net.Uri;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class YouTubePlayerHelper {
    private YouTubePlayer youTubePlayerInstance;
    private float currentSecond = 0f;
    private boolean isPlaying = false;

    // Phát video từ URL
    public void playVideo(YouTubePlayerView youTubePlayerView, String videoUrl) {
        String videoId = extractVideoId(videoUrl);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayerInstance = youTubePlayer;
                youTubePlayer.loadVideo(videoId, currentSecond); // Phát video từ vị trí hiện tại
            }

            @Override
            public void onCurrentSecond(YouTubePlayer youTubePlayer, float second) {
                currentSecond = second; // Lưu lại thời gian hiện tại của video
            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                isPlaying = (state == PlayerConstants.PlayerState.PLAYING); // Cập nhật trạng thái phát video
            }
        });
    }

    // Dừng video khi ứng dụng không hoạt động
    public void onPause() {
        if (youTubePlayerInstance != null && isPlaying) {
            youTubePlayerInstance.pause(); // Dừng video khi ứng dụng không hoạt động
        }
    }

    // Tiếp tục video khi quay lại ứng dụng
    public void onResume() {
        if (youTubePlayerInstance != null && !isPlaying) {
            youTubePlayerInstance.play(); // Tiếp tục video khi quay lại ứng dụng
        }
    }

    // Phương thức lấy video ID từ URL YouTube
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
}
