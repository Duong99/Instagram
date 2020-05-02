package com.example.instagram.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instagram.DownloadFile;
import com.example.instagram.LoadingTotalLike;
import com.example.instagram.dialog.OptionPicturePost;
import com.example.instagram.R;
import com.example.instagram.dialog.SelectPerson;
import com.example.instagram.GetComment;
import com.example.instagram.dialog.ShowRecyclerComment;
import com.example.instagram.adapter.PeopleFilterAdapter;
import com.example.instagram.adapter.PictureAdapter;
import com.example.instagram.main.LoginSuccessActivity;
import com.example.instagram.main.ViewPostsActivity;
import com.example.instagram.main.WatchImageActivity;
import com.example.instagram.LoadingWebAPI;
import com.example.instagram.model.Comment;
import com.example.instagram.model.Person;
import com.example.instagram.model.Picture;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalFragment extends Fragment implements PictureAdapter.OnClickImageHome,
        LoadingTotalLike.GetTotalLike, PeopleFilterAdapter.ClickPeopleFilter,
        LoadingWebAPI.GetDataProfile, PictureAdapter.OnClickComment, GetComment.PeopleComment {

    private View view;
    private TextView txtFullNameProfile, txtTotalPostsHome, txtTotalFollowersHome,
            txtTotalFollowingHome, txtViewMorePersonal;
    private ImageView imvBanner;
    private CircleImageView imvProfile;
    private LoginSuccessActivity loginSuccessActivity;
    private RecyclerView recyclerHome;
    private PictureAdapter adapterPicture;
    private PeopleFilterAdapter adapterPeopleLike;
    private String urlPictureProfile, end_cursor;

    public PersonalFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal, container, false);
        init();

        getDataProfile(loginSuccessActivity.getUserName());
        onClickEvents();

        return view;
    }

    private void getDataProfile(String userName) {
        LoadingWebAPI api = new LoadingWebAPI();
        api.setDataProfile(PersonalFragment.this);
        api.getDataProfile(userName);
    }

    private void onClickEvents() {
        txtViewMorePersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(end_cursor.equals("null"))){
                    new LoadingWebAPI().getPostPersonalNext(end_cursor, loginSuccessActivity.getId());
                }
            }
        });

        imvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.item_dialog_image_profile);
                TextView txtViewProfileImageProfile = dialog.findViewById(R.id.txtViewProfileImageProfile);
                TextView txtPhotoLookImageProfile = dialog.findViewById(R.id.txtLookPhotoImageProfile);
                TextView txtCancelImageProfile = dialog.findViewById(R.id.txtCancelImageProfile);
                TextView txtDownloadImageProfile = dialog.findViewById(R.id.txtDownloadImageProfile);

                txtDownloadImageProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DownloadFile(getContext(), urlPictureProfile).startDownloadImage();
                        dialog.cancel();
                    }
                });

                txtCancelImageProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                txtPhotoLookImageProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), WatchImageActivity.class);
                        intent.putExtra("imvPicture", urlPictureProfile);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });

                txtViewProfileImageProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ViewPostsActivity.class);
                        intent.putExtra("UserNameProfilePost", loginSuccessActivity.getUserName());
                        startActivity(intent);
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });
    }


    private void init() {
        txtViewMorePersonal = view.findViewById(R.id.txtViewMorePersonal);
        txtTotalPostsHome = view.findViewById(R.id.txtTotalPostsHome);
        txtTotalFollowersHome = view.findViewById(R.id.txtTotalFollowersHome);
        txtTotalFollowingHome = view.findViewById(R.id.txtTotalFollowingHome);

        recyclerHome = view.findViewById(R.id.recyclerHome);
        recyclerHome.setHasFixedSize(true);
        recyclerHome.setLayoutManager(new LinearLayoutManager(getContext()));

        txtFullNameProfile = view.findViewById(R.id.txtFullNameProfile);
        imvBanner = view.findViewById(R.id.imvBanner);
        imvProfile = view.findViewById(R.id.imvProfile);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginSuccessActivity){
            loginSuccessActivity = (LoginSuccessActivity) context;
        }
    }

    private void setAdapterRecyclerHome(ArrayList<Picture> pictures){
        adapterPicture = new PictureAdapter(getContext(), pictures);
        adapterPicture.setOnClickImageHome(PersonalFragment.this);
        adapterPicture.setOnClickComment(PersonalFragment.this);
        recyclerHome.setAdapter(adapterPicture);
    }

    @Override
    public void getListPeoplePle(ArrayList<Person> people) {
        final Dialog d = new Dialog(getContext());
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.item_list_people_likes_comments);

        ImageButton imvbtnClosePeopleLike = d.findViewById(R.id.imvbtnClosePeopleLike);

        RecyclerView recyclerPeopleLike = d.findViewById(R.id.recyclerPeopleLike);
        recyclerPeopleLike.setHasFixedSize(true);
        recyclerPeopleLike.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterPeopleLike = new PeopleFilterAdapter(getContext(), people);
        adapterPeopleLike.setClickPeopleFilter(PersonalFragment.this);
        recyclerPeopleLike.setAdapter(adapterPeopleLike);

        imvbtnClosePeopleLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.cancel();
            }
        });
        d.show();

    }

    @Override
    public void onClickPeople(final int position, final ArrayList<Person> people) {
        new SelectPerson(getContext(), people.get(position));
    }

    @Override
    public void dataProfile(String nPost, String nFollowers, String nFollowing, String username, String urlPicture) {
        Picasso.with(getContext()).load(urlPicture).error(R.drawable.picture_profile_default).into(imvBanner);
        Picasso.with(getContext()).load(urlPicture).error(R.drawable.picture_profile_default).into(imvProfile);
        txtTotalPostsHome.setText(String.format("%s \n %s", nPost, getResources().getString(R.string.posts)));
        txtTotalFollowersHome.setText(String.format("%s \n %s", nFollowers, getResources().getString(R.string.followers)));
        txtTotalFollowingHome.setText(String.format("%s \n %s", nFollowing, getResources().getString(R.string.following)));
        txtFullNameProfile.setText(username);
        this.urlPictureProfile = urlPicture;
    }

    @Override
    public void getListPost(ArrayList<Picture> pictures, String end_cursor) {
        this.end_cursor = end_cursor;
        setAdapterRecyclerHome(pictures);

        if (end_cursor.equals("null")){
            txtViewMorePersonal.setVisibility(View.INVISIBLE);
        }else {
            txtViewMorePersonal.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClickImage(int position, ArrayList<Picture> pictures) {
        Intent intent = new Intent(getContext(), WatchImageActivity.class);
        intent.putExtra("imvPicture", pictures.get(position).getDisplay_url());
        startActivity(intent);
    }

    @Override
    public void onClickLike(String shortcode, ArrayList<Picture> pictures) {
        LoadingTotalLike loading = new LoadingTotalLike();
        loading.setPeopleTotalLike(PersonalFragment.this);
        loading.loadingGetTotalLike(shortcode);
    }

    @Override
    public void onClickLongPicture(final int position, final ArrayList<Picture> pictures) {
        new OptionPicturePost(getContext(), pictures.get(position));
    }

    @Override
    public void onClickComent(String shortcode) {
        GetComment comment = new GetComment(getContext(), shortcode);
        comment.getFirstComment();
        comment.setPeopleComment(PersonalFragment.this);
    }

    @Override
    public void onComments(ArrayList<Comment> comments) {
        new ShowRecyclerComment(getContext(), comments);
    }
}