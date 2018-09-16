package com.tnt9.kiermasz;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

public class AddBookActivity extends AppCompatActivity {


    @BindView(R.id.image_add_book_cover) ImageView imageAddBookCover;
    @BindView(R.id.edit_add_book_title) TextInputEditText editTextAddBookTitle;
    @BindView(R.id.edit_add_book_author) TextInputEditText editTextAddBookAuthor;
    @BindView(R.id.edit_add_book_image_url) TextInputEditText editTextAddBookImageURL;
    @BindView(R.id.edit_add_book_publisher) TextInputEditText editTextAddBookPublisher;
    @BindView(R.id.edit_add_book_date_published) TextInputEditText editTextAddBookDatePublished;
    @BindView(R.id.edit_add_book_pages) TextInputEditText editTextAddBookPages;
    @BindView(R.id.edit_add_book_date_isbn) TextInputEditText editTextAddBookDateIsbn;
    @BindView(R.id.edit_add_book_date_language) TextInputEditText editTextAddBookDateLanguage;
    @BindView(R.id.edit_add_book_description) TextInputEditText editTextAddBookDescription;
    @BindView(R.id.card_add_book) CardView cardAddBookBook;
    @BindView(R.id.card_add_book_upload) CardView cardAddBookUpload;

    private static final String TAG = AddBookActivity.class.getSimpleName();
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        ButterKnife.bind(this);
        mFirestore = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar_bottom_navigation);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
    }


    @OnClick(R.id.card_add_book_upload)
    public void onCardAddBookUploadClicked() {

        if (TextUtils.isEmpty(editTextAddBookTitle.getText())) {
            editTextAddBookTitle.setError("Enter correct title");

        } else if (TextUtils.isEmpty(editTextAddBookAuthor.getText())) {
            editTextAddBookAuthor.setError("Enter correct author");

        } else if (!TextUtils.isEmpty(editTextAddBookImageURL.getText())){
            String title = editTextAddBookTitle.getText().toString();

            Bitmap bitmap = ((BitmapDrawable) imageAddBookCover.getDrawable().getCurrent()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 150, baos);
            byte[] data = baos.toByteArray();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final StorageReference mountainsRef = storageRef.child(title + ".jpg");
            StorageReference mountainImagesRef = storageRef.child("images/" + title + ".jpg");

            // While the file names are the same, the references point to different files
            mountainsRef.getName().equals(mountainImagesRef.getName());    // true
            mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false
            UploadTask uploadTask = mountainsRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.v(TAG, "OK");
                    }
                }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }

                        // Continue with the task to get the download URL
                        return mountainsRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            uploadData(downloadUri.toString());
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });
            }else uploadData("");
    }

    @OnFocusChange(R.id.edit_add_book_image_url)
    public void editPublisherFocusChanged(boolean hasFocus){
        if (!hasFocus) {
            String urlNavHeaderBg = Objects.requireNonNull(editTextAddBookImageURL.getText()).toString();
            Glide.with(this).load(urlNavHeaderBg)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageAddBookCover);
        }
    }

    private void uploadData(String url){
        String title = Objects.requireNonNull(editTextAddBookTitle.getText()).toString();
        String author = Objects.requireNonNull(editTextAddBookAuthor.getText()).toString();
        String publisher = Objects.requireNonNull(editTextAddBookPublisher.getText()).toString();
        String date = Objects.requireNonNull(editTextAddBookDatePublished.getText()).toString();
        String numberOfPages = Objects.requireNonNull(editTextAddBookPages.getText()).toString();
        String ISBN = Objects.requireNonNull(editTextAddBookDateIsbn.getText()).toString();
        String language = Objects.requireNonNull(editTextAddBookDateLanguage.getText()).toString();
        String description = Objects.requireNonNull(editTextAddBookDescription.getText()).toString();

        Book book = new Book(title, author, url, publisher, date, numberOfPages, ISBN, language, description);
        mFirestore.collection("books").document(title).set(book)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Book was not added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
