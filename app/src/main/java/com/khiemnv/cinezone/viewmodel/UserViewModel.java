package com.khiemnv.cinezone.viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.khiemnv.cinezone.model.UserModel;
import com.khiemnv.cinezone.repository.UserRepository;

import org.mindrot.jbcrypt.BCrypt;

public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;

    public UserViewModel() {
        userRepository = new UserRepository();
    }

    // Đăng ký người dùng
    public void registerUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        userRepository.registerUser(email, password, listener);
    }

    // Đăng nhập người dùng
    public void loginUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        userRepository.loginUser(email, password, listener);
    }

    // Đăng xuất người dùng
    public void logout() {
        userRepository.logout();
    }

    // Kiểm tra người dùng hiện tại
    public FirebaseUser getCurrentUser() {
        return userRepository.getCurrentUser();
    }

    // Thêm người dùng vào Realtime Database (không lưu mật khẩu)
    public void addUserToDatabase(UserModel user, OnCompleteListener<Void> listener) {
        userRepository.addUserToDatabase(user, listener);
    }
}
