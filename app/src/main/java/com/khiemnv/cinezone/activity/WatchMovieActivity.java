package com.khiemnv.cinezone.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khiemnv.cinezone.BaseActivity;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.adapter.EpisodeAdapter;
import com.khiemnv.cinezone.model.EpisodeModel;
import com.khiemnv.cinezone.utils.YouTubePlayerHelper;
import com.khiemnv.cinezone.viewmodel.EpisodeViewModel;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

public class WatchMovieActivity extends BaseActivity implements EpisodeAdapter.OnEpisodeClickListener {
    private YouTubePlayerView youTubePlayerView;
    private RecyclerView episodeRecyclerView;
    private boolean isSeries;
    private String videoUrl;
    private List<String> episodeIds;
    private YouTubePlayerHelper youTubePlayerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_movie);

        // Ánh xạ view
        youTubePlayerView = findViewById(R.id.moviePlayerView);
        getLifecycle().addObserver(youTubePlayerView);
        episodeRecyclerView = findViewById(R.id.episodeRecyclerView);

        youTubePlayerHelper = new YouTubePlayerHelper();

        // Nhận dữ liệu từ Intent
        isSeries = getIntent().getBooleanExtra("isSeries", false);
        videoUrl = getIntent().getStringExtra("videoUrl");
        episodeIds = getIntent().getStringArrayListExtra("episodeIds");

        // Kiểm tra videoUrl và episodeIds
        if (videoUrl == null && !isSeries) {
            Toast.makeText(this, "Video URL not available", Toast.LENGTH_SHORT).show();
        }

        if (episodeIds == null || episodeIds.isEmpty()) {
            Toast.makeText(this, "No episodes available", Toast.LENGTH_SHORT).show();
        }

        if (!isSeries) {
            // Nếu không phải series, phát videoUrl của movie
            episodeRecyclerView.setVisibility(View.GONE);
            if (videoUrl != null) {
                youTubePlayerHelper.playVideo(youTubePlayerView, videoUrl);
            }
        } else {
            // Nếu là series, tải danh sách các tập phim từ Firebase
            if (episodeIds != null && !episodeIds.isEmpty()) {
                episodeRecyclerView.setVisibility(View.VISIBLE);
                loadEpisodes(episodeIds);
            } else {
                Log.e("WatchMovieActivity", "No episodes available for series.");
            }
        }
    }

    private void loadEpisodes(List<String> episodeIds) {
        // Kiểm tra nếu không có episodeIds
        if (episodeIds == null || episodeIds.isEmpty()) {
            Toast.makeText(this, "No episodes to load", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo ViewModel và lấy danh sách episodes
        EpisodeViewModel episodeViewModel = new ViewModelProvider(this).get(EpisodeViewModel.class);
        episodeViewModel.getEpisodesByIds(episodeIds).observe(this, episodes -> {
            if (episodes != null && !episodes.isEmpty()) {
                setupEpisodeRecyclerView(episodes);
            } else {
                Toast.makeText(this, "Failed to load episodes", Toast.LENGTH_SHORT).show();
                episodeRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void setupEpisodeRecyclerView(List<EpisodeModel> episodes) {
        if (episodes == null || episodes.isEmpty()) {
            episodeRecyclerView.setVisibility(View.GONE);
            return;
        }

        // Thiết lập RecyclerView và Adapter
        EpisodeAdapter adapter = new EpisodeAdapter(this, episodes, this);
        episodeRecyclerView.setAdapter(adapter);
        episodeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Phát video của tập đầu tiên
        EpisodeModel firstEpisode = episodes.get(0);
        if (firstEpisode != null && firstEpisode.getVideoUrl() != null) {
            youTubePlayerHelper.playVideo(youTubePlayerView, firstEpisode.getVideoUrl());
        } else {
            Log.e("WatchMovieActivity", "Error: videoUrl is null for first episode.");
        }
    }

    @Override
    public void onEpisodeClick(String videoUrl) {
        // Phát video của tập phim vừa được click
        if (videoUrl != null) {
            youTubePlayerHelper.playVideo(youTubePlayerView, videoUrl);
        } else {
            Log.e("WatchMovieActivity", "Error: videoUrl is null for clicked episode.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        youTubePlayerHelper.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        youTubePlayerHelper.onResume();
    }
}