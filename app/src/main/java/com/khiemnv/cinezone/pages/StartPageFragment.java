package com.khiemnv.cinezone.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.khiemnv.cinezone.MainActivity;
import com.khiemnv.cinezone.R;

public class StartPageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        // Nút Start
        Button buttonLogin = view.findViewById(R.id.start);
        buttonLogin.setOnClickListener(v -> {
            // Gọi phương thức trong MainActivity để chuyển sang LoginFragment
            ((MainActivity) requireActivity()).showLoginFragment();
        });

        return view;
    }
}
