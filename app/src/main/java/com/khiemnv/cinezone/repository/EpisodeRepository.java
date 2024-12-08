package com.khiemnv.cinezone.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khiemnv.cinezone.model.EpisodeModel;

import java.util.ArrayList;
import java.util.List;

public class EpisodeRepository {
    private final DatabaseReference episodesRef;

    public EpisodeRepository() {
        episodesRef = FirebaseDatabase.getInstance().getReference("Episodes");
    }

    public LiveData<List<EpisodeModel>> getEpisodesByIds(List<String> episodeIds) {
        MutableLiveData<List<EpisodeModel>> liveData = new MutableLiveData<>();
        List<EpisodeModel> episodes = new ArrayList<>();

        if (episodeIds == null || episodeIds.isEmpty()) {
            liveData.setValue(episodes); // Trả về danh sách rỗng nếu không có ID
            return liveData;
        }

        for (String id : episodeIds) {
            episodesRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        EpisodeModel episode = snapshot.getValue(EpisodeModel.class);
                        if (episode != null) {
                            episodes.add(episode);
                        }
                    }

                    // Cập nhật LiveData khi tất cả các ID đã được xử lý
                    if (episodes.size() == episodeIds.size()) {
                        liveData.setValue(episodes);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    liveData.setValue(null); // Trả về null nếu có lỗi
                }
            });
        }

        return liveData;
    }
}
