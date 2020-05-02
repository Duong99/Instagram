package com.example.instagram.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.Utils;
import com.example.instagram.adapter.adapterfirebase.LVAlbumPictureAdapter;
import com.example.instagram.database.FireDatabase;
import com.example.instagram.model.AlbumPicture;

import java.util.ArrayList;

public class ShowDialogListAlbumPicture implements FireDatabase.ListNameAlbumPicture {
    private Context context;
    private FireDatabase fire;
    private String mUrlPicture;



    public ShowDialogListAlbumPicture(Context context, String urlPicture) {
        this.context = context;
        this.mUrlPicture = urlPicture;
        fire = new FireDatabase(context);
        fire.getNameListAlbum(Utils.getUserIdInstagram());
        fire.setListNameAlbumPicture(ShowDialogListAlbumPicture.this);
    }

    @Override
    public void nameListAlbum(final ArrayList<AlbumPicture> listNameAlbum) {
        final LVAlbumPictureAdapter albumPictureAdapter;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_list_album_picture);

        ListView lv = dialog.findViewById(R.id.lvAlbumPicture);

        albumPictureAdapter = new LVAlbumPictureAdapter(listNameAlbum, context);
        lv.setAdapter(albumPictureAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fire.addListItemAblum(mUrlPicture, listNameAlbum.get(position).getId(), Utils.getUserIdInstagram());
                Toast.makeText(context, "Picture Fire Successfully", Toast.LENGTH_SHORT).show();
                albumPictureAdapter.clearData();
                dialog.cancel();
            }
        });

        dialog.show();
    }
}
