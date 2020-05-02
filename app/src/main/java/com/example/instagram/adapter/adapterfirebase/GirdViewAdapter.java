package com.example.instagram.adapter.adapterfirebase;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.instagram.R;
import com.example.instagram.model.AlbumPicture;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GirdViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AlbumPicture> listPicture;

    public GirdViewAdapter(Context context, ArrayList<AlbumPicture> listPicture) {
        this.context = context;
        this.listPicture = listPicture;
    }

    @Override
    public int getCount() {
        return listPicture.size();
    }

    @Override
    public Object getItem(int position) {
        return listPicture.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.girdview_picuter_fire, null);
        }

        ImageView imv = convertView.findViewById(R.id.imvItemGirdView);

        Picasso.with(context).load(listPicture.get(position).getName()).into(imv);

        return convertView;
    }

    public void clearListData(){
        listPicture.clear();
    }
}
