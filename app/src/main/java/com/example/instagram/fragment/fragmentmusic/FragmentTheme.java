package com.example.instagram.fragment.fragmentmusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.Common;
import com.example.instagram.R;
import com.example.instagram.adapter.adaptermusic.AdapterTheme;
import com.example.instagram.main.mainmusic.PlaySongsThemeActivity;
import com.example.instagram.model.Theme;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class FragmentTheme extends Fragment implements AdapterTheme.OnClickTheme {
    private View view;
    private RecyclerView recyclerView;
    private AsyncHttpClient client = new AsyncHttpClient();
    private ArrayList<Theme> themes;
    private AdapterTheme adapterTheme;

    public FragmentTheme() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_theme, container, false);
        init();
        getContentTheme();
        return view;
    }

    private void init() {
        recyclerView = view.findViewById(R.id.recyclerTheme);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        themes = new ArrayList<>();
        adapterTheme = new AdapterTheme(getContext(), themes);
        adapterTheme.setOnClickTheme(FragmentTheme.this);
    }

    private void getContentTheme(){
        client.addHeader("cookie", Common.COOKIE);
        client.get(Common.URL_THEME, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Document doc = Jsoup.parse(new String(responseBody));
                if(doc != null){
                    String imv, html;

                    Elements esBox = doc.select("div.box_topic_item_thumb");

                    for(Element e : esBox){
                        imv = e.getElementsByTag("img").first().attr("data-src");
                        html = e.getElementsByTag("a").first().attr("href");
                        themes.add(new Theme(imv, html));
                    }
                    recyclerView.setAdapter(adapterTheme);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    @Override
    public void onClickTheme(String urlHTMLTheme) {
        Intent intent = new Intent(getContext(), PlaySongsThemeActivity.class);
        intent.putExtra("htmlTheme", urlHTMLTheme);
        startActivity(intent);
    }
}
