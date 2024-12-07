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
import com.khiemnv.cinezone.model.EpisodeModel;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder> {
    private Context context;
    private List<EpisodeModel> episodeList;

    public EpisodeAdapter(Context context, List<EpisodeModel> episodeList) {
        this.context = context;
        this.episodeList = episodeList;
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
        holder.title.setText(episode.getTitle());
        holder.description.setText(episode.getDescription());
        holder.duration.setText(episode.getDuration());
        Glide.with(context).load(episode.getThumbnailUrl()).into(holder.thumbnail);
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
