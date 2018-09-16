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

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class LibraryFragment extends Fragment {

    private static final String TAG = LibraryFragment.class.getSimpleName();
    public static final String EXTRA_BOOK_TITLE = "com.tnt9.kiermasz.BOOK_TITLE";
    public static final String EXTRA_BOOK_AUTHOR = "com.tnt9.kiermasz.BOOK_AUTHOR";
    public static final String EXTRA_BOOK_URL = "com.tnt9.kiermasz.BOOK_URL";

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth;
    private HomeFragmentAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_staggered,
                container, false);

        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        Query query = FirebaseFirestore.getInstance()
                .collection("likes")
                .whereEqualTo("userId", mFirebaseAuth.getUid());

        FirestoreRecyclerOptions<Book> options =
                new FirestoreRecyclerOptions.Builder<Book>()
                        .setQuery(query, Book.class)
                        .build();

        mAdapter = new HomeFragmentAdapter(options, getActivity());
        mAdapter.setOnBookClickListener(new HomeFragmentAdapter.onBookClickListener() {
            @Override
            public void onBookClicked(String bookTitle, String bookAuthor, String bookUrl) {
                Intent intent = new Intent(getContext(), BookDetailsActivity.class);
                intent.putExtra(EXTRA_BOOK_TITLE, bookTitle);
                intent.putExtra(EXTRA_BOOK_AUTHOR, bookAuthor);
                intent.putExtra(EXTRA_BOOK_URL, bookUrl);
                startActivity(intent);
            }

            @Override
            public void checkboxChecked(String bookTitle, String bookAuthor, String bookUrl) {

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String address = mAuth.getUid() + "_" + bookTitle;

                mFirestore.collection("likes").document(address)
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

            @Override
            public void checkboxUnchecked(String bookTitle) {

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String address = mAuth.getUid() + "_" + bookTitle;

                mFirestore.collection("likes").document(address)
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
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        return recyclerView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

}
