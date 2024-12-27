package com.khiemnv.cinezone.adapter;

import android.content.Intent;
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
import com.khiemnv.cinezone.utils.TimeAgoUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private final List<HistoryModel> historyList;

    public HistoryAdapter(List<HistoryModel> historyList) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_movie_card, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryModel history = historyList.get(position);

        String movieId = history.getMovieId();

        String watchedAt = formatTimestamp(history.getWatchedAt());

        long watchedAtTimestamp = convertStringToTimestamp(watchedAt);
        String timeAgo = TimeAgoUtils.formatTimestamp(watchedAtTimestamp);

        // Fetch movie details (giả lập với Firebase hoặc một nguồn khác)
        fetchMovieDetails(movieId, holder);

        holder.watchAt.setText(timeAgo);

        // Sự kiện click để mở MovieDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), MovieDetailActivity.class);
            intent.putExtra("movieId", movieId);
            holder.itemView.getContext().startActivity(intent);
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

    // Định dạng timestamp thành chuỗi ngày tháng
    private String formatTimestamp(long timestamp) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return dateFormat.format(new Date(timestamp));
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

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HistoryAdapter", "Error fetching movie: " + error.getMessage());
            }
        });
    }

    // Hàm chuyển đổi String (ISO 8601) sang timestamp (long)
    private long convertStringToTimestamp(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            Date date = sdf.parse(dateString);
            if (date != null) {
                return date.getTime();  // Trả về timestamp (long)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;  // Nếu không thể chuyển đổi, trả về 0 (hoặc giá trị mặc định nào đó)
    }
}
