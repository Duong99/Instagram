package com.example.instagram.main.mainfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.Utils;
import com.example.instagram.adapter.adapterfirebase.LVAlbumPictureAdapter;
import com.example.instagram.database.FireDatabase;
import com.example.instagram.model.AlbumPicture;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AlbumPictureActivity extends AppCompatActivity implements FireDatabase.ListNameAlbumPicture {

    private ListView lvAlbum;
    private LVAlbumPictureAdapter albumPictureAdapter;
    private FireDatabase fire;
    private FloatingActionButton btnCreateAlbum;
    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_album_picture);

        setTitle("Picture Firebase");

        final Intent intent = getIntent();
        idUser = intent.getStringExtra("idInstagram");

        lvAlbum = findViewById(R.id.lvAlbum);
        btnCreateAlbum = findViewById(R.id.btnCreateAlbum);

        fire = new FireDatabase(this);
        fire.getNameListAlbum(idUser);
        fire.setListNameAlbumPicture(AlbumPictureActivity.this);

        btnCreateAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idUser.equals(Utils.getUserIdInstagram())) {
                    final Dialog dialog = new Dialog(AlbumPictureActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.diaglog_create_album);

                    final EditText edt = dialog.findViewById(R.id.edtCreateNameAlbum);
                    Button btnCancel = dialog.findViewById(R.id.btnCancelCreateAlbum);
                    Button btnCreate = dialog.findViewById(R.id.btnCreateNameAlbum);

                    btnCreate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (edt.getText().length() == 0) {
                                Toast.makeText(AlbumPictureActivity.this,
                                        "Please !!! Enter name album", Toast.LENGTH_SHORT).show();
                            } else {
                                albumPictureAdapter.clearData();
                                lvAlbum.setAdapter(albumPictureAdapter);
                                fire.createAblumPicutre(edt.getText().toString());
                                //fire.getNameListAlbum(idUser);
                                dialog.cancel();
                            }
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });


                    dialog.show();
                } else {
                    Toast.makeText(AlbumPictureActivity.this,
                            "You can't create because this album not your", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void nameListAlbum(final ArrayList<AlbumPicture> listNameAlbum) {
        albumPictureAdapter = new LVAlbumPictureAdapter(listNameAlbum, this);
        lvAlbum.setAdapter(albumPictureAdapter);

        lvAlbum.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (idUser.equals(Utils.getUserIdInstagram())){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(AlbumPictureActivity.this);
                    dialog.setTitle("Warring");
                    dialog.setMessage("You want delete album " + listNameAlbum.get(position).getName());

                    dialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fire.deleteNameAlbum(listNameAlbum.get(position).getId());
                            albumPictureAdapter.clearData();
                        }
                    });

                    dialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
                return true;
            }
        });


        lvAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(AlbumPictureActivity.this, PictureFireActivity.class);
                intent1.putExtra("idInstagram", idUser);
                intent1.putExtra("idPushAlbum", listNameAlbum.get(position).getId());
                startActivity(intent1);
            }
        });


    }
}
