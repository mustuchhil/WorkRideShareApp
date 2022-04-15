package com.example.workrideshare.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.workrideshare.R;
import com.example.workrideshare.databinding.FragmentMyPostsBinding;
import com.example.workrideshare.databinding.FragmentUserAccountBinding;
import com.example.workrideshare.models.Post;
import com.example.workrideshare.models.User;
import com.example.workrideshare.repositories.PostRepository;
import com.example.workrideshare.repositories.UserRepository;
import com.example.workrideshare.viewmodels.PostViewModel;
import com.example.workrideshare.viewmodels.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class MyPostsFragment extends Fragment implements View.OnClickListener, OnMyPostClickListener{

    private final String TAG = this.getClass().getCanonicalName();
    private FragmentMyPostsBinding binding;
    private PostsAdapter postsAdapter;
    private UserViewModel userViewModel;
    private PostRepository postRepository;
    private PostViewModel postViewModel;
    private Post currPost;
    private User newUser;
    private ArrayList<Post> postArrayList;
    private String currEmail;
    private Post updatePost;

    public MyPostsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.binding = FragmentMyPostsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        this.updatePost = new Post();
        this.userViewModel = UserViewModel.getInstance(this.getActivity().getApplication());
        this.postViewModel = PostViewModel.getInstance(this.getActivity().getApplication());
        if (getArguments() != null) {
            currEmail = getArguments().getString("params");
        }
        this.postArrayList = new ArrayList<>();
        Log.d("MYEMPTYPOST ","NOT LOGGED IN "+this.postViewModel.getPostRepository().myPostsFromDB);

        this.postsAdapter = new PostsAdapter(getContext(), this.postArrayList, this);
        this.binding.rvMyPostList.setLayoutManager(new LinearLayoutManager(getContext()));
        this.binding.rvMyPostList.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
        this.binding.rvMyPostList.setAdapter(this.postsAdapter);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Log.d("MYPOSTS ","NOT LOGGED IN ");
        } else {
//            this.userViewModel.getUserRepository().loggedInUser.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
            Log.d("MYPOSTS ","myPost: "+FirebaseAuth.getInstance().getCurrentUser().getUid());
            this.postViewModel.getMyPosts(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        ;
        this.postViewModel.myPosts.observe(this.getActivity(), new Observer<ArrayList<Post>>() {
            @Override
            public void onChanged(ArrayList<Post> posts) {
                if (posts != null){
                    postArrayList.clear();
                    postArrayList.addAll(posts);
                    Log.d("MYPOSTS","myPost: "+postArrayList);
                    postsAdapter.notifyDataSetChanged();
                }
            }
        });

        binding.btnAddPost.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.btnAddPost: {
                    //to save post
                    Intent intentAddPost = new Intent(view.getContext(),AddPostActivity.class);
                    startActivity(intentAddPost);
                    break;
                }
            }
        }
    }

    @Override
    public void OnMyPostItemClicked(Post post) {
        Log.e(TAG, "onRecipeItemClicked: User selected " + post.toString() );

        updateData(post);

        Toast.makeText(this.getContext(), "Recipe selected " + post.getDateCreated(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void DeleteMyPostItemClicked(Post postD) {
        Log.e(TAG, "onRecipeItemClicked: User selected to be deleted" + postD.toString() );
        deleteData(postD);
        Toast.makeText(this.getContext(), "Recipe selected " + "Delete Successfull", Toast.LENGTH_LONG).show();

    }

    private void updateData(Post postD) {

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Update User Information");
        alert.setMessage("Please update the below fields");

        // Set an EditText view to get user input
        LinearLayout linear = new LinearLayout(getActivity().getApplication());
        linear.setOrientation(LinearLayout.VERTICAL);

        final EditText changedName = new EditText(getActivity());
        changedName.setHint("Updated Number of Seats");

        linear.addView(changedName);
        alert.setView(linear);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                //Log.d(TAG,"alertUpdUser: " + matchedUser.getName());
                if (changedName.getText().toString().trim().isEmpty()){
                    return;
                }else{
                        int number = Integer.parseInt(changedName.getText().toString());
                        postD.setNumOfSeats(number);

                    for (int i = 0; i < postArrayList.size(); i++) {
                        if (postD.getPostID() == postArrayList.get(i).getPostID()) {
                            postArrayList.get(i).setNumOfSeats(number);
                            postArrayList.remove(i);
                        }
                    }
                    postsAdapter.notifyDataSetChanged();
                }

                    postViewModel.updatePost(postD);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    private void deleteData(Post postD) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("DELETE POST");
        alert.setMessage("Di you want to delete ?");

        // Set an EditText view to get user input
        LinearLayout linear = new LinearLayout(getActivity().getApplication());
        linear.setOrientation(LinearLayout.VERTICAL);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                    for (int i = 0; i < postArrayList.size(); i++) {
                        if (postD.getPostID() == postArrayList.get(i).getPostID()) {
                            postArrayList.remove(i);
                        }
                    }
                    postsAdapter.notifyDataSetChanged();

                postViewModel.deletePost(postD);
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