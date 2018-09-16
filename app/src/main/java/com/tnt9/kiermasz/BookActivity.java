package com.tnt9.kiermasz;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_add_book)
    Toolbar toolbar;
    @BindView(R.id.book_image)
    ImageView coverImageView;
    @BindView(R.id.book_title)
    TextView titleTextView;
    @BindView(R.id.book_author)
    TextView authorTextView;
    @BindView(R.id.book_checkbox)
    CheckBox checkBox;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    private Book book;
    private String bookTitle;
    private String bookAuthor;
    private String bookUrl;

    private static final String TAG = BookActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);


        db = FirebaseFirestore.getInstance();
        bookTitle = getIntent().getStringExtra(StaggeredFragment.KEY_BOOK_TITLE);
        bookAuthor = getIntent().getStringExtra(StaggeredFragment.KEY_BOOK_AUTHOR);
        bookUrl = getIntent().getStringExtra(StaggeredFragment.KEY_BOOK_URL);


        DocumentReference docRef = db.collection("books").document(bookTitle);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                book = documentSnapshot.toObject(Book.class);
                updateUI();
            }
        });


        mAuth = FirebaseAuth.getInstance();
        db.collection("users")
                .document(Objects.requireNonNull(mAuth.getUid()))
                .collection("watchlist")
                .whereEqualTo("title", bookTitle)
                .whereEqualTo("watchlist", 1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() != 0)
                            checkBox.setChecked(true);
                    }
                });
    }

    @OnClick(R.id.book_checkbox)
    public void checkboxChanged(CheckBox checkBox){

        if (checkBox.isChecked()){

            String address = mAuth.getUid() + "_" + bookTitle;
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


            Map<String, Object> data = new HashMap<>();
            data.put("title", bookTitle);
            data.put("watchlist", 1);

            db.collection("users")
                    .document(Objects.requireNonNull(mAuth.getUid()))
                    .collection("watchlist")
                    .document(bookTitle)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Success adding document");

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });


        }else {

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

            db.collection("users")
                    .document(Objects.requireNonNull(mAuth.getUid()))
                    .collection("watchlist")
                    .document(bookTitle)
                    .update("watchlist", 0)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });

        }

    }
    private void updateUI() {
        titleTextView.setText(book.getTitle());
        authorTextView.setText(book.getAuthor());

        Glide.with(this).load(book.getImageURL())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(coverImageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
