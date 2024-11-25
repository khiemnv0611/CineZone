package com.khiemnv.cinezone.repository;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khiemnv.cinezone.model.UserModel;

public class UserRepository {
    private final DatabaseReference userRef;

    public UserRepository() {
        userRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    public void registerUser(UserModel user, OnCompleteListener<Void> listener) {
        userRef.child(user.getUserId()).setValue(user).addOnCompleteListener(listener);
    }

    public void loginUser(String email, String password, OnCompleteListener<DataSnapshot> listener) {
        userRef.orderByChild("email").equalTo(email).get().addOnCompleteListener(listener);
    }
}

