package com.khiemnv.cinezone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.khiemnv.cinezone.R;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout fragment_home và trả về View
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Thiết lập top header
        View topHeaderLayout = view.findViewById(R.id.topHeaderLayout);

        // Trả về view sau khi thực hiện các thao tác cần thiết
        return view;
    }
}

