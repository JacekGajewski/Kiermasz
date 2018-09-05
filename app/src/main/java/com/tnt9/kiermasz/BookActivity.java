package com.tnt9.kiermasz;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_add_book)
    Toolbar toolbar;
    @BindView(R.id.book_image)
    ImageView coverImageView;
    @BindView(R.id.book_title)
    TextView titleTextView;
    @BindView(R.id.book_author)
    TextView authorTextView;

    FirebaseFirestore db;
    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        db = FirebaseFirestore.getInstance();

        String title = getIntent().getStringExtra(StaggeredFragment.KEY_BOOK);

        DocumentReference docRef = db.collection("books").document(title);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                book = documentSnapshot.toObject(Book.class);
                updateUI();
            }
        });

    }

    private void updateUI() {
        titleTextView.setText(book.getTitle());
        authorTextView.setText(book.getAuthor());

        Glide.with(this).load(book.getImageURL())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(coverImageView);
    }
}
