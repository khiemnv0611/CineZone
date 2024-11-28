package com.khiemnv.cinezone.viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.khiemnv.cinezone.model.UserModel;
import com.khiemnv.cinezone.repository.UserRepository;

import org.mindrot.jbcrypt.BCrypt;

public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;

    public UserViewModel() {
        userRepository = new UserRepository();
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void checkEmailExists(String email, OnCompleteListener<DataSnapshot> listener) {
        userRepository.getUserByEmail(email).addOnCompleteListener(listener);
    }

    public void registerUser(UserModel user, OnCompleteListener<Void> listener) {
        userRepository.registerUser(user, listener);
    }

    public void loginUser(String email, String password, OnCompleteListener<DataSnapshot> listener) {
        userRepository.loginUser(email, password, listener);
    }
}

