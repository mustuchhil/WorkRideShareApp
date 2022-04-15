package com.example.workrideshare.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.workrideshare.R;
import com.example.workrideshare.databinding.ActivitySignUpBinding;
import com.example.workrideshare.models.User;
import com.example.workrideshare.repositories.UserRepository;
import com.example.workrideshare.viewmodels.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = this.getClass().getCanonicalName();
    private ActivitySignUpBinding binding;
    private User newUser;
    private FirebaseAuth mAuth;
    private UserViewModel userViewModel;
    private SignUpActivity signUpActivity;
//    private UserRepository userRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sign_up);
        this.binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.userViewModel = UserViewModel.getInstance(this.getApplication());
        this.newUser = new User();
//        this.userRepository = this.userViewModel.getUserRepository();
        this.binding.btnCreateAccount.setOnClickListener(this);
        this.mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        if(view != null){
            switch (view.getId()){
                case R.id.btnCreateAccount:{
                    this.validateData();
                    break;
                }
            }
        }
    }

    private void validateData() {
        Boolean validData = true;
        String email = "";
        String password = "";
        String name = "";
        String number = "";

        if (this.binding.editTextUserName.getText().toString().isEmpty()){
            this.binding.editTextUserName.setError("Name Cannot be Empty");
            validData = false;
        }else{
            //this.newUser.setName(this.binding.editTextUserName.getText().toString());
            name = this.binding.editTextUserName.getText().toString();
            this.newUser.setName(name);
        }

        if (this.binding.editEmailAddress.getText().toString().isEmpty()){
            this.binding.editEmailAddress.setError("Email Cannot be Empty");
            validData = false;
        }else{
            //this.newUser.setEmail(this.binding.editEmailAddress.getText().toString());
            email = this.binding.editEmailAddress.getText().toString();
            this.newUser.setEmail(email);
        }

        if (this.binding.editTextPassword.getText().toString().isEmpty()){
            this.binding.editTextPassword.setError("Password Cannot be Empty");
            validData = false;
        }else {
            password = this.binding.editTextPassword.getText().toString();
        }

        if (this.binding.editTextPhone.getText().toString().isEmpty()){
            this.binding.editTextPhone.setError("Contact No. cannot be Empty");
            validData = false;
        }else{
            number = this.binding.editTextPhone.getText().toString();
            this.newUser.setPhoneNum(number);
        }

        if (validData){
//            this.userViewModel.addUser(this.newUser);
            this.createAccount(email, password, name, number);
            //this.userRepository.signUp(email, password);

        }else{
            Toast.makeText(this, "Please provide correct inputs", Toast.LENGTH_SHORT).show();
        }
    }

    private void createAccount(String email, String password, String name, String number){
        this.userViewModel.viewSignUp(email, password, name, number);
        Intent mainIntent = new Intent(getApplicationContext(), HomeActivity.class);
        mainIntent.putExtra("EXTRA_EMAIL",email);

        startActivity(mainIntent);
    }
}