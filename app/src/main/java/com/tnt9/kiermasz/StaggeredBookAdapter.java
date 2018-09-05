package com.tnt9.kiermasz;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StaggeredBookAdapter extends FirestoreRecyclerAdapter<Book, StaggeredBookAdapter.BookHolder>{

    private Activity activity;
    StaggeredBookAdapter(@NonNull FirestoreRecyclerOptions<Book> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    public interface onBookClickListener{
        void onBookClicked(String bookTitle);
    }

    private onBookClickListener onBookClickListener;

    public void setOnBookClickListener(StaggeredBookAdapter.onBookClickListener onBookClickListener) {
        this.onBookClickListener = onBookClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull BookHolder holder, int position, @NonNull Book model) {
        final String title = (model.getTitle());
        holder.titleTextView.setText(title);
        holder.authorTextView.setText(model.getAuthor());
        Glide.with(activity).load(model.getImageURL())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.coverImageView);

        holder.bookCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onBookClickListener != null)
                onBookClickListener.onBookClicked(title);
            }
        });

    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card,
                viewGroup, false);
        return new BookHolder(view);
    }

    public class BookHolder extends RecyclerView.ViewHolder{

//        @BindView(R.id.image_book_holder_cover)
//        ImageView coverImageView;
//        @BindView(R.id.text_book_holder_title)
//        TextView titleTextView;
//        @BindView(R.id.text_book_holder_author)
//        TextView authorTextView;
        @BindView(R.id.card_view)
        CardView bookCardView;
        @BindView(R.id.book_image)
        ImageView coverImageView;
        @BindView(R.id.book_title)
        TextView titleTextView;
        @BindView(R.id.book_author)
        TextView authorTextView;

        BookHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
