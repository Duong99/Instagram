package com.example.instagram;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.util.ArrayList;

public class SendingImage {
    private Context context;
    private String urlPicture;

    public SendingImage(Context context, String urlPicture) {
        this.context = context;
        this.urlPicture = urlPicture;
    }

    public void sharingFacebook(){
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(urlPicture))
                    .build();
            ShareDialog.show((Activity) context, linkContent);
        }
    }

    public void sendingApp(){
        ArrayList<Uri> uris = new ArrayList<>();
        uris.add(Uri.parse(String.valueOf(urlPicture)));

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "Share images to.."));
    }

    private class DownloadImgTask extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            sharePhoto(bitmap);
        }
    }

    private void sharePhoto(Bitmap bitmap){
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap).build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo).build();
        ShareDialog dialog = new ShareDialog((Activity) context);
        if (dialog.canShow(SharePhotoContent.class)){
            dialog.show(content);   
        }else {
            Toast.makeText(context, "You can't share photo :(", Toast.LENGTH_SHORT).show();
        }
    }
}
