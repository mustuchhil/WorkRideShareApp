package com.example.workrideshare.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workrideshare.R;
import com.example.workrideshare.databinding.PostsItemBinding;
import com.example.workrideshare.models.Post;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {
    private PostsItemBinding binding;
    private ArrayList<Post> myPostList;
    private Context context;
    private final OnMyPostClickListener clickListener;

    public PostsAdapter(Context context, ArrayList<Post> myPostList, OnMyPostClickListener clickListener){
        this.context = context;
        this.myPostList = myPostList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public PostsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(PostsItemBinding.inflate(LayoutInflater.from(context), parent, false));

//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_item, parent, false);
//        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Post currentRecipe = this.myPostList.get(position);
//        String fromDest = myPostList.get(position).getFromDest();

        holder.bind(context, currentRecipe, clickListener);
    }

    @Override
    public int getItemCount() {
        return this.myPostList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        PostsItemBinding binding;

        public MyViewHolder(PostsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Context context, final Post currentPost, OnMyPostClickListener clickListener){
            binding.tvFrom.setText(currentPost.getFromDest());
            binding.tvTo.setText(currentPost.getToDest());
            binding.tvNumOfSeats.setText(""+currentPost.getNumOfSeats());

            binding.imgEdit.setOnClickListener(new View.OnClickListener(){ //associate clicklistner to only imageview
                @Override
                public void onClick(View v) {
                    clickListener.OnMyPostItemClicked(currentPost);
                }
            });
            binding.imgDelete.setOnClickListener(new View.OnClickListener(){ //associate clicklistner to only imageview
                @Override
                public void onClick(View v) {
                    clickListener.DeleteMyPostItemClicked(currentPost);
                }
            });
        }
    }
}