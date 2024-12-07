package com.khiemnv.cinezone.activity;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khiemnv.cinezone.BaseActivity;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.adapter.EpisodeAdapter;
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

        // Ánh xạ View
        youTubePlayerView = findViewById(R.id.moviePlayerView);
        episodeRecyclerView = findViewById(R.id.episodeRecyclerView);

        // Khởi tạo YouTubePlayerHelper
        youTubePlayerHelper = new YouTubePlayerHelper();

        // Nhận dữ liệu từ Intent
        isSeries = getIntent().getBooleanExtra("isSeries", false);
        videoUrl = getIntent().getStringExtra("videoUrl");
        episodeIds = getIntent().getStringArrayListExtra("episodeIds");

        // Xử lý theo loại phim
        if (!isSeries) {
            // Phim lẻ: Phát trực tiếp videoUrl
            youTubePlayerHelper.playVideo(youTubePlayerView, videoUrl);
        } else if (episodeIds != null && !episodeIds.isEmpty()) {
            // Phim bộ: Hiển thị danh sách tập
            loadEpisodes(episodeIds);
        }
    }

    private void loadEpisodes(List<String> episodeIds) {
        // Lấy dữ liệu tập từ ViewModel
        EpisodeViewModel episodeViewModel = new ViewModelProvider(this).get(EpisodeViewModel.class);
        episodeViewModel.getEpisodesByIds(episodeIds).observe(this, episodes -> {
            if (episodes != null) {
                // Gắn adapter vào RecyclerView
                EpisodeAdapter adapter = new EpisodeAdapter(this, episodes, this);
                episodeRecyclerView.setAdapter(adapter);
                episodeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
        });
    }

    @Override
    public void onEpisodeClick(String videoUrl) {
        youTubePlayerHelper.playVideo(youTubePlayerView, videoUrl); // Khi click vào tập, phát video tương ứng
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
