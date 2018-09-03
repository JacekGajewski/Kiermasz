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
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_add_book) Toolbar toolbar;
    @BindView(R.id.input_layout_add_book_title) TextInputLayout titleInputLayout;
    @BindView(R.id.input_layout_add_book_author) TextInputLayout authorInputLayout;
    @BindView(R.id.edit_text_add_book_title) TextInputEditText titleInputEditText;
    @BindView(R.id.edit_text_add_book_author) TextInputEditText authorInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);






    }
}
