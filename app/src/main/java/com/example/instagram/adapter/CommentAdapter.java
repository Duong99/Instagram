package com.example.instagram.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.model.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Viewhoder>{
    private Context context;
    private ArrayList<Comment> comments;

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public Viewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_recycler_comment, null);
        return new Viewhoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewhoder holder, int position) {
        holder.txtTextComment.setText(comments.get(position).getText());
        holder.txtUser.setText(comments.get(position).getPerson().getUsername());
        holder.txtTotalLike.setText(comments.get(position).getTotalLike());

        Picasso.with(context).load(comments.get(position).getPerson().getImvPeron()).into(holder.imvUser);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class Viewhoder extends RecyclerView.ViewHolder{
        CircleImageView imvUser;
        ImageView imvHeart;
        TextView txtUser, txtTextComment, txtTotalLike;
        public Viewhoder(@NonNull View itemView) {
            super(itemView);
            imvHeart = itemView.findViewById(R.id.imvLikeComment);
            imvUser = itemView.findViewById(R.id.imvComment);
            txtUser = itemView.findViewById(R.id.txtUsernameComment);
            txtTextComment = itemView.findViewById(R.id.txtTextComment);
            txtTotalLike = itemView.findViewById(R.id.txtTotalLikeComment);
        }
    }
}
