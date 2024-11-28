package com.khiemnv.cinezone.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.activity.AuthActivity;
import com.khiemnv.cinezone.activity.StartActivity;

public class ProfileFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView ivAvatar = view.findViewById(R.id.ivAvatar);
        TextView tvFullName = view.findViewById(R.id.tvFullName);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        Button btnSignOut = view.findViewById(R.id.btnSignOut);

        sharedPreferences = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);

        // Lấy thông tin người dùng từ SharedPreferences
        String fullName = sharedPreferences.getString("fullName", "N/A");
        String email = sharedPreferences.getString("email", "N/A");
        String avatarUrl = sharedPreferences.getString("avatarUrl", "");

        tvFullName.setText(fullName);
        tvEmail.setText(email);

        if (!avatarUrl.isEmpty()) {
            Glide.with(this).load(avatarUrl).into(ivAvatar);
        }

        // Xử lý đăng xuất
        btnSignOut.setOnClickListener(v -> {
            sharedPreferences.edit().clear().apply();
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
        
        return view;
    }
}
