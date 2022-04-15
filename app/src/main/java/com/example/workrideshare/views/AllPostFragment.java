package com.example.workrideshare.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workrideshare.R;
import com.example.workrideshare.databinding.FragmentAllPostBinding;
import com.example.workrideshare.models.Post;
import com.example.workrideshare.repositories.PostRepository;
import com.example.workrideshare.viewmodels.PostViewModel;
import com.example.workrideshare.viewmodels.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class AllPostFragment extends Fragment{
    private FragmentAllPostBinding binding;
    private UserViewModel userViewModel;
    private PostViewModel postViewModel;
    private AllPostsAdapter allPostsAdapter;
    private String mParam1;
    private String mParam2;
    private ArrayList<Post> allpostArrayList;

    public AllPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
////            mParam1 = getArguments().getString(ARG_PARAM1);
////            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.binding = FragmentAllPostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        this.userViewModel = UserViewModel.getInstance(this.getActivity().getApplication());
        this.postViewModel = PostViewModel.getInstance(this.getActivity().getApplication());

        this.allpostArrayList = new ArrayList<>();
        this.allPostsAdapter = new AllPostsAdapter(getContext(), this.allpostArrayList);
        this.binding.rvAllPostList.setLayoutManager(new LinearLayoutManager(getContext()));
        this.binding.rvAllPostList.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
        this.binding.rvAllPostList.setAdapter(this.allPostsAdapter);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Log.d("GETPOSTS ","NOT LOGGED IN ");
        } else {
            //Log.d("GETPOSTS ","GETmyPost: "+FirebaseAuth.getInstance().getCurrentUser().getUid());
            this.postViewModel.getAllPosts(this.userViewModel.userCityName.getValue().toUpperCase());
            userViewModel.getUserRepository().getUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }

//        This is for ALL POSTS
        this.postViewModel.allPosts.observe(getActivity(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                if (posts != null) {
                    for (Post post: posts) {
                        allpostArrayList.clear();
                        allpostArrayList.addAll(posts);
                        Log.d("ALLPOSTS tata ","myPost: "+allpostArrayList);
                        allPostsAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        return view;
    }
}