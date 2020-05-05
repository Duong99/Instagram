package com.example.instagram.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.instagram.R;
import com.example.instagram.Utils;
import com.example.instagram.database.MyDatabase;
import com.example.instagram.main.LoginSuccessActivity;
import com.example.instagram.main.ViewPostsActivity;
import com.example.instagram.main.ViewProfileActivity;
import com.example.instagram.main.mainfirebase.AlbumPictureActivity;
import com.example.instagram.model.Person;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectPerson {
    public SelectPerson(final Context context, final Person person) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_dialog_click_people_filter);

        CircleImageView circleimv = dialog.findViewById(R.id.imvPeopeoFilterDialog);
        TextView txtUserName = dialog.findViewById(R.id.txtUserNameFilterDialog);
        TextView txtFullName = dialog.findViewById(R.id.txtFullNameFilterDialog);
        TextView txtAnalyze = dialog.findViewById(R.id.txtAnalyzeDialog);
        TextView txtViewprofile = dialog.findViewById(R.id.txtViewProfileDialog);
        TextView txtCancelSelectPerson = dialog.findViewById(R.id.txtCancelSelectPerson);
        TextView txtViewPictureFire = dialog.findViewById(R.id.txtViewPictureFire);

        txtViewPictureFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFire = new Intent(context, AlbumPictureActivity.class);
                intentFire.putExtra("idInstagram", person.getIdPerson());
                context.startActivity(intentFire);
                dialog.cancel();
            }
        });

        Picasso.with(context).load(person.getImvPeron())
                .error(R.drawable.picture_profile_default).into(circleimv);

        txtFullName.setText(person.getFullName());
        txtUserName.setText(person.getUsername());

        txtAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                              MyDatabase db = new MyDatabase(context);

                if (db.getPersonAnalyzed(person.getIdPerson()) == null){
                    db.addPersonAnalyzed(new Person(person.getIdPerson(), person.getUsername(),
                            person.getFullName(), person.getImvPeron()));
                }

                Intent intent = new Intent(context, LoginSuccessActivity.class);
                intent.putExtra("id", person.getIdPerson());
                intent.putExtra("username", person.getUsername());
                context.startActivity(intent);
                dialog.cancel();
            }
        });

        txtViewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewProfileActivity.class);
                intent.putExtra("UserNameProfilePost", person.getUsername());
                context.startActivity(intent);
                dialog.cancel();
            }
        });

        txtCancelSelectPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
}
