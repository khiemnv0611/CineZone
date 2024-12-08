package com.khiemnv.cinezone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.model.EpisodeModel;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder> {

    private final Context context;
    private final List<EpisodeModel> episodeList;
    private final OnEpisodeClickListener listener;

    public interface OnEpisodeClickListener {
        void onEpisodeClick(String videoUrl);
    }

    public EpisodeAdapter(Context context, List<EpisodeModel> episodeList, OnEpisodeClickListener listener) {
        this.context = context;
        this.episodeList = episodeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.episode_card, parent, false);
        return new EpisodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        EpisodeModel episode = episodeList.get(position);

        if (episode != null) {
            // Gán các giá trị cho các thành phần trong ViewHolder
            holder.title.setText(episode.getTitle() != null ? episode.getTitle() : "Unknown Title");
            holder.description.setText(episode.getDescription() != null ? episode.getDescription() : "No description available");
            holder.duration.setText(episode.getDuration() != 0 ? String.valueOf(episode.getDuration()) : "Duration not available");

            Glide.with(context)
                    .load(episode.getThumbnailUrl() != null ? episode.getThumbnailUrl() : R.drawable.sample_poster)
                    .into(holder.thumbnail);

            // Đặt sự kiện click vào mỗi item
            holder.itemView.setOnClickListener(v -> {
//                Log.d("EpisodeAdapter", "Item clicked at position: " + position);
                if (listener != null && episode.getVideoUrl() != null) {
                    listener.onEpisodeClick(episode.getVideoUrl());
                } else {
//                    Log.e("EpisodeAdapter", "Error: videoUrl is null for episode at position: " + position);
                    Toast.makeText(context, "Video URL not available", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    public static class EpisodeViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, duration;
        ImageView thumbnail;

        public EpisodeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.episodeTitle);
            description = itemView.findViewById(R.id.episodeDescription);
            duration = itemView.findViewById(R.id.episodeDuration);
            thumbnail = itemView.findViewById(R.id.episodeThumbnail);
        }
    }
}