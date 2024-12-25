package com.khiemnv.cinezone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khiemnv.cinezone.BaseActivity;
import com.khiemnv.cinezone.MainActivity;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.adapter.EpisodeAdapter;
import com.khiemnv.cinezone.model.EpisodeModel;
import com.khiemnv.cinezone.utils.YouTubePlayerHelper;
import com.khiemnv.cinezone.viewmodel.EpisodeViewModel;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

public class WatchMovieActivity extends BaseActivity implements EpisodeAdapter.OnEpisodeClickListener {
    private YouTubePlayerView youTubePlayerView;
    private boolean isSeries;
    private String videoUrl, title;
    private List<String> episodeIds;
    private YouTubePlayerHelper youTubePlayerHelper;
    private TextView tvTitle, switchLabel;
    private ImageView logo;
    private ImageButton backButton;

    private RecyclerView episodeRecyclerView;
    private GridLayout episodeGridLayout;
    private Switch switchCollapse;
    private EpisodeAdapter episodeAdapter;
    private List<EpisodeModel> episodeList;
    private int selectedEpisodePosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_movie);

        // Ánh xạ view
        youTubePlayerView = findViewById(R.id.moviePlayerView);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerHelper = new YouTubePlayerHelper();
        episodeRecyclerView = findViewById(R.id.episodeRecyclerView);
        episodeGridLayout = findViewById(R.id.episodeGridLayout);
        switchLabel = findViewById(R.id.switchLabel);
        switchCollapse = findViewById(R.id.switchCollapse);
        tvTitle = findViewById(R.id.tvTitle);
        logo = findViewById(R.id.logo);
        backButton = findViewById(R.id.back_button);

        // Nhận dữ liệu từ Intent
        isSeries = getIntent().getBooleanExtra("isSeries", false);
        videoUrl = getIntent().getStringExtra("videoUrl");
        episodeIds = getIntent().getStringArrayListExtra("episodeIds");
        title = getIntent().getStringExtra("title");

        if (title != null) {
            tvTitle.setText(title);
        }

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Điều hướng về HomeFragment
                navigateToHomeFragment();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Xử lý Switch chuyển đổi
        switchCollapse.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                episodeRecyclerView.setVisibility(View.GONE);
                episodeGridLayout.setVisibility(View.VISIBLE);
                setupEpisodeGridLayout();
            } else {
                episodeGridLayout.setVisibility(View.GONE);
                episodeRecyclerView.setVisibility(View.VISIBLE);
                episodeAdapter.setSelectedPosition(selectedEpisodePosition);
                episodeAdapter.notifyDataSetChanged();
            }
        });

        // Kiểm tra videoUrl và episodeIds
        if (videoUrl == null && !isSeries) {
            Toast.makeText(this, "Video URL not available", Toast.LENGTH_SHORT).show();
        }

        if (!isSeries) {
            // Nếu là phim lẻ
            episodeRecyclerView.setVisibility(View.GONE);
            switchLabel.setVisibility(View.GONE);
            switchCollapse.setVisibility(View.GONE);

            if (videoUrl != null) {
                youTubePlayerHelper.playVideo(youTubePlayerView, videoUrl);
            }
        } else {
            // Nếu là series
            if (episodeIds != null && !episodeIds.isEmpty()) {
                loadEpisodes(episodeIds);
            } else {
                Log.e("WatchMovieActivity", "No episodes available for series.");
            }
        }

        if (selectedEpisodePosition != -1) {
            setupEpisodeGridLayout();
        }
    }

    private void loadEpisodes(List<String> episodeIds) {
        if (episodeIds == null || episodeIds.isEmpty()) {
            Toast.makeText(this, "No episodes to load", Toast.LENGTH_SHORT).show();
            return;
        }

        EpisodeViewModel episodeViewModel = new ViewModelProvider(this).get(EpisodeViewModel.class);
        episodeViewModel.getEpisodesByIds(episodeIds).observe(this, episodes -> {
            if (episodes != null && !episodes.isEmpty()) {
                episodeList = episodes;

                // Đặt trạng thái tập đầu tiên là selected
                selectedEpisodePosition = 0;

                setupEpisodeRecyclerView(episodes);
                setupEpisodeGridLayout();
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
        episodeAdapter = new EpisodeAdapter(this, episodes, this);  // Khởi tạo episodeAdapter tại đây

        // Chọn tập đầu tiên
        episodeAdapter.setSelectedPosition(0); // Đặt tập đầu tiên là active
        episodeRecyclerView.setAdapter(episodeAdapter);
        episodeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Phát video của tập đầu tiên
        EpisodeModel firstEpisode = episodes.get(0);
        if (firstEpisode != null && firstEpisode.getVideoUrl() != null) {
            youTubePlayerHelper.playVideo(youTubePlayerView, firstEpisode.getVideoUrl());
        } else {
            Log.e("WatchMovieActivity", "Error: videoUrl is null for first episode.");
        }
    }

    private void setupEpisodeGridLayout() {
        if (episodeList == null || episodeList.isEmpty()) {
            Toast.makeText(this, "No episodes to display", Toast.LENGTH_SHORT).show();
            return;
        }

        GridLayout episodeGridLayout = findViewById(R.id.episodeGridLayout);
        episodeGridLayout.removeAllViews(); // Clear các nút cũ

        for (int i = 0; i < episodeList.size(); i++) {
            EpisodeModel episode = episodeList.get(i);

            Button button = new Button(this);
            button.setText("Tập " + (i + 1));
            button.setTag(i);

            // Thêm bo tròn cho nút
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.episode_button_background));
            button.setTextColor(ContextCompat.getColor(this, R.color.white));

            // Đặt trạng thái active cho nút
            if (i == selectedEpisodePosition) {
                button.setBackground(ContextCompat.getDrawable(this, R.drawable.episode_button_active));
                button.setTextColor(ContextCompat.getColor(this, R.color.black));
            }

            // Thiết lập LayoutParams cho GridLayout
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 200; // Chiều rộng cố định cho nút
            params.height = GridLayout.LayoutParams.WRAP_CONTENT; // Chiều cao tự động
            params.setMargins(16, 16, 16, 16); // Khoảng cách giữa các nút

            button.setLayoutParams(params);

            // Xử lý sự kiện click
            button.setOnClickListener(v -> {
                selectedEpisodePosition = (int) v.getTag();

                EpisodeModel selectedEpisode = episodeList.get(selectedEpisodePosition);
                if (selectedEpisode != null && selectedEpisode.getVideoUrl() != null) {
                    youTubePlayerHelper.playVideo(youTubePlayerView, selectedEpisode.getVideoUrl());
                }

                // Cập nhật trạng thái GridLayout
                setupEpisodeGridLayout();

                // Cập nhật trạng thái RecyclerView
                episodeAdapter.setSelectedPosition(selectedEpisodePosition);
                episodeAdapter.notifyDataSetChanged();
            });

            episodeGridLayout.addView(button);
        }
    }

    private void navigateToHomeFragment() {
        // Lấy MainActivity từ Intent
        Intent intent = new Intent(WatchMovieActivity.this, MainActivity.class);
        intent.putExtra("navigate_to_home", true); // Thêm dữ liệu để MainActivity biết rằng cần chuyển sang HomeFragment
        startActivity(intent);

        finish(); // Đóng MovieDetailActivity
    }

    @Override
    public void onEpisodeClick(String videoUrl, int position) {
        // Cập nhật vị trí tập được chọn
        selectedEpisodePosition = position;

        // Phát video
        if (videoUrl != null) {
            youTubePlayerHelper.playVideo(youTubePlayerView, videoUrl);
        } else {
            Log.e("WatchMovieActivity", "Error: videoUrl is null for clicked episode.");
        }

        setupEpisodeGridLayout();

        // Cập nhật RecyclerView nếu episodeAdapter không phải là null
        if (episodeAdapter != null) {
            // Cập nhật vị trí được chọn trong adapter
            episodeAdapter.setSelectedPosition(selectedEpisodePosition);
            // Thông báo với adapter rằng cần làm mới RecyclerView
            episodeAdapter.notifyDataSetChanged();
        } else {
            Log.e("WatchMovieActivity", "episodeAdapter is null!");
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