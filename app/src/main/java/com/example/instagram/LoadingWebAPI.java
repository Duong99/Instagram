package com.example.instagram;

import android.util.Log;
import android.widget.Toast;

import com.example.instagram.model.Person;
import com.example.instagram.model.Picture;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class LoadingWebAPI {
    private AsyncHttpClient client = new AsyncHttpClient();
    private ArrayList<Picture> pictures;
    private GetDataProfile getDataProfile;
    private ArrayList<Person> peopleFollow;
    private GetPeopleFollow getPeopleFollow;

    public LoadingWebAPI() {
        client.addHeader("cookie", Utils.getCookieInstagram());
        pictures = new ArrayList<>();
        this.peopleFollow = new ArrayList<>();
    }

    public LoadingWebAPI(ArrayList<Person> peopleFollow) {
        client.addHeader("cookie", Utils.getCookieInstagram());
        peopleFollow = new ArrayList<>();
        this.peopleFollow = peopleFollow;
    }

    public void getDataProfile(String username) {
        String url = LinkUrlApi.URL_INSTAGRAM + username;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Document doc = Jsoup.parse(new String(responseBody));

                if (doc != null) {
                    String oProfile = doc.html().split("_sharedData = ", 2)[1];
                    oProfile = oProfile.substring(0, oProfile.indexOf(";</script>"));

                    if (oProfile != null) {
                        try {
                            JSONObject o = new JSONObject(oProfile);
                            JSONObject ouser = o.getJSONObject("entry_data").getJSONArray("ProfilePage")
                                    .getJSONObject(0).getJSONObject("graphql").getJSONObject("user");

                            getDataProfile.dataProfile(
                                    ouser.getJSONObject("edge_owner_to_timeline_media").getString("count"),
                                    ouser.getJSONObject("edge_followed_by").getString("count"),
                                    ouser.getJSONObject("edge_follow").getString("count"),
                                    ouser.getString("username"), ouser.getString("profile_pic_url_hd"));

                            JSONArray oedges = ouser.getJSONObject("edge_owner_to_timeline_media")
                                    .getJSONArray("edges");

                            String shortcode, display_url, edge_liked_by_count,edge_media_to_comment_count;

                            for (int i = 0; i < oedges.length(); i++) {
                                    JSONObject onode = oedges.getJSONObject(i).getJSONObject("node");

                                    shortcode = onode.getString("shortcode");
                                    display_url = onode.getString("display_url");
                                    edge_liked_by_count = onode.getJSONObject("edge_liked_by")
                                            .getString("count");

                                    edge_media_to_comment_count = onode.getJSONObject("edge_media_to_comment")
                                            .getString("count");

                                    pictures.add(new Picture(shortcode, display_url,
                                            edge_liked_by_count, edge_media_to_comment_count));

                            }
                            String end_cursor = ouser.getJSONObject("edge_owner_to_timeline_media")
                                    .getJSONObject("page_info").getString("end_cursor");

                            if (!(end_cursor.equals("null"))){
                                end_cursor = end_cursor.substring(0, end_cursor.length() - 2);
                            }

                            getDataProfile.getListPost(pictures ,end_cursor);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Duong", "onFailure: " + error.toString());
            }
        });
    }

    public void getPostPersonalNext(String end_cursor, String id) {
       String url = LinkUrlApi.URL_PostPersonal1 + id
                + LinkUrlApi.URL_PostPersonal2 + end_cursor + LinkUrlApi.URL_PostPersonal3;

       client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    if (object != null) {
                        JSONArray aedges = object.getJSONObject("data").getJSONObject("user")
                                .getJSONObject("edge_owner_to_timeline_media").getJSONArray("edges");

                        if (aedges != null) {
                            String shortcode, display_url, edge_liked_by_count, edge_media_to_comment_count;

                            for (int i = 0; i < aedges.length(); i++) {
                                JSONObject onode = aedges.getJSONObject(i).getJSONObject("node");

                                shortcode = onode.getString("shortcode");
                                display_url = onode.getString("display_url");
                                edge_liked_by_count = onode.getJSONObject("edge_media_preview_like")
                                        .getString("count");
                                edge_media_to_comment_count = onode.getJSONObject("edge_media_to_comment")
                                        .getString("count");

                                pictures.add(new Picture(shortcode, display_url, edge_liked_by_count, edge_media_to_comment_count));
                            }
                            String end_cursor = object.getJSONObject("data").getJSONObject("user")
                                    .getJSONObject("edge_owner_to_timeline_media")
                                    .getJSONObject("page_info").getString("end_cursor");

                            if (!(end_cursor.equals("null"))){
                                end_cursor = end_cursor.substring(0, end_cursor.length() - 2);
                            }
                            getDataProfile.getListPost(pictures, end_cursor);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Duong", "onFailure: " + error.toString());
            }
        });
    }

    public void getFollowingDataJson(String url, final String id) {

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject o = new JSONObject(new String(responseBody));
                    if (o.toString() != null) {
                        String idPerson, fullName, imvPeron, username;

                        JSONArray edges = o.getJSONObject("data")
                                .getJSONObject("user")
                                .getJSONObject("edge_follow")
                                .getJSONArray("edges");

                        if (edges.toString() != null) {
                            for (int i = 0; i < edges.length(); i++) {
                                JSONObject onode = edges.getJSONObject(i).getJSONObject("node");
                                idPerson = onode.getString("id");
                                fullName = onode.getString("full_name");
                                imvPeron = onode.getString("profile_pic_url");
                                username = onode.getString("username");
                                peopleFollow.add(new Person(idPerson, username, fullName, imvPeron));
                            }
                        }
                        boolean checkCuroser = o.getJSONObject("data")
                                .getJSONObject("user").getJSONObject("edge_follow").
                                        getJSONObject("page_info").getBoolean("has_next_page");
                        if (checkCuroser) {
                            String end_cursor = o.getJSONObject("data").getJSONObject("user")
                                    .getJSONObject("edge_follow").getJSONObject("page_info")
                                    .getString("end_cursor");

                            end_cursor = end_cursor.substring(0, end_cursor.length() - 2);

                            String url = LinkUrlApi.URL_Following1 + id + LinkUrlApi.URL_Follow2 +
                                    LinkUrlApi.URL_Follow3 + end_cursor + LinkUrlApi.URL_Follow4 +
                                    LinkUrlApi.URL_Follow5;

                            getFollowingDataJson(url, id);

                        } else {
                            getPeopleFollow.getPeopleFollowing(peopleFollow);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {}
        });
    }

    public void getFollowersDataJson(String url, final String id) {
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject o = new JSONObject(new String(responseBody));
                    if (o.toString() != null) {
                        String idPerson, fullName, imvPeron, username;

                        JSONArray edges = o.getJSONObject("data")
                                .getJSONObject("user")
                                .getJSONObject("edge_followed_by")
                                .getJSONArray("edges");

                        if (edges.toString() != null) {
                            for (int i = 0; i < edges.length(); i++) {
                                JSONObject onode = edges.getJSONObject(i).getJSONObject("node");
                                idPerson = onode.getString("id");
                                fullName = onode.getString("full_name");
                                imvPeron = onode.getString("profile_pic_url");
                                username = onode.getString("username");
                                peopleFollow.add(new Person(idPerson, username, fullName, imvPeron));
                            }
                        }
                        boolean checkCuroser = o.getJSONObject("data")
                                .getJSONObject("user").getJSONObject("edge_followed_by").
                                        getJSONObject("page_info").getBoolean("has_next_page");
                        if (checkCuroser) {
                            String end_cursor = o.getJSONObject("data").getJSONObject("user")
                                    .getJSONObject("edge_followed_by").getJSONObject("page_info")
                                    .getString("end_cursor");

                            end_cursor = end_cursor.substring(0, end_cursor.length() - 2);

                            String url = LinkUrlApi.URL_Followers1 + id + LinkUrlApi.URL_Follow2 +
                                    LinkUrlApi.URL_Follow3 + end_cursor + LinkUrlApi.URL_Follow4 +
                                    LinkUrlApi.URL_Follow5;

                            getFollowersDataJson(url, id);

                        } else {
                            getPeopleFollow.getPeopleFollowers(peopleFollow);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Duong", "onFailure: " + error.toString());
            }
        });
    }

    public interface GetPeopleFollow {
        void getPeopleFollowing(ArrayList<Person> peopleFollowing);
        void getPeopleFollowers(ArrayList<Person> peopleFollowers);
    }

    public void setPeopleFollow(GetPeopleFollow getPeopleFollow) {
        this.getPeopleFollow = getPeopleFollow;
    }

    public interface GetDataProfile {
        void dataProfile(String nPost, String nFollowers, String nFollowing, String username, String urlPicture);
        void getListPost(ArrayList<Picture> pictures, String end_cursor);
    }

    public void setDataProfile(GetDataProfile getDataProfile) {
        this.getDataProfile = getDataProfile;
    }
}
