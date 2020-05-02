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
import android.widget.TextView;

import com.example.instagram.GetPostHome;
import com.example.instagram.LoadingTotalLike;
import com.example.instagram.dialog.OptionPicturePost;
import com.example.instagram.R;
import com.example.instagram.dialog.SelectPerson;
import com.example.instagram.GetComment;
import com.example.instagram.dialog.ShowRecyclerComment;
import com.example.instagram.adapter.HomePostAdapter;
import com.example.instagram.adapter.PeopleFilterAdapter;
import com.example.instagram.main.LoginSuccessActivity;
import com.example.instagram.main.WatchImageActivity;
import com.example.instagram.model.Comment;
import com.example.instagram.model.HomePost;
import com.example.instagram.model.Person;
import com.example.instagram.model.Picture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements GetPostHome.PostHome,
        HomePostAdapter.ClickRecyclerPostHome, LoadingTotalLike.GetTotalLike,
        PeopleFilterAdapter.ClickPeopleFilter, HomePostAdapter.OnClickComment,
        GetComment.PeopleComment {

    private GetPostHome getPostHome;
    private View view;
    private RecyclerView recyclerHomePost;
    private HomePostAdapter homePostAdapter;
    private ArrayList<HomePost> homePosts;
    private String cursor = null;
    private PeopleFilterAdapter adapterPeopleLike;
    private TextView txtViewMore;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        init();

        txtViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(cursor.equals("null"))){
                    getPostHome.getPostHomeNext(cursor);
                }
            }
        });
        return view;
    }

    private void init() {
        recyclerHomePost = view.findViewById(R.id.recyclerHomePost);
        recyclerHomePost.setHasFixedSize(true);
        recyclerHomePost.setLayoutManager(new LinearLayoutManager(getContext()));

        txtViewMore = view.findViewById(R.id.txtViewMorePostHome);
        homePosts = new ArrayList<>();

        getPostHome = new GetPostHome();
        getPostHome.setPostHome(HomeFragment.this);
        getPostHome.getPostHomeFirst();
    }

    @Override
    public void postHome(JSONObject edge_web_feed_timeline)  {
        try {
            cursor = edge_web_feed_timeline.getJSONObject("page_info").getString("end_cursor");

            if (cursor.isEmpty()){
                txtViewMore.setVisibility(View.INVISIBLE);
            }else {
                txtViewMore.setVisibility(View.VISIBLE);
            }

            JSONArray edges = edge_web_feed_timeline.getJSONArray("edges");

            if (edges.toString() != null){
                JSONObject node;
                String id, username, fullname, imvuser, shortCode, totalLike, totlaComment, imvPost;

                for (int i=0; i<edges.length(); i++){
                     node = edges.getJSONObject(i).getJSONObject("node");
                     if (node.length() == 29){
                          id = node.getJSONObject("owner").getString("id");
                          username = node.getJSONObject("owner").getString("username");
                          fullname = node.getJSONObject("owner").getString("full_name");
                          imvuser = node.getJSONObject("owner").getString("profile_pic_url");
                          shortCode = node.getString("shortcode");
                          totalLike = node.getJSONObject("edge_media_preview_like").getString("count");
                          totlaComment = node.getJSONObject("edge_media_preview_comment").getString("count");
                          imvPost = node.getString("display_url");

                         homePosts.add(new HomePost(new Person(id, username, fullname, imvuser),
                                 new Picture(shortCode, imvPost, totalLike, totlaComment)));
                     }
                }
                setHomePostAdapter();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setHomePostAdapter(){
        homePostAdapter = new HomePostAdapter(homePosts, getContext());
        homePostAdapter.setHomePosts(HomeFragment.this);
        homePostAdapter.setOnClickComment(HomeFragment.this);
        recyclerHomePost.setAdapter(homePostAdapter);
    }

    @Override
    public void moreOption(final Picture picture) {
        new OptionPicturePost(getContext(), picture);
    }

    @Override
    public void image(String url) {
        Intent intent = new Intent(getContext(), WatchImageActivity.class);
        intent.putExtra("imvPicture", url);
        startActivity(intent);
    }

    @Override
    public void profile(final Person person) {
        new SelectPerson(getContext(), person);
    }

    @Override
    public void totalLike(String shortcode) {
        LoadingTotalLike loadingTotalLike = new LoadingTotalLike();
        loadingTotalLike.setPeopleTotalLike(HomeFragment.this);
        loadingTotalLike.loadingGetTotalLike(shortcode);

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
        adapterPeopleLike.setClickPeopleFilter(HomeFragment.this);
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
    public void clickComment(String shortcode) {
        GetComment comment = new GetComment(getContext(), shortcode);
        comment.getFirstComment();
        comment.setPeopleComment(HomeFragment.this);
    }

    @Override
    public void onComments(ArrayList<Comment> comments) {
        new ShowRecyclerComment(getContext(), comments);
    }
}
