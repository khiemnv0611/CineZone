package com.khiemnv.cinezone.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khiemnv.cinezone.R;

public class ForgotPasswordFragment extends Fragment {
    private EditText etEmail;
    private Button btnSendVerification;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout cho fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        // Lấy TextView và ImageView
        TextView returnToLoginText = view.findViewById(R.id.tvReturnToLogin);
        ImageView returnToLoginArrow = view.findViewById(R.id.ivArrow);

        // Trở về Đăng nhập
        View.OnClickListener returnToLoginListener = v -> {
            navigateToSignIn();
        };

        // Gán sự kiện cho TextView và ImageView
        returnToLoginText.setOnClickListener(returnToLoginListener);
        returnToLoginArrow.setOnClickListener(returnToLoginListener);

        etEmail = view.findViewById(R.id.etEmail);
        btnSendVerification = view.findViewById(R.id.btnSendVerification);

        btnSendVerification.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim().toLowerCase();

            if (email.isEmpty()) {
                Toast.makeText(getContext(), "Please enter your email address.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Invalid email format!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra email trong Realtime Database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
            userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Email tồn tại trong database, gửi email reset mật khẩu
                        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Password reset email sent. Please check your inbox.", Toast.LENGTH_SHORT).show();
                                        navigateToSignIn();
                                    } else {
                                        Toast.makeText(getContext(), "Failed to send reset email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // Nếu email không tồn tại trong database
                        Toast.makeText(getContext(), "Email not found. Please check your email or register.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Error checking email: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }

    public void navigateToSignIn() {
        // Quay lại SignInFragment
        Fragment fragment = new SignInFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        // Áp dụng hiệu ứng chuyển đổi
        transaction.setCustomAnimations(
                R.anim.slide_in_right, // Animation vào của fragment mới
                R.anim.slide_out_left, // Animation ra của fragment hiện tại
                R.anim.slide_in_left, // Animation quay lại vào của fragment cũ (khi nhấn Back)
                R.anim.slide_out_right // Animation quay lại ra của fragment mới
        );

        transaction.replace(R.id.auth_container, fragment);
        transaction.addToBackStack(null); // Cho phép quay lại fragment cũ
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Xử lý sự kiện nhấn nút Back
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Quay lại fragment trước đó
                FragmentManager fragmentManager = getParentFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                } else {
                    // Nếu không có fragment nào để quay lại, gọi super để thoát activity
                    requireActivity().finish();
                }
            }
        });
    }
}

