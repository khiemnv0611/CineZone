package com.khiemnv.cinezone.adapter;

import android.content.Context;
import android.content.Intent;
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
        holder.movieRating.setText("⭐ " + movie.getAverageRating());
        // Sử dụng thư viện như Glide hoặc Picasso để tải ảnh từ URL
        Glide.with(context).load(movie.getImageUrl()).into(holder.moviePoster);

        // Thiết lập sự kiện click cho mỗi item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("genre", genreList != null && !genreList.isEmpty() ? genreList.get(0) : "N/A");
            intent.putExtra("averageRating", movie.getAverageRating());
            intent.putExtra("imageUrl", movie.getImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView movieTitle, movieGenre, movieRating;
        ImageView moviePoster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            movieGenre = itemView.findViewById(R.id.movieGenre);
            movieRating = itemView.findViewById(R.id.movieRating);
            moviePoster = itemView.findViewById(R.id.moviePoster);
        }
    }
}