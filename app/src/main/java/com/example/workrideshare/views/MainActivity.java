package com.example.workrideshare.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.workrideshare.R;
import com.example.workrideshare.databinding.ActivityMainBinding;
import com.example.workrideshare.helpers.LocationHelper;
import com.example.workrideshare.viewmodels.UserViewModel;
import com.example.workrideshare.views.SignInActivity;
import com.example.workrideshare.views.SignUpActivity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private UserViewModel userViewModel;
    private final String TAG = this.getClass().getCanonicalName();
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        this.binding.btnSignIn.setOnClickListener(this);
        this.binding.btnSigUp.setOnClickListener(this);
        this.userViewModel = UserViewModel.getInstance(this.getApplication());
    }



    @Override
    public void onClick(View view) {
        if(view != null){
            switch (view.getId()){
                case R.id.btnSignIn:{
                    Intent insertIntent = new Intent(this, SignInActivity.class);
                    startActivity(insertIntent);
                    break;
                }
                case R.id.btnSigUp:{
                    Intent insertIntent = new Intent(this, SignUpActivity.class);
                    startActivity(insertIntent);
                    break;
                }
            }
        }
    }
}