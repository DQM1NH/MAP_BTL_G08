package com.example.map_btl_g08;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Tongdiem_Score extends AppCompatActivity {

    private TextView txtScore, txtTime, txtHighScore, txtTimeHighScore;
    private Button btnStartGame, btnOption, btnQuit;
    private ImageView imgHome;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "GamePrefs";
    private static final String KEY_HIGH_SCORE = "high_score";
    private static final String KEY_BEST_TIME = "best_time";
    private static final String KEY_BEST_TIME_MS = "best_time_ms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongdiem);

        // Ánh xạ ID
        anhXaId();

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Nhận dữ liệu từ Intent
        nhanDuLieuGame();

        // Thiết lập sự kiện
        setupClickListeners();
    }

    private void anhXaId() {
        imgHome = findViewById(R.id.img_home);
        txtScore = findViewById(R.id.txt_score);
        txtTime = findViewById(R.id.txt_time);
        txtHighScore = findViewById(R.id.txt_highscore);
        txtTimeHighScore = findViewById(R.id.txt_timehighscore);
        btnStartGame = findViewById(R.id.btn_td_stargame);
        btnOption = findViewById(R.id.btn_option);
        btnQuit = findViewById(R.id.btn_quit);
    }

    private void nhanDuLieuGame() {
        Intent intent = getIntent();
        int currentScore = intent.getIntExtra("SCORE", 0);
        String gameTime = intent.getStringExtra("GAME_TIME");
        long elapsedTimeMs = intent.getLongExtra("ELAPSED_TIME_MS", 0);

        // Hiển thị kết quả game hiện tại
        txtScore.setText("Score: " + currentScore);
        if (gameTime != null) {
            txtTime.setText("Time: " + gameTime);
        }

        // Xử lý High Score
        xuLyHighScore(currentScore, elapsedTimeMs);
    }

    private void xuLyHighScore(int currentScore, long currentTimeMs) {
        // Lấy high score cũ
        int oldHighScore = sharedPreferences.getInt(KEY_HIGH_SCORE, 0);
        long oldBestTimeMs = sharedPreferences.getLong(KEY_BEST_TIME_MS, Long.MAX_VALUE);
        String oldBestTime = sharedPreferences.getString(KEY_BEST_TIME, "0:00");

        // Kiểm tra và cập nhật high score
        if (currentScore > oldHighScore) {
            // Điểm cao mới
            luuHighScore(currentScore, currentTimeMs);
            txtHighScore.setText("High Score: " + currentScore);
            txtTimeHighScore.setText("Time: " + formatTime(currentTimeMs));
        } else if (currentScore == oldHighScore && currentTimeMs < oldBestTimeMs) {
            // Điểm bằng nhưng thời gian ngắn hơn
            luuHighScore(currentScore, currentTimeMs);
            txtHighScore.setText("High Score: " + currentScore);
            txtTimeHighScore.setText("Time: " + formatTime(currentTimeMs));
        } else {
            // Giữ nguyên high score cũ
            txtHighScore.setText("High Score: " + oldHighScore);
            txtTimeHighScore.setText("Time: " + oldBestTime);
        }
    }

    private void luuHighScore(int score, long timeMs) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_HIGH_SCORE, score);
        editor.putLong(KEY_BEST_TIME_MS, timeMs);
        editor.putString(KEY_BEST_TIME, formatTime(timeMs));
        editor.apply();
    }

    private String formatTime(long timeMs) {
        long seconds = timeMs / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    private void setupClickListeners() {
        // Nút Home - quay về Start Game
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tongdiem_Score.this, Start_Game.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        // Nút Start Game - chơi lại
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tongdiem_Score.this, Tinhdiem_Score.class);
                startActivity(intent);
                finish();
            }
        });

        // Nút Option - có thể thêm chức năng sau
        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thêm chức năng option tại đây
            }
        });

        // Nút Quit - thoát game
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity(); // Thoát hoàn toàn ứng dụng
            }
        });
    }
}