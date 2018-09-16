package com.tnt9.kiermasz;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class StaggeredFragment extends Fragment {

    FirebaseFirestore db;
    StaggeredBookAdapter adapter;
    RecyclerView view;

    private static final String TAG = StaggeredFragment.class.getSimpleName();
    public static final String KEY_BOOK_TITLE = "book_title";
    public static final String KEY_BOOK_AUTHOR = "book_author";
    public static final String KEY_BOOK_URL = "book_url";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        db = FirebaseFirestore.getInstance();
        view = (RecyclerView) inflater.inflate(R.layout.fragment_staggered,
                container, false);

        Query query = FirebaseFirestore.getInstance()
                .collection("books");

        FirestoreRecyclerOptions<Book> options =
                new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(query, Book.class)
                .build();

        adapter = new StaggeredBookAdapter(options, getActivity());
        adapter.setOnBookClickListener(new StaggeredBookAdapter.onBookClickListener() {
            @Override
            public void onBookClicked(String bookTitle, String bookAuthor, String bookUrl) {
                Intent intent = new Intent(getContext(), BookActivity.class);
                intent.putExtra(KEY_BOOK_TITLE, bookTitle);
                intent.putExtra(KEY_BOOK_AUTHOR, bookAuthor);
                intent.putExtra(KEY_BOOK_URL, bookUrl);
                startActivity(intent);
            }

            @Override
            public void checkboxChecked(String bookTitle, String bookAuthor, String bookUrl) {

//                Map<String, Object> data = new HashMap<>();
//                data.put("user", mAuth.getUid());
//                data.put("book", bookTitle);
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String address = mAuth.getUid() + "_" + bookTitle;
//                db.collection("likes").document(address).set(data)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()){
//                                    Toast.makeText(getContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
//                                }else{
//                                    Toast.makeText(getContext(), "Book was not added", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });

                Book book = new Book(mAuth.getUid(), bookTitle, bookAuthor, bookUrl);
                db.collection("likes").document(address).set(book)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });

            }

            @Override
            public void checkboxUnchecked(String bookTitle) {

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String address = mAuth.getUid() + "_" + bookTitle;

                db.collection("likes").document(address)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        view.setLayoutManager(layoutManager);
        view.setHasFixedSize(true);
        view.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
