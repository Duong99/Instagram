package com.example.instagram.adapter.adaptermusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.model.Theme;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterTheme extends RecyclerView.Adapter<AdapterTheme.Viewhodler>{
    private Context context;
    private ArrayList<Theme> themes;

    public AdapterTheme(Context context, ArrayList<Theme> themes) {
        this.context = context;
        this.themes = themes;
    }

    @NonNull
    @Override
    public Viewhodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_recycler_theme, null);
        return new Viewhodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewhodler holder, int position) {
        Picasso.with(context).load(themes.get(position).getImvTheme()).error(R.drawable.topic_loading).into(holder.imvTheme);
    }

    @Override
    public int getItemCount() {
        return themes.size();
    }

    public class Viewhodler extends RecyclerView.ViewHolder{
        private ImageView imvTheme;
        public Viewhodler(@NonNull View itemView) {
            super(itemView);

            imvTheme = itemView.findViewById(R.id.imvTheme);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positon = getAdapterPosition();
                    if (positon != RecyclerView.NO_POSITION){
                        onClickTheme.onClickTheme(themes.get(positon).getUrlHTMLTheme());
                    }
                }
            });
        }
    }

    private OnClickTheme onClickTheme;

    public interface OnClickTheme{
        void onClickTheme(String urlHTMLTheme);
    }

    public void setOnClickTheme(OnClickTheme onClickTheme){
        this.onClickTheme = onClickTheme;
    }
}
