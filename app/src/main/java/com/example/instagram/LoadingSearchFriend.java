package com.example.instagram;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.adapter.PeopleFilterAdapter;
import com.example.instagram.dialog.SelectPerson;
import com.example.instagram.model.Person;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class LoadingSearchFriend implements PeopleFilterAdapter.ClickPeopleFilter {
    private AsyncHttpClient client = new AsyncHttpClient();
    private Context context;
    private RecyclerView recyclerView;
    private ArrayList<Person> people;

    public LoadingSearchFriend(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.people = new ArrayList<>();
    }

    public void searchfriend(String text){
        final String url = LinkUrlApi.URL_SEARCH1 + text + LinkUrlApi.URL_SEARCH2;
        this.client.addHeader("cookie", Utils.getCookieInstagram());
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    if (object.toString() != null){
                        JSONArray users = object.getJSONArray("users");
                        String id, username, fullname, picture;
                        for (int i = 0; i<users.length(); i++){
                            JSONObject user = users.getJSONObject(i).getJSONObject("user");
                            id = user.getString("pk");
                            username = user.getString("username");
                            fullname = user.getString("full_name");
                            picture = user.getString("profile_pic_url");
                            people.add(new Person(id, username, fullname, picture));
                        }
                        loadListSearchFriend(people);
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

    private void loadListSearchFriend(ArrayList<Person> people){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        PeopleFilterAdapter filterAdapter = new PeopleFilterAdapter(context, people);
        filterAdapter.setClickPeopleFilter(LoadingSearchFriend.this);
        recyclerView.setAdapter(filterAdapter);
    }

    @Override
    public void onClickPeople(int position, ArrayList<Person> people) {
        new SelectPerson(context, people.get(position));
    }
}
