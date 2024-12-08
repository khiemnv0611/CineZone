package com.khiemnv.cinezone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khiemnv.cinezone.model.EpisodeModel;
import com.khiemnv.cinezone.repository.EpisodeRepository;

import java.util.ArrayList;
import java.util.List;

public class EpisodeViewModel extends ViewModel {
    private final EpisodeRepository episodeRepository;

    public EpisodeViewModel() {
        episodeRepository = new EpisodeRepository();
    }

    public LiveData<List<EpisodeModel>> getEpisodesByIds(List<String> episodeIds) {
        if (episodeIds == null || episodeIds.isEmpty()) {
            MutableLiveData<List<EpisodeModel>> emptyData = new MutableLiveData<>();
            emptyData.setValue(new ArrayList<>()); // Trả về danh sách rỗng nếu không có ID
            return emptyData;
        }
        return episodeRepository.getEpisodesByIds(episodeIds);
    }
}
