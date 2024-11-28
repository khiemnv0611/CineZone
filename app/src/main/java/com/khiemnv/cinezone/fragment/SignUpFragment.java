package com.khiemnv.cinezone.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.khiemnv.cinezone.MainActivity;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.model.UserModel;
import com.khiemnv.cinezone.viewmodel.UserViewModel;

public class SignUpFragment extends Fragment {
    private UserViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout cho fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        // Lấy TextView từ layout
        TextView loginText = view.findViewById(R.id.login_text);

        // Lấy nội dung chuỗi từ resources
        String text = getString(R.string.signin_prompt); // Đảm bảo bạn có chuỗi này trong resources

        // Sử dụng SpannableString để thay đổi màu và tạo sự kiện nhấn
        SpannableString spannable = new SpannableString(text);

        // Sử dụng ContextCompat.getColor() để lấy màu
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.mainColor));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // Chuyển sang SignInFragment khi nhấn
                navigateToFragment(new SignInFragment());
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(requireContext(), R.color.mainColor));
                ds.setUnderlineText(false);
                ds.setFakeBoldText(true);
            }
        };


        // Xác định vị trí của từ "Sign in" hoặc "Đăng nhập" dựa trên chuỗi hiện tại
        int start, end;
        if (text.contains("Sign in")) {
            start = text.indexOf("Sign in");
            end = start + "Sign in".length();
        } else if (text.contains("Đăng nhập")) {
            start = text.indexOf("Đăng nhập");
            end = start + "Đăng nhập".length();
        } else {
            // Đề phòng trường hợp không tìm thấy chuỗi
            start = 0;
            end = 0;
        }

        // Áp dụng màu và tính nhấn cho phần "Sign in" hoặc "Đăng nhập"
        spannable.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Đặt SpannableString vào TextView và kích hoạt khả năng nhấn
        loginText.setText(spannable);
        loginText.setMovementMethod(LinkMovementMethod.getInstance());

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        EditText etFirstName = view.findViewById(R.id.etFirstName);
        EditText etLastName = view.findViewById(R.id.etLastName);
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etPassword = view.findViewById(R.id.etPassword);
        EditText etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        Button btnSignUp = view.findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(v -> {
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim().toLowerCase();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (firstName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Invalid email format!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(getContext(), "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.checkEmailExists(email, task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    if (task.getResult().exists()) {
                        Toast.makeText(getContext(), "Email already exists!", Toast.LENGTH_SHORT).show();
                    } else {
                        String hashedPassword = viewModel.hashPassword(password);
                        UserModel user = new UserModel(firstName, lastName, email, hashedPassword, false, null);
                        viewModel.registerUser(user, registerTask -> {
                            if (registerTask.isSuccessful()) {
                                Toast.makeText(getContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                                navigateToFragment(new SignInFragment());
                            } else {
                                Toast.makeText(getContext(), "Registration failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Error checking email!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
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

    private void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        // Áp dụng hiệu ứng chuyển đổi
        transaction.setCustomAnimations(
                R.anim.slide_in_left,  // Animation vào của fragment mới
                R.anim.slide_out_right,  // Animation ra của fragment hiện tại
                R.anim.slide_in_right,  // Animation quay lại vào của fragment cũ
                R.anim.slide_out_left  // Animation quay lại ra của fragment mới
        );

        transaction.replace(R.id.auth_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

