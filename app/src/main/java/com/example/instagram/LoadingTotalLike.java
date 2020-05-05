package com.example.instagram;

import android.util.Log;

import com.example.instagram.model.Person;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class LoadingTotalLike {
    private AsyncHttpClient client = new AsyncHttpClient();

    private ArrayList<Person> peopleLike = new ArrayList<>();
    private GetTotalLike getTotalLike;

    public LoadingTotalLike() {
        this.client.addHeader("cookie", Utils.getCookieInstagram());
    }

    public void loadingGetTotalLike(final String shortcode){
        String url = LinkUrlApi.URL_PEOPLELIKEPOST1 + shortcode + LinkUrlApi.URL_PEOPLELIKEPOST2 + LinkUrlApi.URL_PEOPLELIKEPOST5;

        client.addHeader("cookie", Utils.getCookieInstagram());
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    if (object.toString() != null){
                        JSONArray edges = object.getJSONObject("data").getJSONObject("shortcode_media")
                                .getJSONObject("edge_liked_by").getJSONArray("edges");

                        if (edges.toString() != null){
                            String id, username, fullname, profilePicture;
                            for (int i=0; i<edges.length(); i++){
                                JSONObject node = edges.getJSONObject(i).getJSONObject("node");

                                id = node.getString("id");
                                username = node.getString("username");
                                fullname = node.getString("full_name");
                                profilePicture = node.getString("profile_pic_url");

                                peopleLike.add(new Person(id, username, fullname, profilePicture));
                            }

                            if (object.getJSONObject("data").getJSONObject("shortcode_media")
                                    .getJSONObject("edge_liked_by").getJSONObject("page_info").getBoolean("has_next_page")){
                                String end_cursor = object.getJSONObject("data").getJSONObject("shortcode_media")
                                        .getJSONObject("edge_liked_by").getJSONObject("page_info").getString("end_cursor");

                                end_cursor = end_cursor.substring(0, end_cursor.length()-2);

                                loadingGetTotalLikeNext(shortcode, end_cursor);
                            }else {
                                getTotalLike.getListPeoplePle(peopleLike);
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Duong", "LoadingTotalLike - onFailure: " + error.toString());
            }
        });
    }

    private void loadingGetTotalLikeNext(final String shortcode, String end_cursor){
        String url = LinkUrlApi.URL_PEOPLELIKEPOST1 + shortcode + LinkUrlApi.URL_PEOPLELIKEPOST2 + LinkUrlApi.URL_PEOPLELIKEPOST3
                + end_cursor + LinkUrlApi.URL_PEOPLELIKEPOST4 + LinkUrlApi.URL_PEOPLELIKEPOST5;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    if (object.toString() != null){
                        JSONArray edges = object.getJSONObject("data").getJSONObject("shortcode_media")
                                .getJSONObject("edge_liked_by").getJSONArray("edges");

                        if (edges != null){
                            String id, username, fullname, profilePicture;

                            for (int i=0; i<edges.length(); i++){
                                JSONObject node = edges.getJSONObject(i).getJSONObject("node");

                                id = node.getString("id");
                                username = node.getString("username");
                                fullname = node.getString("full_name");
                                profilePicture = node.getString("profile_pic_url");

                                peopleLike.add(new Person(id, username, fullname, profilePicture));
                            }

                            if (object.getJSONObject("data").getJSONObject("shortcode_media")
                                    .getJSONObject("edge_liked_by").getJSONObject("page_info").getBoolean("has_next_page")){
                                String end_cursor = object.getJSONObject("data").getJSONObject("shortcode_media")
                                        .getJSONObject("edge_liked_by").getJSONObject("page_info").getString("end_cursor");

                                loadingGetTotalLikeNext(shortcode, end_cursor);
                            }else {
                                getTotalLike.getListPeoplePle(peopleLike);
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Duong", "LoadingTotalLikeNext - onFailure: " + error.toString());
            }
        });
    }

    public interface GetTotalLike{
        void getListPeoplePle(ArrayList<Person> people);
    }

    public void setPeopleTotalLike(GetTotalLike getTotalLike){
        this.getTotalLike = getTotalLike;
    }
}
