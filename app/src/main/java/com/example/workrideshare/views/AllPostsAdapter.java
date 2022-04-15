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
import com.example.workrideshare.databinding.AllPostsItemsBinding;
import com.example.workrideshare.databinding.PostsItemBinding;
import com.example.workrideshare.models.Post;

import java.util.ArrayList;

public class AllPostsAdapter extends RecyclerView.Adapter<AllPostsAdapter.MyViewHolder> {
    private AllPostsItemsBinding binding;
    private ArrayList<Post> allPostList;
    private Context context;

    public AllPostsAdapter(Context context, ArrayList<Post> myPostList){
        this.context = context;
        this.allPostList = myPostList;
    }

    @NonNull
    @Override
    public AllPostsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(AllPostsItemsBinding.inflate(LayoutInflater.from(context), parent, false));

//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_item, parent, false);
//        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Post currentRecipe = this.allPostList.get(position);
//        currentRecipe.getPostID()
//        String fromDest = myPostList.get(position).getFromDest();
        holder.bind(context, currentRecipe);
    }

    @Override
    public int getItemCount() {
        return this.allPostList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AllPostsItemsBinding binding;

        public MyViewHolder(AllPostsItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Context context, final Post currentPost){
            binding.tvFrom.setText(currentPost.getFromDest());
            binding.tvTo.setText(currentPost.getToDest());
            binding.tvNumOfSeats.setText(""+currentPost.getNumOfSeats());
            binding.tvContact.setText(""+currentPost.getContact());
            binding.tvDesc.setText(""+currentPost.getDesc());

            Log.d("MYPOSTS ","current post  "+currentPost.getNumOfSeats());
//            binding.tvFrom.setText("SSauga");
//            binding.tvTo.setText("Toronto");
//            binding.tvNumOfSeats.setText("43");
        }
    }
}
