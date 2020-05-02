package com.example.instagram.adapter.adapterfirebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.instagram.R;
import com.example.instagram.model.AlbumPicture;

import java.util.ArrayList;

public class LVAlbumPictureAdapter extends BaseAdapter {
    private ArrayList<AlbumPicture> listNamePicture;
    private Context context;

    public LVAlbumPictureAdapter(ArrayList<AlbumPicture> listNamePicture, Context context) {
        this.listNamePicture = listNamePicture;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listNamePicture.size();
    }

    @Override
    public Object getItem(int position) {
        return listNamePicture.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_lv_list_ablum_picture, null);
        }

        TextView txtNameListAlbumPicture = view.findViewById(R.id.txtNameListAlbumPicture);
        txtNameListAlbumPicture.setText(listNamePicture.get(position).getName());

        return view;
    }

    public void clearData(){
        listNamePicture.clear();
    }
}
