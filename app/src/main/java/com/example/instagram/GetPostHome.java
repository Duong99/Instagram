package com.example.instagram;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cz.msebera.android.httpclient.Header;

public class GetPostHome {
    private AsyncHttpClient client = new AsyncHttpClient();
    public GetPostHome() {
        client.addHeader("cookie", Utils.getCookieInstagram());
    }

    public void getPostHomeFirst(){
        client.get(LinkUrlApi.URL_INSTAGRAM, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Document doc = Jsoup.parse(new String(responseBody));
                if (doc != null){

                    String oProfile = doc.html().split("'feed',", 2)[1];
                    oProfile = oProfile.substring(0, oProfile.indexOf(");</script>"));

                    if (oProfile != null){
                        try {
                            JSONObject edge_web_feed_timeline = new JSONObject(oProfile)
                                    .getJSONObject("user").getJSONObject("edge_web_feed_timeline");
                            mGetPostHome.postHome(edge_web_feed_timeline);

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

    public void getPostHomeNext(String cursor){
        String url = LinkUrlApi.URL_HOMEPOST1 + cursor + LinkUrlApi.URL_HOMEPOST2;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    if (object != null){
                        JSONObject edge_web_feed_timeline = object.getJSONObject("data").getJSONObject("user")
                                .getJSONObject("edge_web_feed_timeline");
                        mGetPostHome.postHome(edge_web_feed_timeline);
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



    private PostHome mGetPostHome;

    public interface PostHome{
        void postHome(JSONObject edge_web_feed_timeline) throws JSONException;
    }

    public void setPostHome(PostHome getPostHome){
        this.mGetPostHome = getPostHome;
    }
}




