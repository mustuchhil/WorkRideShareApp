package com.example.workrideshare.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.workrideshare.models.Post;
import com.example.workrideshare.repositories.PostRepository;

import java.util.ArrayList;
import java.util.List;

public class PostViewModel extends AndroidViewModel {

    private static PostViewModel ourInstance;
    public MutableLiveData<List<Post>> allPosts;
    public MutableLiveData<ArrayList<Post>> myPosts = new MutableLiveData<>();
    private final PostRepository postRepository = new PostRepository();

    public PostViewModel(Application application) {
        super(application);
    }

    public static PostViewModel getInstance(Application application){
        if (ourInstance == null){
            ourInstance = new PostViewModel(application);
        }
        return ourInstance;
    }

    public PostRepository getPostRepository(){
        return this.postRepository;
    }

    public void addPost(Post newPost){
        Log.d("TAG","currentPost: " + newPost.toString());
        this.postRepository.addPost(newPost);
    }

    public void getMyPosts(String userID){
        Log.e("TAG","GetPost: " + userID);
        this.postRepository.getMyPosts(userID);
        this.myPosts = this.postRepository.myPostsFromDB;
    }

    public void getAllPosts(String cityName){
        this.postRepository.getAllPosts(cityName);
        this.allPosts = this.postRepository.allPosts;
    }

    public void updatePost(Post updatedPost){
        this.postRepository.updatePost(updatedPost);

    }

    public void deletePost(Post post){
        this.postRepository.deletePost(post);
    }
}
