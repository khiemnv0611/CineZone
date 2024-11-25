package com.khiemnv.cinezone.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.khiemnv.cinezone.MainActivity;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.model.UserModel;
import com.khiemnv.cinezone.viewmodel.UserViewModel;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import org.mindrot.jbcrypt.BCrypt;

public class SignInFragment extends Fragment {
    private UserViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout cho fragment
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        // Quên mật khẩu
        TextView loginText = view.findViewById(R.id.login_text);
        TextView forgotPasswordText = view.findViewById(R.id.forgotPasswordText);

        // Lấy nội dung chuỗi từ resources
        String text = getString(R.string.signup_prompt);

        // Sử dụng SpannableString để thay đổi màu và tạo sự kiện nhấn
        SpannableString spannable = new SpannableString(text);

        // Sử dụng ContextCompat.getColor() để lấy màu
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.mainColor));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // Chuyển sang fragment_signup khi nhấn
                Fragment fragment = new SignUpFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                // Áp dụng hiệu ứng chuyển đổi
                transaction.setCustomAnimations(
                        R.anim.slide_in_right, // Animation vào của fragment mới
                        R.anim.slide_out_left, // Animation ra của fragment hiện tại
                        R.anim.slide_in_left, // Animation quay lại vào của fragment cũ (khi nhấn Back)
                        R.anim.slide_out_right // Animation quay lại ra của fragment mới
                );

                transaction.replace(R.id.auth_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(requireContext(), R.color.mainColor)); // Đảm bảo màu vẫn hiển thị khi nhấn
                ds.setUnderlineText(false); // Gạch chân
                ds.setFakeBoldText(true); // In đậm
            }
        };

        // Xác định vị trí của từ "Sign up" hoặc "Đăng ký" dựa trên chuỗi hiện tại
        int start, end;
        if (text.contains("Sign up")) {
            start = text.indexOf("Sign up");
            end = start + "Sign up".length();
        } else if (text.contains("Đăng ký")) {
            start = text.indexOf("Đăng ký");
            end = start + "Đăng ký".length();
        } else {
            // Đề phòng trường hợp không tìm thấy chuỗi
            start = 0;
            end = 0;
        }

        // Áp dụng màu và tính nhấn cho phần "Sign up" hoặc "Đăng ký"
        spannable.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Đặt SpannableString vào TextView và kích hoạt khả năng nhấn
        loginText.setText(spannable);
        loginText.setMovementMethod(LinkMovementMethod.getInstance());

        // Xử lý sự kiện nhấn cho TextView "Quên mật khẩu"
        forgotPasswordText.setOnClickListener(v -> {
            // Chuyển sang ForgotPasswordFragment
            Fragment fragment = new ForgotPasswordFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

            // Áp dụng hiệu ứng chuyển đổi
            transaction.setCustomAnimations(
                    R.anim.slide_in_left, // Animation vào của fragment mới
                    R.anim.slide_out_right, // Animation ra của fragment hiện tại
                    R.anim.slide_in_right, // Animation quay lại vào của fragment cũ (khi nhấn Back)
                    R.anim.slide_out_left // Animation quay lại ra của fragment mới
            );

            transaction.replace(R.id.auth_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etPassword = view.findViewById(R.id.etPassword);

        MaterialButton btnSignIn = view.findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim().toLowerCase(); // Normalize email
            String password = etPassword.getText().toString();

            // Kiểm tra đầu vào
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Email and password are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Invalid email format!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Hash mật khẩu
            String hashedPassword = viewModel.hashPassword(password);

            // Đăng nhập user
            viewModel.loginUser(email, password, task -> {
                if (task.isSuccessful()) {
                    if (task.getResult() != null && task.getResult().exists()) {
                        boolean isPasswordCorrect = false;

                        for (DataSnapshot snapshot : task.getResult().getChildren()) {
                            UserModel user = snapshot.getValue(UserModel.class);
                            if (user != null) {
                                boolean isPasswordValid = BCrypt.checkpw(password, user.getPassword());

                                if (isPasswordValid) {
                                    isPasswordCorrect = true;

                                    Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_LONG).show();

                                    // Điều hướng tới MainActivity
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    intent.putExtra("isAdmin", user.isAdmin());
                                    startActivity(intent);
                                    if (getActivity() != null) getActivity().finish();
                                    return;
                                }
                            }
                        }

                        if (!isPasswordCorrect) {
                            Toast.makeText(getContext(), "Incorrect password!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Email not found!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Login failed! Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }
}

