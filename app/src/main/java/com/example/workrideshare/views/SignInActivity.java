package com.example.workrideshare.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.workrideshare.R;
import com.example.workrideshare.databinding.ActivitySignInBinding;
import com.example.workrideshare.databinding.ActivitySignUpBinding;
import com.example.workrideshare.models.User;
import com.example.workrideshare.repositories.UserRepository;
import com.example.workrideshare.viewmodels.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = this.getClass().getCanonicalName();
    private ActivitySignInBinding binding;
    private User user;
    private FirebaseAuth mAuth;
    private UserViewModel userViewModel;
//    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sign_in);
        this.binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.binding.btnLogin.setOnClickListener(this);

        this.mAuth = FirebaseAuth.getInstance();
        this.userViewModel = UserViewModel.getInstance(this.getApplication());
    }

    @Override
    public void onClick(View view) {
        if(view != null){
            switch (view.getId()){
                case R.id.btnLogin:{
                    this.validateData();
                    break;
                }
            }
        }
    }

    private void validateData(){
        Boolean validData = true;
        String email = "";
        String password = "";

        if (this.binding.editEmail.getText().toString().isEmpty()){
            this.binding.editEmail.setError("Email Cannot be Empty");
            validData = false;
        }else{
            email = this.binding.editEmail.getText().toString();
        }

        if (this.binding.editPassword.getText().toString().isEmpty()){
            this.binding.editPassword.setError("Password Cannot be Empty");
            validData = false;
        }else {
            password = this.binding.editPassword.getText().toString();
        }

        if (validData){
            //this.userRepository.signIn(email, password);
            this.signIn(email,password);
        }else{
            Toast.makeText(this, "Please provide correct inputs", Toast.LENGTH_SHORT).show();
        }
    }

    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            userViewModel.getUserRepository().loggedInUser.setId(mAuth.getUid());
                            Intent intentHome = new Intent(getApplicationContext(), HomeActivity.class);
                            intentHome.putExtra("EXTRA_EMAIL",email);
                            startActivity(intentHome);
                        }else{
                            Log.e(TAG, "onComplete: Sign In Failed", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}