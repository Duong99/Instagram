package com.example.instagram.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.instagram.Common;
import com.example.instagram.DownloadFile;
import com.example.instagram.R;
import com.example.instagram.SendingImage;
import com.example.instagram.main.ViewPostsActivity;
import com.example.instagram.model.Picture;

public class OptionPicturePost {
    public OptionPicturePost(final Context context, final Picture picture) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_click_long_image_home_dialog);

        TextView txtGoToPost = dialog.findViewById(R.id.txtGoToPostImage);
        TextView txtDownload = dialog.findViewById(R.id.txtDownloadImage);
        TextView txtCancel = dialog.findViewById(R.id.txtCancelImage);
        TextView txtSharingFacebook = dialog.findViewById(R.id.txtSharingFacebook);
        TextView txtSending = dialog.findViewById(R.id.txtSending);
        TextView txtPictureFire = dialog.findViewById(R.id.txtPictureFire);

        txtPictureFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShowDialogListAlbumPicture(context, picture.getDisplay_url());
                dialog.cancel();
            }
        });

        txtSending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendingImage(context, picture.getDisplay_url()).sendingApp();
                dialog.cancel();
            }
        });

        txtSharingFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendingImage(context, Common.URL_INSTAGRAM + "p/" + picture.getShortcode()).sharingFacebook();
                dialog.cancel();
            }
        });

        txtGoToPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewPostsActivity.class);
                intent.putExtra("UserNameProfilePost", picture.getShortcode());
                context.startActivity(intent);

                dialog.cancel();
            }
        });

        txtDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new DownloadFile(context, picture.getDisplay_url()).startDownloadImage();

                dialog.cancel();
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
}
