package com.example.workrideshare.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.workrideshare.R;
import com.example.workrideshare.databinding.ActivityHomeBinding;
import com.example.workrideshare.helpers.LocationHelper;
import com.example.workrideshare.models.User;
import com.example.workrideshare.viewmodels.PostViewModel;
import com.example.workrideshare.viewmodels.UserViewModel;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity{
    private final String TAG = this.getClass().getCanonicalName();
    private ActivityHomeBinding binding;
    private PostViewModel postViewModel;
    private UserViewModel userViewModel;
    private Location lastLocation;
    private LocationHelper locationHelper;
    private LocationCallback locationCallback;
    String email = "test@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home);
        this.binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        BottomNavigationView navigationView = this.binding.menuNavigationBar;
        this.postViewModel = PostViewModel.getInstance(this.getApplication());
        this.userViewModel = UserViewModel.getInstance(this.getApplication());

        UserAccountFragment userAccountFragment = new UserAccountFragment();
        MyPostsFragment myPostsFragment = new MyPostsFragment();
        AllPostFragment AllPostsFragment = new AllPostFragment();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Log.d("GETPOSTS ","NOT LOGGED IN ");
        } else {
            Log.d("GETPOSTS ","GETmyPost: "+FirebaseAuth.getInstance().getCurrentUser().getUid());
            postViewModel.getMyPosts(FirebaseAuth.getInstance().getCurrentUser().getUid());
            userViewModel.getUserRepository().getUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }

        //        Location permission Request
        this.locationHelper = LocationHelper.getInstance();
        this.locationHelper.checkPermissions(this);
        this.locationHelper.getLastLocation(this).observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                if (location != null){
                    lastLocation = location;
//                    binding.tvLocationAddress.setText(lastLocation.toString());
                    Address obtainedAddress = locationHelper.performForwardGeocoding(getApplicationContext(), lastLocation);
                    if (obtainedAddress != null){
                        String city = obtainedAddress.getLocality();
                        MutableLiveData ct = new MutableLiveData();
                        ct.setValue(city);
                        userViewModel.setUserCityName(ct);

                        String country = obtainedAddress.getCountryName();
                        MutableLiveData ctry = new MutableLiveData();
                        ctry.setValue(country);
                        userViewModel.setUserCountryName(ctry);
                        postViewModel.getAllPosts(city);
                        Log.e(TAG, "RECENT Location Obtained"+obtainedAddress.getAddressLine(0));
                    }else{
                        Log.e(TAG, "onSuccess: Last Location Obtained Lat : " + location.getLatitude() +
                                " Lng : " + location.getLongitude());
                    }
                }else{
                    Log.e(TAG,"Last location not obtained");
                }
            }
        });

        this.initiateLocationListener();
        setFragment(myPostsFragment);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.myPost) {
                    //my post fragment
                    setFragment(myPostsFragment);
                    return true;
                }else if (id == R.id.allPosts) {
                    //all post fragment
                    setFragment(AllPostsFragment);
                    return true;
                } else if (id == R.id.account) {
                    setFragment(userAccountFragment);
                    return true;
                }
                return false;
            }
        });
    }

    private void initiateLocationListener(){
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for(Location loc : locationResult.getLocations()){
                    lastLocation = loc;
                    Address obtainedAddress = locationHelper.performForwardGeocoding(getApplicationContext(), lastLocation);

                    if (obtainedAddress != null){
                        String city = obtainedAddress.getLocality();
                        MutableLiveData ct = new MutableLiveData();
                        ct.setValue(city);
                        userViewModel.setUserCityName(ct);

                        String country = obtainedAddress.getCountryName();
                        MutableLiveData ctry = new MutableLiveData();
                        ctry.setValue(country);
                        userViewModel.setUserCountryName(ctry);
                        postViewModel.getAllPosts(city);
                        Log.e(TAG, "RECENT Location Obtained"+obtainedAddress.getLocality()+" , "+obtainedAddress.getCountryName());
                    }else{
                        Log.e(TAG, "onSuccess: Last Location Obtained Lat : " + lastLocation.toString());
                    }
                }
            }
        };

        this.locationHelper.requestLocationUpdates(this, locationCallback);
    }

    private void setFragment (Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("params",this.email);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame , fragment);
        fragment.setArguments(bundle);
        fragmentTransaction.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.locationHelper.stopLocationUpdates(this, this.locationCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.locationHelper.stopLocationUpdates(this, this.locationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.locationHelper.requestLocationUpdates(this, this.locationCallback);
    }

}