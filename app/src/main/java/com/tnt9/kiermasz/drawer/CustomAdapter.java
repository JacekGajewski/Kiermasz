package com.tnt9.kiermasz.drawer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CustomAdapter extends FirestoreRecyclerAdapter<Book, CustomAdapter.BookHolder>{


    public CustomAdapter(@NonNull FirestoreRecyclerOptions<Book> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BookHolder holder, int position, @NonNull Book model) {

    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    public class BookHolder extends RecyclerView.ViewHolder{

        public BookHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
