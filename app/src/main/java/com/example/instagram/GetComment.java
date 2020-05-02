package com.example.instagram;

import android.content.Context;
import android.widget.Toast;

import com.example.instagram.adapter.CommentAdapter;
import com.example.instagram.model.Comment;
import com.example.instagram.model.Person;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class GetComment {
    private AsyncHttpClient client = new AsyncHttpClient();
    private Context context;
    private String shortcode;
    private ArrayList<Comment> comments;

    public GetComment(Context context, String shortcode) {
        comments = new ArrayList<>();
        this.context = context;
        this.shortcode = shortcode;
    }

    public void getFirstComment(){
        String url = "https://www.instagram.com/p/" + shortcode+ "/comments/?__a=1";
        client.addHeader("cookie", Utils.getCookieInstagram());
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Document doc = Jsoup.parse(new String(responseBody));
                if (doc.toString() != null){
                    String comment = doc.html().split("\"edge_media_to_parent_comment\":", 2)[1];
                    comment = comment.substring(0, comment.indexOf(",\"edge_media_to_hoisted_comment\""));

                    if (comment != null){
                        try {
                            JSONObject object = new JSONObject(comment);

                            if (object.toString() != null){
                                JSONArray edges = object.getJSONArray("edges");
                                String txtUser, txtTotalLike, txtTextComment, imvUser, idComment, idUser;
                                for (int i=0; i<edges.length(); i++){
                                    JSONObject node = edges.getJSONObject(i).getJSONObject("node");
                                    idComment = node.getString("id");
                                    txtTextComment = node.getString("text");
                                    txtTotalLike = node.getJSONObject("edge_liked_by").getString("count");

                                    imvUser = node.getJSONObject("owner").getString("profile_pic_url");
                                    txtUser = node.getJSONObject("owner").getString("username");
                                    idUser = node.getJSONObject("owner").getString("id");

                                    comments.add(new Comment(new Person(idUser, txtUser, "", imvUser),
                                            txtTextComment, idComment, txtTotalLike));
                                }

                                if (object.getJSONObject("page_info").getBoolean("has_next_page")){
                                    String end_cursor = object.getJSONObject("page_info").getString("end_cursor");

                                    String idCuror = end_cursor.split("\"cached_comments_cursor\": \"", 2)[1];
                                    idCuror = idCuror.substring(0, idCuror.indexOf("\","));

                                    String cursor = end_cursor.split("\"bifilter_token\": \"", 2)[1];
                                    cursor = cursor.substring(0, cursor.indexOf("=="));

                                    String url = Common.COMMENT1 + shortcode + Common.COMMENT2 + idCuror + Common.COMMENT3 + cursor + Common.COMMENT4;
                                    loadCommentNext(url, shortcode);
                                }else {
                                    mPeopleCommet.onComments(comments);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void loadCommentNext(String url, final String shortcode){
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    if (object.toString() != null){
                        JSONObject edge_media_to_parent_comment = object.getJSONObject("data")
                                .getJSONObject("shortcode_media").getJSONObject("edge_media_to_parent_comment");



                        JSONArray edges = edge_media_to_parent_comment.getJSONArray("edges");
                        String txtUser, txtTotalLike, txtTextComment, imvUser, idComment, idUser;
                        for (int i=0; i<edges.length(); i++){
                            JSONObject node = edges.getJSONObject(i).getJSONObject("node");
                            idComment = node.getString("id");
                            txtTextComment = node.getString("text");
                            txtTotalLike = node.getJSONObject("edge_liked_by").getString("count");

                            imvUser = node.getJSONObject("owner").getString("profile_pic_url");
                            txtUser = node.getJSONObject("owner").getString("username");
                            idUser = node.getJSONObject("owner").getString("id");

                            comments.add(new Comment(new Person(idUser, txtUser, "", imvUser),
                                    txtTextComment, idComment, txtTotalLike));
                        }

                        if (edge_media_to_parent_comment.getJSONObject("page_info").getBoolean("has_next_page")){
                            String end_cursor = edge_media_to_parent_comment.getJSONObject("page_info").getString("end_cursor");

                            String idCuror = end_cursor.split("\"cached_comments_cursor\": \"", 2)[1];
                            idCuror = idCuror.substring(0, idCuror.indexOf("\","));

                            String cursor = end_cursor.split("\"bifilter_token\": \"", 2)[1];
                            cursor = cursor.substring(0, cursor.indexOf("=="));

                            String url = Common.COMMENT1 + shortcode + Common.COMMENT2 + idCuror + Common.COMMENT3 + cursor + Common.COMMENT4;
                            loadCommentNext(url, shortcode);
                        }else {
                            mPeopleCommet.onComments(comments);
                        }
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

    private PeopleComment mPeopleCommet;

    public interface PeopleComment{
        void onComments(ArrayList<Comment> comments);
    }

    public void setPeopleComment(PeopleComment peopleComment){
        this.mPeopleCommet = peopleComment;
    }
}
