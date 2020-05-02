package com.example.instagram.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.adapter.CommentAdapter;
import com.example.instagram.model.Comment;

import java.util.ArrayList;

public class ShowRecyclerComment {
    private CommentAdapter commentAdapter;

    public ShowRecyclerComment(Context context, ArrayList<Comment> comments) {

        commentAdapter = new CommentAdapter(context, comments);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.item_list_people_likes_comments);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerPeopleLike);
        TextView txtTitle = dialog.findViewById(R.id.txtTitleLikeComment);
        ImageButton imvCancel = dialog.findViewById(R.id.imvbtnClosePeopleLike);

        txtTitle.setText("Comments");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(commentAdapter);

        imvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
