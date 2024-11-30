package com.khiemnv.cinezone.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khiemnv.cinezone.BaseActivity;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.model.UserModel;

public class ProfileDetailActivity extends BaseActivity {
    private EditText etFirstName, etLastName, etEmail;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        // Ánh xạ view
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
//        btnSave = findViewById(R.id.btnSave);

        // Lấy thông tin từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("user_email", "N/A");

        // Lấy thông tin người dùng từ Firebase
        if (!email.equals("N/A")) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userRef = database.getReference("Users");

            userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            UserModel user = userSnapshot.getValue(UserModel.class);
                            if (user != null) {
                                etFirstName.setText(user.getFirstName());
                                etLastName.setText(user.getLastName());
                                etEmail.setText(user.getEmail());
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ProfileDetailActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Lưu thông tin cập nhật
//        btnSave.setOnClickListener(v -> {
//            String firstName = etFirstName.getText().toString();
//            String lastName = etLastName.getText().toString();
//
//            if (!firstName.isEmpty() && !lastName.isEmpty()) {
//                userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
//                                userSnapshot.getRef().child("firstName").setValue(firstName);
//                                userSnapshot.getRef().child("lastName").setValue(lastName);
//                                Toast.makeText(EditActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(EditActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } else {
//                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}

