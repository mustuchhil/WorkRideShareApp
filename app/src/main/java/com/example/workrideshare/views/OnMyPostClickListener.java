package com.example.workrideshare.views;

import com.example.workrideshare.models.Post;

public interface OnMyPostClickListener {
    void OnMyPostItemClicked(Post post);

    void DeleteMyPostItemClicked(Post post);
}
