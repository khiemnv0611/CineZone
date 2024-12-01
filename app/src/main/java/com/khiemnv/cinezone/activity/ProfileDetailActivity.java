package com.khiemnv.cinezone.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khiemnv.cinezone.BaseActivity;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.fragment.ProfileFragment;
import com.khiemnv.cinezone.model.UserModel;

public class ProfileDetailActivity extends BaseActivity {
    private EditText etURLAvatar, etFirstName, etLastName, etFullName, etEmail;
    private ShapeableImageView ivAvatar;
    private Button btnCancel, btnUpdate;
    private ImageButton btnRotateName;

    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        // Ánh xạ view
        etURLAvatar = findViewById(R.id.etUrlAvatar);
        ivAvatar = findViewById(R.id.ivAvatar);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        btnCancel = findViewById(R.id.btnCancel);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnRotateName = findViewById(R.id.btnRotateName);

        // Lấy thông tin từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("user_email", "N/A");

        // Lấy thông tin người dùng từ Firebase
        if (!email.equals("N/A")) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            userRef = database.getReference("Users");

            userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            UserModel user = userSnapshot.getValue(UserModel.class);
                            if (user != null) {
                                // Gán giá trị cho các trường
                                etFirstName.setText(user.getFirstName());
                                etLastName.setText(user.getLastName());
                                etFullName.setText(user.getFirstName() + " " + user.getLastName());
                                etEmail.setText(user.getEmail());
                                etURLAvatar.setText(user.getAvatarUrl());

                                // Tải ảnh từ URL vào ivAvatar
                                Glide.with(ProfileDetailActivity.this)
                                        .load(user.getAvatarUrl())
                                        .circleCrop()  // Cắt ảnh thành hình tròn (theo kiểu của bạn)
                                        .into(ivAvatar);
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

        // Lắng nghe sự thay đổi URL trong etURLAvatar và tự động cập nhật ảnh
        etURLAvatar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Không cần xử lý gì ở đây
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String url = charSequence.toString();
                if (!url.isEmpty()) {
                    // Tải ảnh từ URL vào ivAvatar (sử dụng Glide)
                    Glide.with(ProfileDetailActivity.this)
                            .load(url)
                            .circleCrop()  // Cắt ảnh thành hình tròn (theo kiểu của bạn)
                            .into(ivAvatar);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Không cần xử lý gì ở đây
            }
        });

        // Xử lý nút xoay tên
        btnRotateName.setOnClickListener(v -> {
            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();

            // Kiểm tra xem fullName đang hiển thị ở dạng nào
            String currentFullName = etFullName.getText().toString();
            if (currentFullName.equals(firstName + " " + lastName)) {
                // Đổi sang dạng LastName FirstName
                etFullName.setText(lastName + " " + firstName);
            } else {
                // Đổi ngược lại sang FirstName LastName
                etFullName.setText(firstName + " " + lastName);
            }
        });

        // Xử lý nút Cancel
        btnCancel.setOnClickListener(v -> {
            finish(); // Trở về trang trước đó (ProfileFragment)
        });

        // Xử lý nút Update
        btnUpdate.setOnClickListener(v -> {
            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            String avatarUrl = etURLAvatar.getText().toString();

            if (!firstName.isEmpty() && !lastName.isEmpty() && !avatarUrl.isEmpty()) {
                userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                userSnapshot.getRef().child("firstName").setValue(firstName);
                                userSnapshot.getRef().child("lastName").setValue(lastName);
                                userSnapshot.getRef().child("avatarUrl").setValue(avatarUrl);

                                // Gửi dữ liệu etFullName về ProfileFragment qua Intent
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("fullName", etFullName.getText().toString());
                                setResult(RESULT_OK, resultIntent);
                                Toast.makeText(ProfileDetailActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProfileDetailActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}