package com.khiemnv.cinezone.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.activity.MovieDetailActivity;
import com.khiemnv.cinezone.model.MovieModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private Context context;
    private List<MovieModel> movieList;

    public MovieAdapter(Context context) {
        this.context = context;
        this.movieList = new ArrayList<>();
    }

    public void setMovieList(List<MovieModel> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_card, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieModel movie = movieList.get(position);
        holder.movieTitle.setText(movie.getTitle());
        List<String> genreList = movie.getGenre();
        if (genreList != null && !genreList.isEmpty()) {
            holder.movieGenre.setText(genreList.get(0));  // Hiển thị phần tử đầu tiên
        } else {
            holder.movieGenre.setText("N/A");
        }
        holder.movieAverageRating.setText("⭐ " + movie.getAverageRating());
        // Sử dụng thư viện như Glide hoặc Picasso để tải ảnh từ URL
        Glide.with(context).load(movie.getImageUrl()).into(holder.moviePoster);

        // Định dạng ngày tháng
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Tính giờ và phút
        int hours = movie.getDuration() / 60;
        int minutes = movie.getDuration() % 60;

        // Thiết lập sự kiện click cho mỗi item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("genre", genreList != null && !genreList.isEmpty() ? TextUtils.join(", ", genreList) : "N/A");
            intent.putExtra("type", movie.getType());
            intent.putExtra("ageRating", movie.getAgeRating());
            intent.putExtra("status", movie.getStatus());
            intent.putExtra("description", movie.getDescription());
            intent.putExtra("country", movie.getCountry());
            intent.putExtra("productionCompanies", movie.getProductionCompanies() != null ? String.join(", ", movie.getProductionCompanies()) : "N/A");

            // Định dạng ngày tháng
            String formattedDate = (movie.getReleaseDate() != null) ? dateFormat.format(movie.getReleaseDate()) : "N/A";
            intent.putExtra("releaseDate", formattedDate);

            // Định dạng thời gian
            String formattedDuration;
            if (hours > 0) {
                formattedDuration = hours + " " + context.getString(R.string.hours);
                if (minutes > 0) {
                    formattedDuration += " " + minutes + " " + context.getString(R.string.minutes);
                }
            } else {
                if (minutes > 0) {
                    formattedDuration = minutes + " " + context.getString(R.string.minutes);
                } else {
                    formattedDuration = "0 " + context.getString(R.string.minutes);
                }
            }
            intent.putExtra("duration", formattedDuration);

            intent.putExtra("viewCount", movie.getViewCount());
            intent.putExtra("averageRating", movie.getAverageRating());

            // Định dạng số
            int totalRatings = movie.getTotalRatings();
            NumberFormat numberFormat = NumberFormat.getInstance();
            String formattedRatings = numberFormat.format(totalRatings);
            intent.putExtra("totalRatings", formattedRatings);

            intent.putExtra("imageUrl", movie.getImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView movieTitle, movieGenre, movieAverageRating;
        ImageView moviePoster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            movieGenre = itemView.findViewById(R.id.movieGenre);
            movieAverageRating = itemView.findViewById(R.id.movieAverageRating);
            moviePoster = itemView.findViewById(R.id.moviePoster);
        }
    }
}