package com.example.instagram.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.main.mainfirebase.AlbumPictureActivity;

import com.example.instagram.model.Person;

public class EnterPassword {
    public EnterPassword(final Context context, final Person person) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_password_picture_fire);

        final EditText edtPassword = dialog.findViewById(R.id.edtPassword); 
        final Button btnCancel = dialog.findViewById(R.id.btnCancelPassword);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirmPassword);
        
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtPassword.getText().toString().equals("")){
                    Toast.makeText(context, "Enter password", Toast.LENGTH_SHORT).show();
                    edtPassword.findFocus();
                }else {
                    if (edtPassword.getText().toString().equals(person.getUsername())){
                        Intent intent = new Intent(context, AlbumPictureActivity.class);
                        intent.putExtra("idInstagram", person.getIdPerson());
                        context.startActivity(intent);
                        dialog.cancel();
                    }else {
                        Toast.makeText(context, "Incorrect password", Toast.LENGTH_SHORT).show();

                        new CountDownTimer(1500, 100){
                            int i = 0;
                            @Override
                            public void onTick(long millisUntilFinished) {
                                if (i==0){
                                    edtPassword.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
                                    i = 1;
                                }else {
                                    edtPassword.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                                    i = 0;
                                }
                            }

                            @Override
                            public void onFinish() {
                                edtPassword.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                                edtPassword.findFocus();
                            }
                        }.start();
                    }
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
    }
}
