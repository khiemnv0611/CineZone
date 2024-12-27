package com.khiemnv.cinezone.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.khiemnv.cinezone.MainActivity;
import com.khiemnv.cinezone.R;
import com.khiemnv.cinezone.model.UserModel;
import com.khiemnv.cinezone.viewmodel.UserViewModel;

public class SignInFragment extends Fragment {
    private UserViewModel viewModel;

    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;

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
            String email = etEmail.getText().toString().trim().toLowerCase();
            String password = etPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Email and password are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Invalid email format!", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.loginUser(email, password, task -> {
                if (task.isSuccessful()) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null && currentUser.isEmailVerified()) {
                        handleUserLogin(currentUser, false);  // Đăng nhập bằng email, không tạo mới người dùng
                    } else {
                        Toast.makeText(getContext(), "Please verify your email.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        firebaseAuth = FirebaseAuth.getInstance();

        // Cấu hình Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

        LinearLayout btnGoogleSignIn = view.findViewById(R.id.btnGoogleSignIn);
        btnGoogleSignIn.setOnClickListener(v -> signInWithGoogle());

        return view;
    }

    private void signInWithGoogle() {
        // Đăng xuất Google trước khi mở giao diện chọn tài khoản
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Sau khi đăng xuất, khởi động Intent đăng nhập
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            } else {
                Log.e("GoogleSignIn", "Sign out failed: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            try {
                // Nhận tài khoản Google
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                Toast.makeText(getContext(), "Google Sign-In failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("GoogleSignIn", "ApiException: ", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("GoogleSignIn", "firebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    Log.d("GoogleSignIn", "User logged in: " + currentUser.getDisplayName());
                    handleUserLogin(currentUser, true);  // Đăng nhập qua Google, tạo mới người dùng nếu chưa có
                }
            } else {
                Log.e("GoogleSignIn", "Authentication failed: " + task.getException().getMessage());
            }
        });
    }

    private void handleUserLogin(FirebaseUser currentUser, boolean isGoogleLogin) {
        // Lấy token và lưu vào SharedPreferences
        currentUser.getIdToken(true).addOnCompleteListener(tokenTask -> {
            if (tokenTask.isSuccessful()) {
                String token = tokenTask.getResult().getToken();
                Log.d("Login", "Token: " + token);

                // Lưu token vào SharedPreferences
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("auth_token", token);
                editor.putString("user_email", currentUser.getEmail());
                editor.apply();

                // Kiểm tra người dùng trong Realtime Database
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
                userRef.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            proceedToMainActivity();  // Người dùng đã tồn tại, chuyển tới MainActivity
                        } else {
                            if (isGoogleLogin) {
                                // Nếu là đăng nhập qua Google, tạo mới người dùng trong Realtime Database
                                createNewUser(currentUser);
                            } else {
                                // Nếu là đăng nhập qua email/password và người dùng không tồn tại
                                Toast.makeText(getContext(), "User data missing. Please contact support.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Login", "Database error: " + error.getMessage());
                    }
                });
            } else {
                Log.e("Login", "Error fetching token: " + tokenTask.getException().getMessage());
            }
        });
    }

    private void createNewUser(FirebaseUser currentUser) {
        UserModel newUser = new UserModel(
                currentUser.getDisplayName().split(" ")[0],  // Tên đầu
                currentUser.getDisplayName().split(" ").length > 1 ? currentUser.getDisplayName().split(" ")[1] : "",
                currentUser.getEmail(),
                false,
                currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : ""
        );
        newUser.setUserId(currentUser.getUid());

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.child(currentUser.getUid()).setValue(newUser).addOnCompleteListener(dbTask -> {
            if (dbTask.isSuccessful()) {
                proceedToMainActivity();  // Người dùng mới đã được tạo, chuyển tới MainActivity
            } else {
                Log.e("Login", "Error saving user data: " + dbTask.getException().getMessage());
            }
        });
    }

    private void proceedToMainActivity() {
        Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}

