package com.khiemnv.cinezone.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khiemnv.cinezone.model.HistoryModel;
import com.khiemnv.cinezone.repository.HistoryRepository;

import java.util.List;
import java.util.UUID;

public class HistoryViewModel extends ViewModel {
    private final HistoryRepository repository;
    private final MutableLiveData<List<HistoryModel>> historyLiveData;

    // Constructor
    public HistoryViewModel() {
        repository = new HistoryRepository();
        historyLiveData = new MutableLiveData<>();
    }

    // Getter cho LiveData
    public LiveData<List<HistoryModel>> getHistoryLiveData() {
        return historyLiveData;
    }

    // Fetch user history qua Repository
    public void fetchUserHistory(String email) {
        repository.getUserHistory(email, new HistoryRepository.OnHistoryFetchListener() {
            @Override
            public void onSuccess(List<HistoryModel> historyList) {
                historyLiveData.postValue(historyList);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("HistoryViewModel", "Failed to fetch history: " + e.getMessage());
            }
        });
    }

    // Save history qua Repository
    public void saveHistory(String email, String movieId) {
        // Tạo dữ liệu lịch sử
        String historyId = UUID.randomUUID().toString();
        long watchedAt = System.currentTimeMillis();

        HistoryModel history = new HistoryModel(historyId, email, movieId, watchedAt);

        // Gửi yêu cầu lưu lịch sử
        repository.saveHistory(history, task -> {
            if (task.isSuccessful()) {
                Log.d("HistoryViewModel", "Lưu lịch sử thành công");
            } else {
                Log.e("HistoryViewModel", "Lưu lịch sử thất bại", task.getException());
            }
        });
    }
}
