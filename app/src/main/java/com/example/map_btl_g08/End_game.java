package com.example.map_btl_g08;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class End_game extends AppCompatActivity {
    Button btnPlayAgain;
    TextView tvScore_1;
    TextView tvBestScore_1;
    int bestScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        // anh xa id
        btnPlayAgain = findViewById(R.id.btnPlayagain);
        tvScore_1 = findViewById(R.id.tvScore_1);
        tvBestScore_1 = findViewById(R.id.tvBestScore_1);
        // Nhan Intent
        Intent myIntent = getIntent();
        // Lay bundel ra khoi Intent
        Bundle myBundle = myIntent.getBundleExtra("mypackage");
        // Lay du lieu khoi Bundle
        int score = myBundle.getInt("score");
        int bestscore = myBundle.getInt("bestscore");
        // Hien ket qua
        tvScore_1.setText(String.valueOf(score));
        tvBestScore_1.setText(String.valueOf(bestscore));

        // xu ly su kien click
        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khai bao inntent
                Intent myIntent = new Intent(End_game.this, PlayActivity.class);
                // Khoi dong
                startActivity(myIntent);
            }
        });
    }
}