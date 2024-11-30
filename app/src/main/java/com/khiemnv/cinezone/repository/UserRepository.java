package com.khiemnv.cinezone.repository;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khiemnv.cinezone.model.UserModel;

public class UserRepository {
    private final FirebaseAuth firebaseAuth;
    private final DatabaseReference userRef;

    public UserRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    public void registerUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    public void loginUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    public void logout() {
        firebaseAuth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void addUserToDatabase(UserModel user, OnCompleteListener<Void> listener) {
        userRef.child(user.getUserId()).setValue(user).addOnCompleteListener(listener);
    }
}