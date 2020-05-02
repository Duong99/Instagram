package com.example.instagram.adapter.adaptermusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.model.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterSongLove extends RecyclerView.Adapter<AdapterSongLove.Viewholder>{
    private Context context;
    private ArrayList<Song> songs;

    public AdapterSongLove(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_reycler_song, null);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        if (songs.get(position).getAvatar() == null){
            Picasso.with(context).load("https://stc-m.nixcdn.com/touch_v2/images/img-plist-full.jpg")
                    .error(R.drawable.topic_loading).into(holder.imv);
        }else {
            Picasso.with(context).load(songs.get(position).getAvatar()).error(R.drawable.img_plist_full).into(holder.imv);
            holder.title.setText(songs.get(position).getTitle());
            holder.singer.setText(songs.get(position).getSinger());
        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ImageView imv, imvHeart, imvDownloadMusic;
        TextView title, singer;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imv = itemView.findViewById(R.id.imvSong);
            title = itemView.findViewById(R.id.txtTitleSong);
            singer = itemView.findViewById(R.id.txtSingerSong);
            imvHeart = itemView.findViewById(R.id.imvHeart);
            imvDownloadMusic = itemView.findViewById(R.id.imvDownloadMusic);

            imvDownloadMusic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        onClickSong.downMusic(songs.get(position).getLocation());
                    }
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        onClickSong.onCickPlayListenSong(position, songs);
                    }
                }
            });

            imvHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        onClickSong.onClickNotLikeSong(songs.get(position).getSongKey());
                    }
                }
            });
        }
    }
    private OnClickPlayListenSong onClickSong;

    public interface OnClickPlayListenSong{
        void onCickPlayListenSong(int position, ArrayList<Song> songs);
        void onClickNotLikeSong(String key);
        void downMusic(String url);
    }

    public void setOnClickSong(OnClickPlayListenSong onClickSong){
        this.onClickSong = onClickSong;
    }

}