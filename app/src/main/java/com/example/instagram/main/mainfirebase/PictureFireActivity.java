package com.example.instagram.main.mainfirebase;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instagram.Utils;
import com.example.instagram.database.FireDatabase;
import com.example.instagram.R;
import com.example.instagram.adapter.adapterfirebase.GirdViewAdapter;
import com.example.instagram.model.AlbumPicture;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class PictureFireActivity extends AppCompatActivity implements FireDatabase.ListUrlPictureDB {

    private final static int CODE_CAMERA = 347;
    private final static int CODE_FOLDER = 245;

    private GridView grirdViewPictureFire;
    private FireDatabase mydb;
    private GirdViewAdapter adapter;
    private String idUser, idPushAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_fire);

        grirdViewPictureFire = findViewById(R.id.grirdViewPictureFire);

        Intent intent = getIntent();
        idUser = intent.getStringExtra("idInstagram");
        idPushAlbum = intent.getStringExtra("idPushAlbum");

        mydb = new FireDatabase(this);
        mydb.getItemAlbum(idUser, idPushAlbum);
        mydb.setListUrlPictureDB(PictureFireActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Utils.getUserIdInstagram().equals(idUser)){
            getMenuInflater().inflate(R.menu.menu_picture, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_menu_camera:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CODE_CAMERA);
                break;
            case R.id.item_menu_folder:
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, CODE_FOLDER);
                break;
                default:break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CODE_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            savePictureFromCameraAndFolder(bitmap);
        }

        if (requestCode == CODE_FOLDER && resultCode == RESULT_OK && data != null){
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                savePictureFromCameraAndFolder(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void savePictureFromCameraAndFolder(Bitmap bitmap) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_get_image_save_firebase);

        final ImageView imv = dialog.findViewById(R.id.imgSavePicture);
        Button btnCancel = dialog.findViewById(R.id.btnCancelSavePicture);
        Button btnSave = dialog.findViewById(R.id.btnSavePicture);

        imv.setImageBitmap(bitmap);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                // Create a storage reference from our app
                StorageReference storageRef = storage.getReference();

                final Calendar calendar = Calendar.getInstance();

                // Create a reference to "mountains.jpg"
                final String pathfolder = Utils.getUserIdInstagram() + "/" ;
                final String path = calendar.getTimeInMillis()+".png";
                StorageReference mountainsRef = storageRef.child(pathfolder + path);

                // Get the data from an ImageView as bytes
                imv.setDrawingCacheEnabled(true);
                imv.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imv.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                final byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(PictureFireActivity.this, "Save unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        final Uri uri = taskSnapshot.getUploadSessionUri();

                        final String url = String.valueOf(uri).substring(0, String.valueOf(uri).indexOf("&uploadType"));

                        AsyncHttpClient client = new AsyncHttpClient();
                        client.get(url, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                try {
                                    JSONObject object = new JSONObject(new String(responseBody));
                                    if (object.toString() != null){

                                        String downloadTokens = "?alt=media&token="  +object.getString("downloadTokens");
                                        if (downloadTokens != null){
                                            String picutreUrl = "https://firebasestorage.googleapis.com/v0/b/instagram-f6f9b.appspot.com/o/"+
                                                    Utils.getUserIdInstagram() + "%2F" + path + downloadTokens;

                                            mydb.addListItemAblum(picutreUrl, idPushAlbum, Utils.getUserIdInstagram());
                                            adapter.clearListData();
                                            Toast.makeText(PictureFireActivity.this, "Save successfully", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        });
                    }
                });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }


    @Override
    public void listPicture(final ArrayList<AlbumPicture> listUrlPicture) {
        adapter = new GirdViewAdapter(this, listUrlPicture);
        grirdViewPictureFire.setAdapter(adapter);

        grirdViewPictureFire.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (idUser.equals(Utils.getUserIdInstagram())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(PictureFireActivity.this);
                    builder.setTitle("Warring");
                    builder.setMessage("You want delete this picture ?");
                    builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new FireDatabase(PictureFireActivity.this)
                                    .deletePicture(listUrlPicture.get(position).getId(),idPushAlbum);
                            adapter.clearListData();
                        }
                    });

                    builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
                return true;
            }
        });

        grirdViewPictureFire.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> list = new ArrayList<>();
                for (AlbumPicture listUrlPicture1 : listUrlPicture){
                    list.add(listUrlPicture1.getName());
                }

                Intent intent1 = new Intent(PictureFireActivity.this, ViewPictureFireActivity.class);
                intent1.putExtra("position", position);
                intent1.putExtra("listurlstring", list);
                startActivity(intent1);
            }
        });


    }
}
