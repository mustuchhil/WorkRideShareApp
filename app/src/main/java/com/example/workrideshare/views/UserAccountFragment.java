package com.example.workrideshare.views;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.workrideshare.R;
import com.example.workrideshare.databinding.FragmentUserAccountBinding;
import com.example.workrideshare.helpers.LocationHelper;
import com.example.workrideshare.models.User;
import com.example.workrideshare.repositories.UserRepository;
import com.example.workrideshare.viewmodels.UserViewModel;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

public class UserAccountFragment extends Fragment implements View.OnClickListener {
    private final String TAG = this.getClass().getCanonicalName();
    private FragmentUserAccountBinding binding;
    private String currEmail;
//    private UserRepository userRepository;
    private UserViewModel userViewModel;
    private User newUser;
    private String latlng;
    private User matchedUser;

    public UserAccountFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            currEmail = getArguments().getString("params");
        }

//        this.locationHelper = LocationHelper.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.binding = FragmentUserAccountBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        this.userViewModel = UserViewModel.getInstance(this.getActivity().getApplication());
        this.newUser = new User();

//        this.userViewModel.getAllUsers();
//        this.userViewModel.searchUserByEmail(currEmail);

        this.userViewModel.getUserRepository().userFromDB.observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                binding.lblUserName.setText(user.getName());
                binding.lblEmail.setText(user.getEmail());
                binding.lblPhoneNo.setText(user.getPhoneNum());
                matchedUser = user;
            }
        });
        this.userViewModel.userCityName.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.lblCity.setText(s);
            }
        });
        this.userViewModel.userCountryName.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.lblCountry.setText(s);
            }
        });

        binding.btnUpdateAcc.setOnClickListener(this);
        binding.btnDeleteAcc.setOnClickListener(this);
        binding.btnLogout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view != null){
            switch (view.getId()){
                case R.id.btnUpdateAcc:{
                    //to update account
                    this.updateData();
                    break;
                }
                case R.id.btnDeleteAcc:{
                    //to delete account
                    //this.userViewModel.deleteUser(this.currEmail);
                    this.userViewModel.deleteUser();
                    Intent intent1 = new Intent(this.getContext(),MainActivity.class);
                    startActivity(intent1);
                    break;
                }
                case R.id.btnLogout:{
                    //to logout
                    Intent intent1 = new Intent(this.getContext(),MainActivity.class);
                    startActivity(intent1);
                    break;
                }
            }
        }

    }

    private void updateData() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Update User Information");
        alert.setMessage("Please update the below fields");

        // Set an EditText view to get user input
        LinearLayout linear = new LinearLayout(getActivity().getApplication());
        linear.setOrientation(LinearLayout.VERTICAL);

        final EditText changedName = new EditText(getActivity());
        changedName.setHint("Updated Name");

        final EditText changedPhoneNum = new EditText(getActivity());
        changedPhoneNum.setHint("Updated Phone No");

        linear.addView(changedName);
        linear.addView(changedPhoneNum);
        alert.setView(linear);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                //Log.d(TAG,"alertUpdUser: " + matchedUser.getName());
                if (changedName.getText().toString().trim().isEmpty() && changedPhoneNum.getText().toString().trim().isEmpty()){
                    return;
                }else{
                    if (changedName.getText().toString().trim().isEmpty()) {
                        newUser.setName(matchedUser.getName());
                    }else{
                        newUser.setName(changedName.getText().toString().trim());
                    }

                    if(changedPhoneNum.getText().toString().trim().isEmpty()){
                        newUser.setPhoneNum(matchedUser.getPhoneNum());
                    }else{
                        newUser.setPhoneNum(changedPhoneNum.getText().toString().trim());
                    }

                    userViewModel.getUserRepository().updateUser(newUser);

                    binding.lblUserName.setText(newUser.getName());
                    binding.lblPhoneNo.setText(newUser.getPhoneNum());
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
}