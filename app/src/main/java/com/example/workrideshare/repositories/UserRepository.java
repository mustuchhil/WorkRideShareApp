package com.example.workrideshare.repositories;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.workrideshare.models.User;
import com.example.workrideshare.viewmodels.UserViewModel;
import com.example.workrideshare.views.HomeActivity;
import com.example.workrideshare.views.SignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserRepository {
    private final String TAG = this.getClass().getCanonicalName();
    private final FirebaseFirestore DB;
    private final String COLLECTION_USERS = "Users";
    private final String FIELD_NAME = "name";
    private final String FIELD_EMAIL = "nmail";
    private final String FIELD_PHONE = "nhoneNum";
    public User loggedInUser = new User();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public MutableLiveData<List<User>> allUsers = new MutableLiveData<>();
    public MutableLiveData<User> userFromDB = new MutableLiveData<>();
    public List<User> apnalist = new ArrayList<>();
    private  String pass;

    public UserRepository() {
        DB = FirebaseFirestore.getInstance();
    }

    public User signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG,"mauthID" + mAuth.getUid());
                            Log.d(TAG,"Not able to fetch the id");
                            loggedInUser.setId(mAuth.getUid());
                        }else{
                            Log.e(TAG, "onComplete: Sign In Failed", task.getException());
                        }
                    }
                });
        return this.loggedInUser;
    }

    public void signUp(String email, String password, String name, String number){
        this.pass = password;
        Log.e(TAG, "LoggedInUser: 3 " + email);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()){
                           loggedInUser.setId(mAuth.getUid());
                           loggedInUser.setName(name);
                           loggedInUser.setEmail(email);
                           loggedInUser.setPhoneNum(number);
                           Log.e(TAG, "LoggedInuSER: " + loggedInUser.toString());
                           addUser();
                        } else{
                            Log.e(TAG, "onComplete: Failed to create user with email and password" + task.getException() + task.getException().getLocalizedMessage());
                            //Toast.makeText(SignUpActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void addUser(){
        try{
            Log.d(TAG,"userRepository " + loggedInUser.getName());

            User newUserDB = new User(loggedInUser.getName(), loggedInUser.getEmail(), loggedInUser.getPhoneNum(), UUID.randomUUID().toString());

            //create a new document with User ID as Doc Id
            DB.collection(COLLECTION_USERS)
                    .document(loggedInUser.getId())
                    .set(newUserDB);

            signIn(loggedInUser.getEmail(), this.pass);

        }catch (Exception ex){
            Log.e(TAG, "addUser: " + ex.getLocalizedMessage());
        }
    }

    public void getUser(String userID){
        try{
            DB.collection(COLLECTION_USERS)
                    .document(userID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            loggedInUser = documentSnapshot.toObject(User.class);
                            if (loggedInUser != null){
                                Log.d(TAG, "onComplete: User found " + loggedInUser.toString());
                                userFromDB.postValue(loggedInUser);
                            }else{
                                Log.e(TAG, "onComplete: Unable to convert the matching document to Friend object");
                            }
                        }
                    });

        }catch(Exception ex){
            Log.e(TAG, "getAllUsers: Exception occurred " + ex.getLocalizedMessage() );
            Log.e(TAG, String.valueOf(ex.getStackTrace()));
        }
    }

    public void deleteUser(){
        try{
            DB.collection(COLLECTION_USERS)
                    .document(loggedInUser.getId())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: Document successfully deleted");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Unable to delete document" + e.getLocalizedMessage());
                        }
                    });

        }catch(Exception ex){
            Log.e(TAG, "deleteUser: Exception occurred " + ex.getLocalizedMessage() );
        }
    }

    public void updateUser(User updatedUser){
        Map<String, Object> updatedInfo = new HashMap<>();
        updatedInfo.put(FIELD_NAME, updatedUser.getName());
        updatedInfo.put(FIELD_PHONE, updatedUser.getPhoneNum());
//        .update(FIELD_NAME, "Changed Name")
        try{
            DB.collection(COLLECTION_USERS)
                    .document(loggedInUser.getId())
                    .update(updatedInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: Document successfully updated");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Unable to update document" + e.getLocalizedMessage());
                        }
                    });
        }catch(Exception ex){
            Log.e(TAG, "updateUser: Exception occurred " + ex.getLocalizedMessage() );
        }
    }

}
