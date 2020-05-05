package com.example.instagram.database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.instagram.Utils;
import com.example.instagram.model.AlbumPicture;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FireDatabase {
    private final static String LISTALBUM = "list_Album";
    private final static String NAMEALBUM = "name";
    private final static String LIST = "list";

    private DatabaseReference mData;
    private Context context;

    public FireDatabase(Context context) {
        this.context = context;
        this.mData = FirebaseDatabase.getInstance().getReference();
    }

    public void deleteNameAlbum(String idPushAlbumDelete){
        mData = FirebaseDatabase.getInstance().getReference(Utils.getUserIdInstagram() + "/" + LISTALBUM );
        mData.child(idPushAlbumDelete).removeValue();
    }

    public void deletePicture(String idChildrenDelete, String idPushAlbum){
        mData = FirebaseDatabase.getInstance()
                .getReference(Utils.getUserIdInstagram() + "/" + LISTALBUM + "/" + idPushAlbum + "/" + LIST);
        mData.child(idChildrenDelete).removeValue();
    }

    public void createAblumPicutre(String nameAlbum){
        mData = FirebaseDatabase.getInstance().getReference(Utils.getUserIdInstagram() + "/" + LISTALBUM);
        mData.push().child(NAMEALBUM).setValue(nameAlbum);
    }

    public void addListItemAblum(String url, String idPush, String idUser){
        mData = FirebaseDatabase.getInstance().getReference(idUser + "/" + LISTALBUM + "/" + idPush + "/");
        mData.child(LIST).push().setValue(url);
    }

    public void getNameListAlbum(String idUser){
        final ArrayList<AlbumPicture> listNameAlbum = new ArrayList<>();
        mData = FirebaseDatabase.getInstance().getReference(idUser).child(LISTALBUM);
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    listNameAlbum.add(new AlbumPicture(String.valueOf(dataSnapshot1.getKey().toString()),
                            String.valueOf(dataSnapshot1.child(NAMEALBUM).getValue().toString())));
                }
                mListNameAlbumPicture.nameListAlbum(listNameAlbum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("AAA", "onDataChange:  cancel");
            }
        });

    }

    public void getItemAlbum(String iduser, String idPushAlbum){
        final ArrayList<AlbumPicture> listUrlPicture = new ArrayList<>();
        mData = FirebaseDatabase.getInstance().getReference(iduser + "/" + LISTALBUM + "/" + idPushAlbum + "/" + LIST);
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    listUrlPicture.add(new AlbumPicture(String.valueOf(dataSnapshot1.getKey().toString()),
                            String.valueOf(dataSnapshot1.getValue().toString())));
                }
                mListUrlPictureDB.listPicture(listUrlPicture);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ListNameAlbumPicture mListNameAlbumPicture;
    public interface ListNameAlbumPicture{
        void nameListAlbum(ArrayList<AlbumPicture> listNameAlbum);
    }

    public void setListNameAlbumPicture(ListNameAlbumPicture listNameAlbumPicture){
        this.mListNameAlbumPicture = listNameAlbumPicture;
    }

    private ListUrlPictureDB mListUrlPictureDB;

    public interface ListUrlPictureDB{
        void listPicture(ArrayList<AlbumPicture> listUrlPicture);
    }

    public void setListUrlPictureDB(ListUrlPictureDB listUrlPictureDB){
        this.mListUrlPictureDB = listUrlPictureDB;
    }
}
