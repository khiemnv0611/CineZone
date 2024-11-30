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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.activity.AuthActivity;
import com.khiemnv.cinezone.activity.StartActivity;
import com.khiemnv.cinezone.model.UserModel;

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
        String email = sharedPreferences.getString("user_email", "N/A");

        // Kiểm tra nếu email không phải "N/A", nghĩa là người dùng đã đăng nhập
        if (!email.equals("N/A")) {
            // Lấy thông tin người dùng từ Firebase
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userRef = database.getReference("Users");

            userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Giả sử bạn lưu thông tin người dùng trong từng node với key là userId
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            UserModel user = userSnapshot.getValue(UserModel.class);

                            // Hiển thị thông tin người dùng
                            String fullName = user.getFirstName() + " " + user.getLastName();
                            tvFullName.setText(fullName);
                            tvEmail.setText(user.getEmail());

                            if (!user.getAvatarUrl().isEmpty()) {
                                Glide.with(ProfileFragment.this).load(user.getAvatarUrl()).into(ivAvatar);
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Người dùng chưa đăng nhập, bạn có thể điều hướng họ đến màn hình đăng nhập
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }

        // Xử lý sự kiện đăng xuất
        btnSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("auth_token");
            editor.remove("user_email");
            editor.apply();

            Intent intent = new Intent(getActivity(), AuthActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }
}
