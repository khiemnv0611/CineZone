package com.khiemnv.cinezone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.khiemnv.cinezone.model.EpisodeModel;
import com.khiemnv.cinezone.repository.EpisodeRepository;

import java.util.List;

public class EpisodeViewModel extends ViewModel {
    private final EpisodeRepository episodeRepository;

    public EpisodeViewModel() {
        episodeRepository = new EpisodeRepository();
    }

    public LiveData<List<EpisodeModel>> getEpisodesByIds(List<String> episodeIds) {
        return episodeRepository.getEpisodesByIds(episodeIds);
    }
}
