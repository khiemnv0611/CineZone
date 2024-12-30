package com.khiemnv.cinezone.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.activity.MovieDetailActivity;
import com.khiemnv.cinezone.model.HistoryModel;
import com.khiemnv.cinezone.model.MovieModel;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private final List<HistoryModel> historyList;
    private final Context context;

    public HistoryAdapter(Context context, List<HistoryModel> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    public void setHistoryList(List<HistoryModel> newHistoryList) {
        historyList.clear();
        historyList.addAll(newHistoryList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.featured_movie_card, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryModel history = historyList.get(position);
        String movieId = history.getMovieId();
        String timeAgo = getTimeAgo(context, history.getWatchedAt());

        // Fetch movie details
        fetchMovieDetails(movieId, holder);

        holder.watchAt.setText(timeAgo);

        // Sự kiện click để mở MovieDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtra("movieId", movieId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    // ViewHolder
    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView movieThumbnail;
        TextView title, watchAt;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            movieThumbnail = itemView.findViewById(R.id.movie_thumbnail);
            title = itemView.findViewById(R.id.title);
            watchAt = itemView.findViewById(R.id.watchAt);
        }
    }

    // Fetch movie details từ Firebase
    private void fetchMovieDetails(String movieId, HistoryViewHolder holder) {
        DatabaseReference movieRef = FirebaseDatabase.getInstance().getReference("Movies").child(movieId);

        movieRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MovieModel movie = snapshot.getValue(MovieModel.class);
                if (movie != null) {
                    holder.title.setText(movie.getTitle());
                    Glide.with(holder.itemView.getContext())
                            .load(movie.getImageUrl())
                            .placeholder(R.drawable.sample_poster)
                            .into(holder.movieThumbnail);

                    // Sự kiện click mở MovieDetailActivity với thông tin chi tiết
                    holder.itemView.setOnClickListener(v -> {
                        Context context = holder.itemView.getContext();

                        Intent intent = new Intent(context, MovieDetailActivity.class);

                        // Truyền dữ liệu chi tiết qua Intent
                        intent.putExtra("movieId", movie.getMovieId());
                        intent.putExtra("title", movie.getTitle());

                        List<String> genreList = movie.getGenre();
                        intent.putExtra("genre", genreList != null && !genreList.isEmpty() ? TextUtils.join(", ", genreList) : "N/A");

                        intent.putExtra("type", movie.getType());
                        intent.putExtra("ageRating", movie.getAgeRating());
                        intent.putExtra("status", movie.getStatus());
                        intent.putExtra("description", movie.getDescription());
                        intent.putExtra("country", movie.getCountry());
                        intent.putExtra("season", movie.getSeason());
                        intent.putExtra("directors", movie.getDirectors() != null ? String.join(", ", movie.getDirectors()) : "N/A");
                        intent.putExtra("productionCompanies", movie.getProductionCompanies() != null ? String.join(", ", movie.getProductionCompanies()) : "N/A");

                        // Định dạng ngày tháng
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDate = (movie.getReleaseDate() != null) ? dateFormat.format(movie.getReleaseDate()) : "N/A";
                        intent.putExtra("releaseDate", formattedDate);

                        // Định dạng thời gian
                        String formattedDuration;
                        int hours = movie.getDuration() / 60;
                        int minutes = movie.getDuration() % 60;

                        if (hours > 0) {
                            formattedDuration = hours + " " + (hours == 1 ? context.getString(R.string.hour) : context.getString(R.string.hours));
                            if (minutes > 0) {
                                formattedDuration += " " + minutes + " " + (minutes == 1 ? context.getString(R.string.minute) : context.getString(R.string.minutes));
                            }
                        } else {
                            formattedDuration = minutes > 0
                                    ? minutes + " " + (minutes == 1 ? context.getString(R.string.minute) : context.getString(R.string.minutes))
                                    : "0 " + context.getString(R.string.minutes);
                        }
                        intent.putExtra("duration", formattedDuration);

                        // Định dạng số cho viewCount
                        int viewCount = movie.getViewCount();
                        NumberFormat numberFormatViewCount = NumberFormat.getInstance();
                        String formattedViewCount = numberFormatViewCount.format(viewCount);
                        intent.putExtra("viewCount", formattedViewCount);

                        intent.putExtra("averageRating", movie.getAverageRating());

                        // Định dạng số cho totalRatings
                        int totalRatings = movie.getTotalRatings();
                        NumberFormat numberFormatRating = NumberFormat.getInstance();
                        String formattedRatings = numberFormatRating.format(totalRatings);
                        intent.putExtra("totalRatings", formattedRatings);
                        intent.putExtra("imageUrl", movie.getImageUrl());
                        intent.putExtra("videoUrl", movie.getVideoUrl());
                        intent.putExtra("trailerUrl", movie.getTrailerUrl());
                        intent.putExtra("actors", (Serializable) movie.getActors());
                        intent.putExtra("isSeries", movie.getIsSeries());

                        List<String> episodeIds = movie.getEpisodeIds();
                        if (episodeIds == null) {
                            episodeIds = new ArrayList<>();
                        }

                        // Số tập trong series
                        final int episodeCount = episodeIds.size();
                        intent.putExtra("episodeCount", episodeCount);
                        intent.putExtra("totalEpisodes", movie.getTotalEpisodes());

                        if (movie.getEpisodeIds() != null) {
                            intent.putStringArrayListExtra("episodeIds", new ArrayList<>(movie.getEpisodeIds()));
                        }

                        context.startActivity(intent);

                        // Hiệu ứng chuyển trang
                        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HistoryAdapter", "Error fetching movie: " + error.getMessage());
            }
        });
    }

    public static String getTimeAgo(Context context, long timestamp) {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        // Tính toán các đơn vị thời gian
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;
        long months = days / 30;
        long years = days / 365;

        if (seconds < 60) {
            return context.getString(R.string.just_now);
        } else if (minutes < 60) {
            return context.getString(R.string.minutes_ago, minutes);
        } else if (hours < 24) {
            return context.getString(R.string.hours_ago, hours);
        } else if (days < 7) {
            return context.getString(R.string.days_ago, days);
        } else if (weeks < 4) {
            return context.getString(R.string.weeks_ago, weeks);
        } else if (months < 12) {
            return context.getString(R.string.months_ago, months);
        } else {
            return context.getString(R.string.years_ago, years);
        }
    }
}
