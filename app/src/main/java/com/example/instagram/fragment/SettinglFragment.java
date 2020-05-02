package com.example.instagram.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.Utils;
import com.example.instagram.adapter.PeopleFilterAdapter;
import com.example.instagram.database.MyDatabase;
import com.example.instagram.main.LoginSuccessActivity;
import com.example.instagram.main.MainActivity;
import com.example.instagram.main.ViewPostsActivity;
import com.example.instagram.model.Person;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettinglFragment extends Fragment implements PeopleFilterAdapter.ClickPeopleFilter {
    private LoginSuccessActivity loginSuccessActivity;
    private View view;
    private RecyclerView recyclerAnalyzed;
    private TextView txtDeleteAllAnalyzed, txtLogOut;
    private MyDatabase db;
    private PeopleFilterAdapter peopelFilterAdapter;

    public SettinglFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        init();
        clickEvents();
        return view;
    }

    private void clickEvents() {
        txtDeleteAllAnalyzed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Notification");
                builder.setMessage("You Want Delete All Data Analyzed");
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (loginSuccessActivity.getId().equals(Utils.getUserIdInstagram())){
                            Person person = db.getPersonAnalyzed(Utils.getUserIdInstagram());
                            db.deleteAllPersonAnlyzed();
                            db.addPersonAnalyzed(person);

                            peopelFilterAdapter = new PeopleFilterAdapter(getContext(), db.getAllAnalyzedPerson());
                            recyclerAnalyzed.setAdapter(peopelFilterAdapter);
                        }else {
                            Toast.makeText(loginSuccessActivity, "Can't Delete Because Not You", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
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
        });

        txtLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Notification");
                builder.setMessage("You Want To Log Out App");
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CookieManager.getInstance().removeAllCookies(null);
                        CookieManager.getInstance().flush();

                        Intent intent = new Intent(getContext(), MainActivity.class);
                        // Can phai xoa cookie
                        startActivity(intent);
                    }
                });

                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.show();
            }
        });
    }

    private void init() {
        recyclerAnalyzed = view.findViewById(R.id.recyclerAnalyzed);
        recyclerAnalyzed.setHasFixedSize(true);
        recyclerAnalyzed.setLayoutManager(new LinearLayoutManager(getContext()));

        db = new MyDatabase(getContext());

        txtDeleteAllAnalyzed = view.findViewById(R.id.txtDeleteAllData);
        txtLogOut = view.findViewById(R.id.txtLogoOut);

        peopelFilterAdapter = new PeopleFilterAdapter(getContext(), db.getAllAnalyzedPerson());
        recyclerAnalyzed.setAdapter(peopelFilterAdapter);

        peopelFilterAdapter.setClickPeopleFilter(SettinglFragment.this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginSuccessActivity) {
            loginSuccessActivity = (LoginSuccessActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onClickPeople(final int position, final ArrayList<Person> people) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_dialog_click_recycler_analyzed);

        CircleImageView circleImageView = dialog.findViewById(R.id.imvPeopeoAnalyzed);
        TextView txtUserName = dialog.findViewById(R.id.txtUserNameAnalyzedDialog);
        TextView txtFullname = dialog.findViewById(R.id.txtFullNameAnalyzedDialog);
        TextView txtOpen = dialog.findViewById(R.id.txtOpenAnalyzeDialog);
        TextView txtViewProfile = dialog.findViewById(R.id.txtViewProfileAnalyzed);
        TextView txtDelete = dialog.findViewById(R.id.txtDeleteAnalyze);
        TextView txtCancel = dialog.findViewById(R.id.txtCancelAnalyze);

        Picasso.with(getContext()).load(people.get(position).getImvPeron()).error(R.drawable.picture_profile_default).into(circleImageView);
        txtUserName.setText(people.get(position).getUsername());
        txtFullname.setText(people.get(position).getFullName());

        txtOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginSuccessActivity.class);
                intent.putExtra("id", people.get(position).getIdPerson());
                intent.putExtra("username", people.get(position).getUsername());
                startActivity(intent);
                dialog.cancel();
            }
        });

        txtViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewPostsActivity.class);
                intent.putExtra("UserNameProfilePost", people.get(position).getUsername());
                startActivity(intent);
                dialog.cancel();
            }
        });

        txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getPersonAnalyzed(people.get(position).getIdPerson()) != null){
                    if (people.get(position).getIdPerson().equals(Utils.getUserIdInstagram())){
                        Toast.makeText(loginSuccessActivity, "You Can't Delete Yourself", Toast.LENGTH_SHORT).show();
                    }else {
                        db.deletePersonAnlyzed(people.get(position).getIdPerson());

                        peopelFilterAdapter = new PeopleFilterAdapter(getContext(), db.getAllAnalyzedPerson());
                        recyclerAnalyzed.setAdapter(peopelFilterAdapter);

                        peopelFilterAdapter.setClickPeopleFilter(SettinglFragment.this);

                        Toast.makeText(loginSuccessActivity, "Delete Success", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                    
                }
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
