package com.khiemnv.cinezone.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.adapter.HistoryAdapter;
import com.khiemnv.cinezone.model.HistoryModel;
import com.khiemnv.cinezone.viewmodel.HistoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView noHistoryTextView;
    private HistoryAdapter historyAdapter;
    private HistoryViewModel historyViewModel;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Ánh xạ view
        recyclerView = view.findViewById(R.id.recycler_view);
        noHistoryTextView = view.findViewById(R.id.noHistoryTextView); // Sử dụng TextView trong XML

        // Tạo danh sách trống và adapter
        List<HistoryModel> historyList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(historyList);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(historyAdapter);

        // Lấy email từ SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("user_email", null);

        if (email == null || email.isEmpty()) {
            Toast.makeText(getContext(), "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return view;
        }

        // Kết nối ViewModel
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        observeHistoryData();

        // Fetch dữ liệu lịch sử
        historyViewModel.fetchUserHistory(email);

        return view;
    }

    // Lắng nghe thay đổi dữ liệu từ ViewModel
    private void observeHistoryData() {
        historyViewModel.getHistoryLiveData().observe(getViewLifecycleOwner(), historyList -> {
            if (historyList == null || historyList.isEmpty()) {
                // Không có lịch sử
                recyclerView.setVisibility(View.GONE);
                noHistoryTextView.setVisibility(View.VISIBLE); // Hiển thị thông báo
            } else {
                // Có lịch sử, cập nhật adapter
                recyclerView.setVisibility(View.VISIBLE);
                noHistoryTextView.setVisibility(View.GONE); // Ẩn thông báo
                historyAdapter.setHistoryList(historyList);
            }
        });
    }
}