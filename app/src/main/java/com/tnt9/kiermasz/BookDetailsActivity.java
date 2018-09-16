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

public class BookDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_add_book) Toolbar toolbar;
    @BindView(R.id.image_book_details_cover) ImageView coverImageView;
    @BindView(R.id.text_book_details_title) TextView titleTextView;
    @BindView(R.id.text_book_details_author) TextView authorTextView;
    @BindView(R.id.checkbox_book_details_watchlist) CheckBox checkBox;

    private static final String TAG = BookDetailsActivity.class.getSimpleName();

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth;

    private Book mBook;
    private String mBookTitle;
    private String mBookAuthor;
    private String mBookUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        mFirestore = FirebaseFirestore.getInstance();
        mBookTitle = getIntent().getStringExtra(HomeFragment.EXTRA_BOOK_TITLE);
        mBookAuthor = getIntent().getStringExtra(HomeFragment.EXTRA_BOOK_AUTHOR);
        mBookUrl = getIntent().getStringExtra(HomeFragment.EXTRA_BOOK_URL);

        DocumentReference docRef = mFirestore.collection("books").document(mBookTitle);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mBook = documentSnapshot.toObject(Book.class);
                updateUI();
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestore.collection("users")
                .document(Objects.requireNonNull(mFirebaseAuth.getUid()))
                .collection("watchlist")
                .whereEqualTo("title", mBookTitle)
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

    @OnClick(R.id.checkbox_book_details_watchlist)
    public void checkboxChanged(CheckBox checkBox){

        if (checkBox.isChecked()){

            String address = mFirebaseAuth.getUid() + "_" + mBookTitle;
            Book book = new Book(mFirebaseAuth.getUid(), mBookTitle, mBookAuthor, mBookUrl);
            mFirestore.collection("likes").document(address).set(book)
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
            data.put("title", mBookTitle);
            data.put("watchlist", 1);

            mFirestore.collection("users")
                    .document(Objects.requireNonNull(mFirebaseAuth.getUid()))
                    .collection("watchlist")
                    .document(mBookTitle)
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

            String address = mFirebaseAuth.getUid() + "_" + mBookTitle;

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

            mFirestore.collection("users")
                    .document(Objects.requireNonNull(mFirebaseAuth.getUid()))
                    .collection("watchlist")
                    .document(mBookTitle)
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
        titleTextView.setText(mBook.getTitle());
        authorTextView.setText(mBook.getAuthor());

        Glide.with(this).load(mBook.getImageURL())
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
