package com.example.map_btl_g08;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Continue extends AppCompatActivity {
    Button btnNo;
    Button btnYes;
    int bestScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue);
        // anh xa id
        btnYes = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);

        // Load lich su
        HistoryManager.addHistory(this, "Continue");
        // Nhan Intent
        Intent myIntent = getIntent();
        // Lay bundel ra khoi Intent
        Bundle myBundle = myIntent.getBundleExtra("mypackage");
        // Lay du lieu khoi Bundle
        int score = myBundle.getInt("score");

        // Xu ly su kien click
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khai bao Intent
                Intent myIntent_Yes = new Intent(Continue.this, PlayActivity.class);
                // Truyen lai diem vao PlayActivity
                myIntent_Yes.putExtra("score_continue", score);
                myIntent_Yes.putExtra("time_continue", 20000L);
                myIntent_Yes.putExtra("is_continue", true);
                // Khoi dong
                startActivity(myIntent_Yes);
                finish();
            }
        });
        // Xu ly su kien click
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // XU LY BEST_SCORE
                // Đọc điểm cao nhất từ SharedPreferences
                SharedPreferences prefs = getSharedPreferences("game_data", MODE_PRIVATE);
                bestScore = prefs.getInt("best_score", 0);

                // So sánh và cập nhật nếu điểm mới cao hơn
                if (score > bestScore) {
                    bestScore = score;
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("best_score", bestScore);
                    editor.apply();
                    // Khai bao Intent
                    Intent myIntent_No = new Intent(Continue.this, Best_Score.class);
                    // lay du lieu score
                    int bestscore_1 = bestScore;
                    // Dong goi du lieu vao Bundle
                    Bundle myscore_1 = new Bundle();
                    Bundle mybestScore_1 = new Bundle();
                    // Dua du lieu vao Bundle
                    myscore_1.putInt("bestscore", bestscore_1);
                    // Dua bundle vao Intent
                    myIntent_No.putExtra("mypackage", myscore_1);
                    // Khoi dong
                    startActivity(myIntent_No);
                }else{
                    // Khai bao Intent
                    Intent myIntent_No = new Intent(Continue.this, End_game.class);
                    // lay du lieu score
                    int score_1 = score;
                    int bestscore_1 = bestScore;
                    // Dong goi du lieu vao Bundle
                    Bundle myscore_1 = new Bundle();
                    // Dua du lieu vaom Bundle
                    myscore_1.putInt("score", score_1);
                    myscore_1.putInt("bestscore", bestscore_1);
                    // Dua bundle vao Intent
                    myIntent_No.putExtra("mypackage", myscore_1);
                    // Khoi dong
                    startActivity(myIntent_No);
                }
            }
        });
    }
}