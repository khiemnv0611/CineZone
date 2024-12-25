package com.khiemnv.cinezone.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.model.EpisodeModel;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder> {
    private final Context context;
    private final List<EpisodeModel> episodeList;
    private final OnEpisodeClickListener listener;
    private int selectedPosition = -1; // Vị trí được chọn (-1: chưa có item nào được chọn)

    public interface OnEpisodeClickListener {
        void onEpisodeClick(String videoUrl, int position);
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

            // Đổi viền và màu chữ dựa trên trạng thái selectedPosition
            if (holder.getAdapterPosition() == selectedPosition) {
                holder.itemView.setBackground(createBorderDrawable());
            } else {
                holder.itemView.setBackground(createDefaultDrawable());
            }

            // Đặt sự kiện click vào mỗi item
            holder.itemView.setOnClickListener(v -> {
                int currentPosition = holder.getAdapterPosition();

                if (currentPosition != RecyclerView.NO_POSITION) {
                    EpisodeModel clickedEpisode = episodeList.get(currentPosition);
                    if (listener != null && clickedEpisode.getVideoUrl() != null) {
                        listener.onEpisodeClick(clickedEpisode.getVideoUrl(), currentPosition);
                    } else {
                        Toast.makeText(context, "Video URL not available", Toast.LENGTH_SHORT).show();
                    }

                    // Cập nhật trạng thái của item trước và item hiện tại
                    int previousSelectedPosition = selectedPosition;
                    selectedPosition = currentPosition;

                    // Chỉ cập nhật hai vị trí: vị trí cũ và vị trí mới
                    notifyItemChanged(previousSelectedPosition);
                    notifyItemChanged(selectedPosition);
                }
            });
        }
    }

    // Tạo drawable cho viền khi item được chọn
    private Drawable createBorderDrawable() {
        GradientDrawable borderDrawable = new GradientDrawable();
        borderDrawable.setShape(GradientDrawable.RECTANGLE);
        borderDrawable.setStroke(4, ContextCompat.getColor(context, R.color.mainYellow)); // Chọn độ dày và màu của viền
        return borderDrawable;
    }

    // Tạo drawable cho viền mặc định khi item không được chọn
    private Drawable createDefaultDrawable() {
        GradientDrawable borderDrawable = new GradientDrawable();
        borderDrawable.setShape(GradientDrawable.RECTANGLE);
        borderDrawable.setStroke(4, ContextCompat.getColor(context, R.color.mainDarkBlue)); // Chọn độ dày và màu của viền mặc định
        return borderDrawable;
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

    public void setSelectedPosition(int position) {
        // Cập nhật trạng thái của item trước và item hiện tại
        int previousSelectedPosition = selectedPosition;
        selectedPosition = position;

        // Chỉ cập nhật hai vị trí: vị trí cũ và vị trí mới
        notifyItemChanged(previousSelectedPosition);
        notifyItemChanged(selectedPosition);
    }
}