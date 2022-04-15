package com.example.workrideshare.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.workrideshare.R;
import com.example.workrideshare.databinding.ActivityAddPostBinding;
import com.example.workrideshare.models.Post;
import com.example.workrideshare.repositories.UserRepository;
import com.example.workrideshare.viewmodels.PostViewModel;
import com.example.workrideshare.viewmodels.UserViewModel;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = this.getClass().getCanonicalName();
    private ActivityAddPostBinding binding;
    private PostViewModel postViewModel;
    private Post newPost;
    private UserViewModel userViewModel;
//    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_post);
        this.binding = ActivityAddPostBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.postViewModel = PostViewModel.getInstance(this.getApplication());

        this.userViewModel = UserViewModel.getInstance(this.getApplication());
        this.newPost = new Post();
        this.binding.btnSavePost.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.btnSavePost: {
                    Boolean validData = true;

                    if (this.binding.editTextFrom.getText().toString().isEmpty()){
                        this.binding.editTextFrom.setError("Source Cannot be Empty");
                        validData = false;
                    }else{
                        this.newPost.setFromDest(this.binding.editTextFrom.getText().toString());
                    }

                    if (this.binding.editToDest.getText().toString().isEmpty()){
                        this.binding.editToDest.setError("Destination Cannot be Empty");
                        validData = false;
                    }else{
                        this.newPost.setToDest(this.binding.editToDest.getText().toString());
                    }

                    if (this.binding.editNoOfSeats.getText().toString().isEmpty()){
                        this.binding.editNoOfSeats.setError("No. of Seats Cannot be Empty");
                        validData = false;
                    }else {
                        int currSeats = Integer.parseInt(this.binding.editNoOfSeats.getText().toString());
                        this.newPost.setNumOfSeats(currSeats);
                    }

                    if (this.binding.editTextPhone.getText().toString().isEmpty()){
                        this.binding.editTextPhone.setError("Contact No. cannot be Empty");
                        validData = false;
                    }else{
                        this.newPost.setContact(this.binding.editTextPhone.getText().toString());
                    }

                    if (this.binding.editTextCity.getText().toString().isEmpty()){
                        this.binding.editTextCity.setError("City cannot be Empty");
                        validData = false;
                    }else{
                        this.newPost.setPostCity(this.binding.editTextCity.getText().toString().toUpperCase());
                    }

                    if (this.binding.editTextDesc.getText().toString().isEmpty()){
                        this.binding.editTextDesc.setError("Description cannot be Empty");
                        validData = false;
                    }else{
                        this.newPost.setDesc(this.binding.editTextDesc.getText().toString());
                    }

                    if (validData){
                        this.newPost.setUserID(userViewModel.getUserRepository().loggedInUser.getId().toString());
                        Toast.makeText(this, "Post Saved", Toast.LENGTH_SHORT).show();
                        this.postViewModel.addPost(this.newPost);
                    }else{
                        Toast.makeText(this, "Please provide correct inputs", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    }
}