package com.khiemnv.cinezone.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.khiemnv.cinezone.activity.ProfileDetailActivity;
import com.khiemnv.cinezone.model.UserModel;

public class ProfileFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private static final int REQUEST_CODE_EDIT = 101;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView ivAvatar = view.findViewById(R.id.ivAvatar);
        TextView tvFullName = view.findViewById(R.id.tvFullName);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        Button btnSignOut = view.findViewById(R.id.btnSignOut);
        ImageView icEdit = view.findViewById(R.id.icEdit);

        sharedPreferences = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);

        String email = sharedPreferences.getString("user_email", "N/A");
        if (email == null || email.isEmpty() || email.equals("N/A")) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return view;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("Users");

        userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        UserModel user = userSnapshot.getValue(UserModel.class);
                        if (user != null) {
                            String fullName = user.getFirstName() + " " + user.getLastName();
                            tvFullName.setText(fullName);
                            tvEmail.setText(user.getEmail());

                            if (!user.getAvatarUrl().isEmpty()) {
                                Glide.with(ProfileFragment.this).load(user.getAvatarUrl()).into(ivAvatar);
                            }
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProfileFragment", "Firebase error: " + error.getMessage());
                Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });

        icEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileDetailActivity.class);
            startActivityForResult(intent, REQUEST_CODE_EDIT);
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK && data != null) {
            String fullName = data.getStringExtra("fullName");
            if (fullName != null && !fullName.isEmpty()) {
                TextView tvFullName = getView().findViewById(R.id.tvFullName);
                tvFullName.setText(fullName);
            }
        }
    }
}
