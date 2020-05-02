package com.example.instagram.main.mainmusic;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instagram.R;
import com.example.instagram.database.MyDBLoveSong;

import com.example.instagram.model.Song;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayGameFunnyActivity extends AppCompatActivity {

    private ImageButton ibtnWatchImageSinger, ibtn5050, ibtnAsk;
    private ImageView imvStart, imvSinger;
    private TextView txtPoint;
    private Button btnAnswer0, btnAnswer1, btnAnswer2, btnAnswer3, btnCheck;
    private SeekBar skbTimeSong;
    private MyDBLoveSong db;
    private ArrayList<Song> songs, songsNew;
    private int positionAnswer = -1, positionRequest = 0, point = 0, time = 600;
    private Boolean starHope = false, check = false;
    private MediaPlayer mp = new MediaPlayer();
    private Random r;
    private CountDownTimer cdtStart;
    private int positionCorrect = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game_funny);
        setTitle("Game Vui");

        init();
        ramdonRequestSongs();
        setEnableButtonAnswer(false);

        ibtn5050.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnCheck.getText().equals("Check")){
                    positionAnswer = 0;
                    String s = "";
                    ibtn5050.setVisibility(View.INVISIBLE);
                    for (int i=0; i<2; i++){
                        int j = r.nextInt(4);
                        if (j == positionCorrect || s.contains(String.valueOf(j))){
                            i--;
                        }else {
                            switch (j) {
                                case 0:
                                    btnAnswer0.setEnabled(false);
                                    break;
                                case 1:
                                    btnAnswer1.setEnabled(false);
                                    break;
                                case 2:
                                    btnAnswer2.setEnabled(false);
                                    break;
                                case 3:
                                    btnAnswer3.setEnabled(false);
                                    break;
                            }
                        }
                        s += j;
                    }
                }
            }
        });

        ibtnWatchImageSinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnCheck.getText().equals("Check")){
                    ibtnWatchImageSinger.setVisibility(View.INVISIBLE);
                    Picasso.with(PlayGameFunnyActivity.this).load(songsNew.get(positionRequest).getAvatar())
                            .error(R.drawable.img_plist_full).into(imvSinger);
                }
            }
        });


        ibtnAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PlayGameFunnyActivity.this);
                builder.setTitle("Luật chơi: ");
                builder.setMessage("Bạn sẽ được nghe 10 bài hát tương đương với 15 câu hỏi lời " +
                        "\n Bạn phải trả lời thật nhanh trong 10 phút đâu là tên ca sĩ thể hiện bài hát đó" +
                        "\n Hết 10 phút mà bạn chưa trả lời xong thì bạn sẽ thua cuộc" +
                        "\n Hết 10 phút bạn trả lời xong 15 câu hỏi mà số điểm của bạn âm thì bạn thua cuộc" +
                        "\n Cách tính điểm:" +
                        "\n Trả lời đúng sẽ được cộng (+) 10 điểm nếu dùng ngôi sao huy vọng sẽ cộng (+) 20 điểm hoặc tăng gấp đôi só điểm (Nếu điểm bạn >0)" +
                        "\n Trả lời sai sẽ bị trù chia trừ 15 điểm và " +
                        "\n Nếu có ngôi sao huy vọng số điểm sẽ bị chia theo vị trí câu bạn đang trả lời (Nếu điểm bạn > 0) và bị trừ (-) 20 điểm nữa" +
                        "\n Sự trợ giúp: " +
                        "\n Có 2 quyền trợ giúp là 50:50 và xem ảnh ca sĩ "+
                        "\n Mỗi làn dùng sẽ bị trừ 10 điểm nếu có ngỗi sao trừ 15 điểm"+
                        "\n Chúc bạn chơi game vui vẻ"
                );
                builder.setPositiveButton("Đã hiểu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        clickButtonAnswer();
        clickImvStartHope();
    }

    private void setSKTime() {
        skbTimeSong.setEnabled(false);
        skbTimeSong.setMax(time);
        skbTimeSong.setProgress(time);
        new CountDownTimer(600000, 1000){

             @Override
             public void onTick(long millisUntilFinished) {
                 time --;
                 skbTimeSong.setProgress(time);
             }

             @Override
             public void onFinish() {
                check = false;
             }
         }.start();
    }

    private void setPointCorrect(Boolean starHope){
       if(starHope){
           if(point > 0){
               point *= 2;
           }else {
               point += 20;
           }
       }else {
           point +=10;
       }
       txtPoint.setText(String.valueOf(point));
    }

    private void setPointFall(Boolean starHope){
        if(starHope){
            if (point>0){
                point /= point;
            }

            point -= 20;
        }else {
            point -=15;
        }
        txtPoint.setText(String.valueOf(point));
    }

    private void clickImvStartHope() {
        imvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnCheck.getText().equals("Check")){
                    Toast.makeText(PlayGameFunnyActivity.this, "Bạn phải chọn trước khi bắt đầu câu hỏi mới",
                            Toast.LENGTH_SHORT).show();
                }else {
                    starHope = true;
                    countDownTimerStartHope();
                }
            }
        });

    }

    private void setTextButtonAnswer(int positionRequest){
        btnAnswer1.setText(null);
        btnAnswer2.setText(null);
        btnAnswer3.setText(null);
        btnAnswer0.setText(null);

        // set Vị trí button câu trả lời đúng
        positionCorrect = r.nextInt(4);
        switch (positionCorrect){
            case 0:
                btnAnswer0.setText(songsNew.get(positionRequest).getSinger());
                break;
            case 1:
                btnAnswer1.setText(songsNew.get(positionRequest).getSinger());
                break;
            case 2:
                btnAnswer2.setText(songsNew.get(positionRequest).getSinger());
                break;
            case 3:
                btnAnswer3.setText(songsNew.get(positionRequest).getSinger());
                break;
        }

        // Set vị trí những câu trả lời không đúng
        List<Integer> listChua = new ArrayList<>();

        for(int i=0; i<4; i++){
            int positionFall = r.nextInt(songs.size());
            if(listChua.contains(positionFall) &&
                    songs.get(positionFall).getSinger().equals(songsNew.get(positionRequest).getSinger())){
                i--;
            }else {
                listChua.add(positionFall);

                if (btnAnswer0.getText().equals("") && i == 0){
                    btnAnswer0.setText(songs.get(positionFall).getSinger());
                }

                if (btnAnswer1.getText().equals("") && i == 1){
                    btnAnswer1.setText(songs.get(positionFall).getSinger());
                }

                if (btnAnswer2.getText().equals("") && i == 2){
                    btnAnswer2.setText(songs.get(positionFall).getSinger());
                }

                if (btnAnswer3.getText().equals("") && i == 3){
                    btnAnswer3.setText(songs.get(positionFall).getSinger());
                }
            }
        }
        playMusic(songsNew.get(positionRequest).getLocation());
    }

    private void clickButtonAnswer() {
        btnAnswer0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBGSelectButtonWhitle();
                positionAnswer = 0;
                setBGSelectButtonYellow(btnAnswer0);
            }
        });

        btnAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBGSelectButtonWhitle();
                positionAnswer = 1;
                setBGSelectButtonYellow(btnAnswer1);
            }
        });

        btnAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBGSelectButtonWhitle();
                positionAnswer = 2;
                setBGSelectButtonYellow(btnAnswer2);
            }
        });

        btnAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBGSelectButtonWhitle();
                positionAnswer = 3;
                setBGSelectButtonYellow(btnAnswer3);
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int vt = -1;

                if (btnCheck.getText().equals("Start")) { vt = 0; setSKTime();}
                if (btnCheck.getText().equals("Check")) { vt = 1; }
                if (btnCheck.getText().equals("Continue")) { vt = 2; }

                switch (vt) {
                    case 0:
                        setEnableButtonAnswer(true);
                        btnCheck.setText("Check");
                        setTextButtonAnswer(positionRequest);
                        break;
                    case 1:
                        checkAnswer(positionAnswer);
                        if (positionAnswer != -1){
                            setEnableButtonAnswer(false);
                            btnCheck.setText("Continue");

                            if (mp.isPlaying()){
                                mp.stop();
                            }

                            if (starHope){
                                cdtStart.cancel();
                                imvStart.setVisibility(View.VISIBLE);
                            }
                            positionAnswer = -1;
                        }

                        break;
                    case 2:
                        positionRequest ++;

                        if (ibtnWatchImageSinger.getVisibility() == View.INVISIBLE){
                            imvSinger.setVisibility(View.INVISIBLE);
                        }

                        if (positionRequest == 11){
                            // Xong game
                        }else {
                            setEnableButtonAnswer(true);
                            setBGSelectButtonWhitle();
                            setTextButtonAnswer(positionRequest);
                            btnCheck.setText("Check");
                        }
                        break;
                }
            }
        });
    }

    private void checkAnswer(int positionAnswer){
        if (positionAnswer == -1){
            Toast.makeText(PlayGameFunnyActivity.this, "Vui lòng chọn 1 đáp án",
                    Toast.LENGTH_SHORT).show();
        }else {
            switch (positionCorrect){
                case 0:
                    setBGButtonCorrect(btnAnswer0);
                    setPointCorrect(starHope);
                    break;
                case 1:
                    setBGButtonCorrect(btnAnswer1);
                    setPointCorrect(starHope);
                    break;
                case 2:
                    setBGButtonCorrect(btnAnswer2);
                    setPointCorrect(starHope);
                    break;
                case 3:
                    setBGButtonCorrect(btnAnswer3);
                    setPointCorrect(starHope);
                    break;
            }
        }

        if (positionAnswer == positionCorrect){
            Toast.makeText(this, "Rất chính xác :))", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Sai rồi Lêu Nêu", Toast.LENGTH_SHORT).show();
            switch (positionAnswer){
                case 0:
                    setBGButtonFall(btnAnswer0);
                    setPointFall(starHope);
                    break;
                case 1:
                    setBGButtonFall(btnAnswer1);
                    setPointFall(starHope);
                    break;
                case 2:
                    setBGButtonFall(btnAnswer2);
                    setPointFall(starHope);
                    break;
                case 3:
                    setBGButtonFall(btnAnswer3);
                    setPointFall(starHope);
                    break;
            }
        }
    }

    private void setBGSelectButtonYellow(Button btn){
        btn.setBackgroundColor(getResources().getColor(R.color.color_answer_select));
    }

    private void setBGSelectButtonWhitle(){
        btnAnswer1.setBackgroundColor(getResources().getColor(R.color.color_answer_none));
        btnAnswer2.setBackgroundColor(getResources().getColor(R.color.color_answer_none));
        btnAnswer3.setBackgroundColor(getResources().getColor(R.color.color_answer_none));
        btnAnswer0.setBackgroundColor(getResources().getColor(R.color.color_answer_none));
    }

    private void playMusic(String url) {
        try {
            mp = new MediaPlayer();
            mp.setDataSource(url);
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {

        ibtnAsk = findViewById(R.id.ibtnAsk);
        ibtnWatchImageSinger = findViewById(R.id.ibtnWatchImageSinger);
        ibtn5050 = findViewById(R.id.ibtn5050);

        imvSinger = findViewById(R.id.imvSinger);

        btnAnswer0 = findViewById(R.id.btnAnswer1);
        btnAnswer1 = findViewById(R.id.btnAnswer2);
        btnAnswer2 = findViewById(R.id.btnAnswer3);
        btnAnswer3 = findViewById(R.id.btnAnswer4);
        btnCheck = findViewById(R.id.btnCheck);

        imvStart = findViewById(R.id.imvStarHope);
        txtPoint = findViewById(R.id.txtPoint);
        skbTimeSong = findViewById(R.id.skbTimeSong);

        r = new Random();
        songs = new ArrayList<>();

        db = new MyDBLoveSong(this);
        songs = db.getAllSong();

    }

    private void ramdonRequestSongs(){
        songsNew = new ArrayList<>();
        List<Integer> lChua = new ArrayList<>();

        for (int i=0 ; i<10; i++){
            int j = r.nextInt(songs.size());
            if (lChua.contains(j)){
                i--;
            }else {
                lChua.add(j);
                songsNew.add(new Song(songs.get(j).getSongKey(),
                        songs.get(j).getAvatar(),
                        songs.get(j).getTitle(),
                        songs.get(j).getSinger(),
                        songs.get(j).getLocation(),
                        songs.get(j).getTime()));
            }
        }
    }

    private void setBGButtonCorrect(final Button btn){
        btn.setBackgroundColor(getResources().getColor(R.color.color_answer_correct));
    }

    private void setBGButtonFall(final Button btn){
        btn.setBackgroundColor(getResources().getColor(R.color.color_answer_fall));
    }

    private void countDownTimerStartHope(){
        cdtStart = new CountDownTimer(10000000, 250) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (imvStart.getVisibility() == View.VISIBLE){
                    imvStart.setVisibility(View.INVISIBLE);
                }else {
                    imvStart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void setEnableButtonAnswer(Boolean bl){
        btnAnswer1.setEnabled(bl);
        btnAnswer2.setEnabled(bl);
        btnAnswer3.setEnabled(bl);
        btnAnswer0.setEnabled(bl);
    }

    @Override
    public void onBackPressed() {
        if (mp.isPlaying()){
            mp.stop();
        }
        super.onBackPressed();
    }
}
