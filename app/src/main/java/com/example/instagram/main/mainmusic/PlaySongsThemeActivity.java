package com.example.instagram.main.mainmusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.LinkUrlApi;
import com.example.instagram.DownloadFile;
import com.example.instagram.R;
import com.example.instagram.adapter.adaptermusic.AdapterSongLove;
import com.example.instagram.database.MyDBLoveSong;
import com.example.instagram.model.Song;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PlaySongsThemeActivity extends AppCompatActivity implements AdapterSongLove.OnClickPlayListenSong{
    private TextView txtTitleThemePlay, txtContenTitleThemeplay, txtViewMoreThemePlay;
    private RecyclerView recyclerViewThemePlay;
    private ImageView imvThemePlay;
    private AsyncHttpClient client = new AsyncHttpClient();
    private ArrayList<String> listEncryptKey;
    private AdapterSongLove adapterSong;
    private ArrayList<Song> songs;
    private int positon = 0;
    private MyDBLoveSong db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_songs_theme);

        db = new MyDBLoveSong(this);
        init();

        getUrlHtmlTheme();

        txtViewMoreThemePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positon ++;
                if(positon == listEncryptKey.size() - 1){
                    getSongs(listEncryptKey.get(positon));
                    txtViewMoreThemePlay.setVisibility(View.INVISIBLE);
                }else {
                    getSongs(listEncryptKey.get(positon));
                }
            }
        });

    }
    private void getSongs(String url){
        client.addHeader("cookie", LinkUrlApi.COOKIE);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    if (object != null){
                        JSONArray listItem = object.getJSONObject("data").getJSONArray("listItem");
                        if(listItem != null){
                             String songKey, avatar, title, singer, location, time;
                            for (int i=0; i<listItem.length(); i++){
                                songKey = listItem.getJSONObject(i).getString("songKey");
                                avatar = listItem.getJSONObject(i).getString("avatar");
                                title = listItem.getJSONObject(i).getString("title");
                                singer = listItem.getJSONObject(i).getString("singerTitle");
                                location = listItem.getJSONObject(i).getString("location");
                                time = listItem.getJSONObject(i).getString("time");

                                songs.add(new Song(songKey, avatar, title, singer, location, time));
                            }
                            recyclerViewThemePlay.setAdapter(adapterSong);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(PlaySongsThemeActivity.this, "Check your network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUrlHtmlTheme() {
        Intent intent = getIntent();
        client.addHeader("cookie", LinkUrlApi.COOKIE);
        String url = intent.getStringExtra("htmlTheme");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Document doc = Jsoup.parse(new String(responseBody));
                if (doc != null){
                    Picasso.with(PlaySongsThemeActivity.this).load(doc.select("div.slider_thumb").select("img")
                            .first().attr("src")).error(R.drawable.topic_loading).into(imvThemePlay);

                    txtTitleThemePlay.setText(doc.select("h2.title_genre_main").first().text());
                    setTitle(txtTitleThemePlay.getText().toString());
                    txtContenTitleThemeplay.setText(doc.select("p.text_demo").first().text());

                    Elements esEncryptKey = doc.select("li.playlist_item_single").select("div.item_thumb");
                    if(esEncryptKey != null){
                        String key;
                        for(Element e : esEncryptKey){
                            key = e.getElementsByTag("span").last().attr("keyencrypt");
                            listEncryptKey.add("https://www.nhaccuatui.com/ajax/get-media-info?key2=" + key);
                        }
                        getSongs(listEncryptKey.get(positon));
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void init() {

        recyclerViewThemePlay = findViewById(R.id.recyclerThemePlay);
        recyclerViewThemePlay.setHasFixedSize(true);
        recyclerViewThemePlay.setLayoutManager(new LinearLayoutManager(this));

        listEncryptKey = new ArrayList<>();
        songs = new ArrayList<>();
        adapterSong = new AdapterSongLove(PlaySongsThemeActivity.this, songs);
        txtContenTitleThemeplay = findViewById(R.id.txtContentTitleThemePlay);
        txtTitleThemePlay = findViewById(R.id.txtTitleThemePlay);
        txtViewMoreThemePlay = findViewById(R.id.txtViewMoreThemePlay);
        imvThemePlay = findViewById(R.id.imvThemePlay);

        adapterSong.setOnClickSong(PlaySongsThemeActivity.this);
    }


    @Override
    public void onCickPlayListenSong(int position, ArrayList<Song> songs) {
        ArrayList<String> listSong = new ArrayList<>();
        for (int i=0; i<songs.size(); i++){
            listSong.add(songs.get(i).getAvatar() + "---"
                    + songs.get(i).getTitle() +"---"
                    + songs.get(i).getSinger() + "---"
                    + songs.get(i).getLocation()+ "---"
                    + songs.get(i).getTime()) ;

        }
        Intent intent = new Intent(this, PlayMusicActivity.class);

        intent.putExtra("position", position);
        intent.putExtra("QuestionListExtra", listSong);

        startActivity(intent);
    }

    @Override
    public void onClickNotLikeSong(String key, Song song) {
        if(db.getSong(key) == null){
            db.addSong(song);

            if(db.getSong(key) != null){
                Toast.makeText(this, getResources().getString(R.string.add_song_success), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, getResources().getString(R.string.add_song_fall), Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Bạn đã thích bài này rồi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void downMusic(String url) {
        new DownloadFile(this, url);
    }
}
