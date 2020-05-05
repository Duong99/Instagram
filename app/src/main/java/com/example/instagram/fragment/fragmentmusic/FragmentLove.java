package com.example.instagram.fragment.fragmentmusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.DownloadFile;
import com.example.instagram.R;
import com.example.instagram.adapter.adaptermusic.AdapterSongLove;
import com.example.instagram.database.MyDBLoveSong;
import com.example.instagram.main.mainmusic.PlayGameFunnyActivity;
import com.example.instagram.main.mainmusic.PlayMusicActivity;
import com.example.instagram.model.Song;

import java.util.ArrayList;


public class FragmentLove extends Fragment implements AdapterSongLove.OnClickPlayListenSong{
    private View view;
    private MyDBLoveSong myDBLoveSong;
    private ArrayList<Song> songs;
    private RecyclerView recyclerLove;
    private AdapterSongLove adapterSongLove;
    private Button btnPlayGame;

    public FragmentLove() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_love, container, false);

        init();

        btnPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PlayGameFunnyActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void init() {
        btnPlayGame = view.findViewById(R.id.btnPlayGame);

        songs = new ArrayList<>();
        recyclerLove = view.findViewById(R.id.recyclerLove);
        recyclerLove.setHasFixedSize(true);
        recyclerLove.setLayoutManager(new LinearLayoutManager(getContext()));

        myDBLoveSong = new MyDBLoveSong(getContext());
        songs = myDBLoveSong.getAllSong();
        adapterSongLove = new AdapterSongLove(getContext(), songs);
        recyclerLove.setAdapter(adapterSongLove);
        adapterSongLove.setOnClickSong(FragmentLove.this);
    }

    private void getReturnListLove(){
        songs = myDBLoveSong.getAllSong();
        adapterSongLove = new AdapterSongLove(getContext(), songs);
        adapterSongLove.setOnClickSong(FragmentLove.this);
        recyclerLove.setAdapter(adapterSongLove);
    }

    @Override
    public void onClickNotLikeSong(String key, Song song) {
        myDBLoveSong.delete(key);
        if(myDBLoveSong.getSong(key) == null){
            Toast.makeText(getContext(), R.string.delete_song_success, Toast.LENGTH_SHORT).show();
            getReturnListLove();
        }else {
            Toast.makeText(getContext(), R.string.delete_song_fall, Toast.LENGTH_SHORT).show();
        }
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
        Intent intent = new Intent(getContext(), PlayMusicActivity.class);

        intent.putExtra("position", position);
        intent.putExtra("QuestionListExtra", listSong);

        startActivity(intent);
    }

    @Override
    public void downMusic(String url) {
        new DownloadFile(getContext(), url).startDownloadImage();
    }
}
