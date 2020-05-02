package com.example.instagram.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.main.WatchImageActivity;
import com.example.instagram.model.Picture;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.Viewholder>{
    private Context context;
    private ArrayList<Picture> pictures;

    public PictureAdapter(Context context, ArrayList<Picture> pictures) {
        this.context = context;
        this.pictures = pictures;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_recycler_home, null);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Picasso.with(context).load(pictures.get(position).getDisplay_url())
                .error(R.drawable.picture_profile_default).into(holder.imvPersonal);

        if (Integer.parseInt(pictures.get(position).getEdge_media_to_comment()) > 1){
            holder.txtTotalComents.setText(String.format("%s %s",
                    pictures.get(position).getEdge_media_to_comment(), context.getResources().getString(R.string.comments)));
        }else {
            holder.txtTotalComents.setText(String.format("%s %s",
                    pictures.get(position).getEdge_media_to_comment(), context.getResources().getString(R.string.comment)));
        }

        if (Integer.parseInt(pictures.get(position).getEdge_liked_by_count()) > 1){
            holder.txtTotalLike.setText(String.format("%s %s",
                    pictures.get(position).getEdge_liked_by_count(), context.getResources().getString(R.string.likes)));
        }else {
            holder.txtTotalLike.setText(String.format("%s %s",
                    pictures.get(position).getEdge_liked_by_count(), context.getResources().getString(R.string.like)));
        }
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        private ImageView imvPersonal;
        private TextView txtTotalLike;
        private TextView txtTotalComents;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            imvPersonal = itemView.findViewById(R.id.imvPostPersonalHome);
            txtTotalLike = itemView.findViewById(R.id.txtTotalLikePostPersonal);
            txtTotalComents = itemView.findViewById(R.id.txtTotalComments);

            imvPersonal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        mOnClickImageHome.onClickImage(position, pictures);
                    }
                }
            });

            imvPersonal.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        mOnClickImageHome.onClickLongPicture(position, pictures);
                    }
                    return false;
                }
            });

            txtTotalLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        mOnClickImageHome.onClickLike(pictures.get(position).getShortcode(), pictures);
                    }
                }
            });

            txtTotalComents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        mOnClickComment.onClickComent(pictures.get(position).getShortcode());
                    }
                }
            });
        }
    }
    private OnClickComment mOnClickComment;

    public interface OnClickComment{
        void onClickComent(String shortcode);
    }

    public void setOnClickComment(OnClickComment onClickComment){
        this.mOnClickComment = onClickComment;
    }

    private OnClickImageHome mOnClickImageHome;

    public interface OnClickImageHome{
        void onClickImage(int position, ArrayList<Picture> pictures);

        void onClickLike(String shortcode, ArrayList<Picture> pictures);

        void onClickLongPicture(int position, ArrayList<Picture> pictures);
    }

    public void setOnClickImageHome(OnClickImageHome onClickImageHome){
        this.mOnClickImageHome = onClickImageHome;
    }
}
