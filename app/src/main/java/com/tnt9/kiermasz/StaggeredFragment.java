package com.tnt9.kiermasz;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class StaggeredFragment extends Fragment {

    FirebaseFirestore db;
    StaggeredBookAdapter adapter;
    RecyclerView view;

    public static final String KEY_BOOK = "book_title";

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
            public void onBookClicked(String bookTitle) {
                Intent intent = new Intent(getContext(), BookActivity.class);
                intent.putExtra(KEY_BOOK, bookTitle);
                startActivity(intent);
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
