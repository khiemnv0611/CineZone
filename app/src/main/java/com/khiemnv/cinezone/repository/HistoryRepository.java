package com.khiemnv.cinezone.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khiemnv.cinezone.model.HistoryModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryRepository {
    private final DatabaseReference historyRef;

    public HistoryRepository() {
        historyRef = FirebaseDatabase.getInstance().getReference("WatchHistories");
    }

    // Listener interface để xử lý callback fetch
    public interface OnHistoryFetchListener {
        void onSuccess(List<HistoryModel> historyList);

        void onFailure(Exception e);
    }

    // Fetch user history
    public void getUserHistory(String email, OnHistoryFetchListener listener) {
        historyRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<HistoryModel> historyList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            HistoryModel history = dataSnapshot.getValue(HistoryModel.class);
                            if (history != null) {
                                historyList.add(history);
                            }
                        }
                        listener.onSuccess(historyList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onFailure(error.toException());
                    }
                });
    }

    // Save or update history
    public void saveHistory(HistoryModel history, OnCompleteListener<Void> onCompleteListener) {
        // Kiểm tra xem bộ phim đã có trong lịch sử chưa
        historyRef.orderByChild("email").equalTo(history.getEmail())  // Dùng email để tìm kiếm
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean movieExists = false;

                        // Kiểm tra tất cả các bộ phim trong lịch sử của người dùng
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            HistoryModel existingHistory = dataSnapshot.getValue(HistoryModel.class);
                            if (existingHistory != null && existingHistory.getMovieId().equals(history.getMovieId())) {
                                // Nếu bộ phim đã có trong lịch sử, cập nhật watchedAt
                                movieExists = true;
                                String existingHistoryId = dataSnapshot.getKey(); // Lấy id của lịch sử đã tồn tại
                                historyRef.child(existingHistoryId).child("watchedAt").setValue(System.currentTimeMillis())
                                        .addOnCompleteListener(onCompleteListener);
                                break;
                            }
                        }

                        // Nếu bộ phim chưa có trong lịch sử, lưu bộ phim mới
                        if (!movieExists) {
                            String historyId = history.getHistoryId();
                            historyRef.child(historyId).setValue(history).addOnCompleteListener(onCompleteListener);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        onCompleteListener.onComplete(null); // callback lỗi
                    }
                });
    }
}