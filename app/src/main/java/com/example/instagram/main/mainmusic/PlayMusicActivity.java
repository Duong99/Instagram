package com.example.instagram.main.mainmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.fragment.fragmentmusic.FragmentLove;
import com.example.instagram.model.Song;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayMusicActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView circleImageView;
    private TextView txtSingerSong, txtTitleSong;
    private ImageButton ibtnPrevious, ibtnPause, ibtnNext;
    private SeekBar seekBarTime;
    private ArrayList<Song> mSongs;
    private int mPosition, startTime = 0;
    private MediaPlayer mediaPlayer;
    private Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        setTitle("Music");

        init();

        getDataIntent();

        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void playMusic(int position){
        try {
            String url = mSongs.get(position).getLocation();// your URL here
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
            mediaPlayer.start();

            seekBarTime.setMax(mediaPlayer.getDuration());
            UpdateSongTime.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void getDataIntent() {
        Intent intent = getIntent();
        mPosition = intent.getIntExtra("position", -1);
        ArrayList<String> mListSong = new ArrayList<>();
        mListSong = intent.getStringArrayListExtra("QuestionListExtra");

        for (int i=0; i<mListSong.size(); i++){
            String[] words = mListSong.get(i).split("---");//tach chuoi dua tren khoang trang
            //su dung vong lap foreach de in cac element cua mang chuoi thu duoc
            mSongs.add(new Song("", words[0], words[1],words[2],words[3], words[4]));

        }

        playMusic(mPosition);
        nextPreviousSong(mPosition);
    }

    private void nextPreviousSong(int position){
        Picasso.with(this).load(mSongs.get(position).getAvatar()).into(circleImageView);
        txtSingerSong.setText(mSongs.get(position).getSinger());
        txtTitleSong.setText(mSongs.get(position).getTitle());
    }

    private void init() {
        circleImageView = findViewById(R.id.circleImv);
        txtSingerSong = findViewById(R.id.txtSingerSong);
        txtTitleSong = findViewById(R.id.txtTitleSong);

        ibtnNext = findViewById(R.id.ibtnNext);
        ibtnPause = findViewById(R.id.ibtnPauseMusic);
        ibtnPrevious = findViewById(R.id.ibtnPrevious);

        ibtnNext.setOnClickListener(this);
        ibtnPause.setOnClickListener(this);
        ibtnPrevious.setOnClickListener(this);

        seekBarTime = findViewById(R.id.seekBarTime);

        mSongs = new ArrayList<>();

        RotateAnimation animation = new RotateAnimation(0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animation.setDuration(10000);
        animation.setRepeatCount(Animation.INFINITE);
        circleImageView.startAnimation(animation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibtnPrevious:
                if (!(mPosition == 0)){
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }

                    mPosition -= 1;
                    playMusic(mPosition);
                    nextPreviousSong(mPosition);
                }
                break;
            case R.id.ibtnPauseMusic:
                if (mediaPlayer.isPlaying()){
                    ibtnPause.setBackgroundResource(R.drawable.ic_media_play_light);
                    mediaPlayer.pause();
                }else {
                    ibtnPause.setBackgroundResource(R.drawable.ic_media_pause_light);
                    mediaPlayer.start();
                }
                break;
            case R.id.ibtnNext:
                if (!(mPosition == mSongs.size())){

                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }

                    mPosition +=1;
                    playMusic(mPosition);
                    nextPreviousSong(mPosition);
                }
                break;
        }
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            seekBarTime.setProgress(startTime);
            myHandler.postDelayed(this, 100);
        }
    };
}
