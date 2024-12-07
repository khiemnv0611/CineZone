package com.khiemnv.cinezone.repository;

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

        episodesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (String id : episodeIds) {
                    if (snapshot.hasChild(id)) {
                        EpisodeModel episode = snapshot.child(id).getValue(EpisodeModel.class);
                        if (episode != null) {
                            episodes.add(episode);
                        }
                    }
                }
                liveData.setValue(episodes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                liveData.setValue(null);
            }
        });

        return liveData;
    }
}
