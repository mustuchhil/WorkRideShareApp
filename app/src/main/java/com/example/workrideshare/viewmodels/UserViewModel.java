package com.example.workrideshare.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.workrideshare.models.User;
import com.example.workrideshare.repositories.UserRepository;

import java.lang.invoke.MutableCallSite;
import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private final String TAG = this.getClass().getCanonicalName();
    private static UserViewModel ourInstance;
    private static UserRepository userRepository;
    public MutableLiveData<String> userCityName = new MutableLiveData<>(); ;
    public MutableLiveData<String> userCountryName = new MutableLiveData<>(); ;
    public User loggedInUser;

    public UserViewModel(Application application){
        super(application);
    }

    public static UserViewModel getInstance(Application application){
        if (ourInstance == null){
            userRepository =  new UserRepository();
            ourInstance = new UserViewModel(application);
        }
        return ourInstance;
    }

    public UserRepository getUserRepository(){
        return this.userRepository;
    }

    public void viewSignUp(String email, String password, String name, String number){
        Log.e(TAG, "LoggedInUser: 1" + password);
        this.userRepository.signUp(email, password, name, number);
        Log.e(TAG, "LoggedInUser: 2" + email);
    }

    public void logInUser(String email, String password) {
        this.userRepository.signIn(email, password);
    }

    public void deleteUser(){
        this.userRepository.deleteUser();
    }

    public void updateFriend(User updatedUser){
        this.userRepository.updateUser(updatedUser);
    }

    public void setUserCityName(MutableLiveData<String> city){
        this.userCityName = city;
    }

    public MutableLiveData<String> getUserCityName(Context context) {
        return userCityName;
    }

    public MutableLiveData<String> getUserCountryName(Context context) {
        return userCountryName;
    }

    public void setUserCountryName(MutableLiveData<String> userCountryName) {
        this.userCountryName = userCountryName;
    }
}
