package com.example.workrideshare.repositories;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.workrideshare.models.Post;
import com.example.workrideshare.viewmodels.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostRepository {
    private final String TAG = this.getClass().getCanonicalName();
    private final FirebaseFirestore DB;
    private final String COLLECTION_POSTS = "Posts";
    private final String FIELD_CONTACT = "contact";
    private final String FIELD_DEST = "toDest";
    private final String FIELD_FROM = "fromDest";
    private final String FIELD_NUMSEATS = "numOfSeats";
    private final String FIELD_CITY = "postCity";
    private final String FIELD_USERID = "userID";
    private final String FIELD_DATACREATED = "dateCreated";
    private final String FIELD_DESC = "desc";
    public MutableLiveData<List<Post>> allPosts = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Post>> myPostsFromDB = new MutableLiveData<>();

    public PostRepository() {
        DB = FirebaseFirestore.getInstance();
    }

    public void addPost(Post newPost){
        try{
            newPost.setDateCreated(Timestamp.now());
//            Storing the Unique ID as PostID
            DocumentReference  newPostRefpost = DB.collection(COLLECTION_POSTS).document();
            newPost.setPostID(newPostRefpost.getId());
            //create subcollections containing documents
            newPostRefpost.set(newPost);
        }catch (Exception ex){
            Log.e(TAG, "addFriend: " + ex.getLocalizedMessage());
        }
    }

    public void getMyPosts(String userID){
        myPostsFromDB = new MutableLiveData<>();
        try{
            DB.collection(COLLECTION_POSTS)
                    .whereEqualTo(FIELD_USERID, userID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        ArrayList<Post> postList = new ArrayList<>();
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            Post city = documentSnapshot.toObject(City.class);
                            if (task.isSuccessful()){
                                Log.d(TAG, "onComplete: Post found " + task.getResult());
                                Log.d(TAG, "onComplete: Post found " + task.getResult().getDocuments());
                                Log.d(TAG, "onComplete: Post found " + userID);
                                if (task.getResult().getDocuments().size() != 0){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Logging the ID of your desired document & the document data itself
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        //matching Post found
                                        Post matchedPost = new Post();
                                        matchedPost = document.toObject(Post.class);
                                        matchedPost.setPostID(document.getId());
                                        Log.e(TAG, "onComplete: Post found " + matchedPost.toString());
                                        postList.add(matchedPost);
                                    }
                                    if (postList != null){
//                                        myPostsFromDB.postValue(postList);
                                        myPostsFromDB.setValue(postList);

                                    }else{
                                        Log.e(TAG, "onComplete: Unable to convert the matching document to Post object");
                                    }
                                }else{
                                    Log.e(TAG, "onComplete: No post with give name found");
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Unable to find posts with name " + userID + e.getLocalizedMessage() );
                        }
                    });
        }catch(Exception ex){
            Log.e(TAG, "Exception occurred: " + ex.getLocalizedMessage() );
            Log.e(TAG, "onFailure: Unable to find post with name " + userID + ex.getLocalizedMessage() );
        }
    }

    public void getAllPosts(String cityName){
        allPosts = new MutableLiveData<>();
        Log.e(TAG, "GET ALL POST ENTERED");
        try{
            DB.collection(COLLECTION_POSTS)
                    .whereEqualTo(FIELD_CITY, cityName)
                    .orderBy(FIELD_DATACREATED, Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }

                            List<Post> posts = new ArrayList<>();

                            if (value != null){
                                Log.d(TAG, "onEvent: Current Changes " + value.getDocumentChanges());

                                for (DocumentChange documentChange: value.getDocumentChanges()){

                                    Post currentPost = documentChange.getDocument().toObject(Post.class);
                                    currentPost.setPostID(documentChange.getDocument().getId());
                                    Log.d(TAG, "onEvent: currentUser : " + currentPost.toString());

                                    switch (documentChange.getType()){
                                        case ADDED:
                                            posts.add(currentPost);
                                            break;
                                        case MODIFIED:
                                            //TODO - search in friendList for existing object and replace it with new one - currentFriend
                                            break;
                                        case REMOVED:
                                            posts.remove(currentPost);
                                            break;
                                    }
                                }

                                allPosts.postValue(posts);
                                Log.e(TAG, "ALL DBPOST: "+allPosts.getValue());
                            }else{
                                Log.e(TAG, "onEvent: No changes received");
                            }
                            Log.d(TAG, "Current cites in CA: " + cityName);
                        }

                    });
        }catch(Exception ex){
            Log.e(TAG, "Exception occurred: " + ex.getLocalizedMessage() );
            Log.e(TAG, "onFailure: Unable to find post with City name " + cityName + ex.getLocalizedMessage() );
        }
    }

    public void deletePost(Post post){
        try{
            DB.collection(COLLECTION_POSTS)
                    .document(post.getPostID())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: Document successfully deleted");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Unable to delete document" + e.getLocalizedMessage());
                        }
                    });

        }catch(Exception ex){
            Log.e(TAG, "deleteFriend: Exception occured " + ex.getLocalizedMessage() );
        }
    }

    public void updatePost(Post updatedPost){
        Log.e(TAG, "onSuccess: Undating Post with ID "+updatedPost.getPostID());
        try {
            DB.collection(COLLECTION_POSTS)
                    .document(updatedPost.getPostID())
                    .update(FIELD_NUMSEATS, updatedPost.getNumOfSeats())
//                    .update(updatedInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: Document successfully updated");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Unable to update document" + e.getLocalizedMessage());
                        }
                    });
        }catch(Exception ex){
            Log.e(TAG, "updateUser: Exception occurred " + ex.getLocalizedMessage() );
        }
    }

}
