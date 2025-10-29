package com.example.map_btl_g08;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Start_Game extends AppCompatActivity {
    Button btnPlay;
    Button btnTutorial;
    TextView tvPrivacy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        // anh xa id
        btnPlay = findViewById(R.id.btnPlay);
        btnTutorial = findViewById(R.id.btnTutorial);
        tvPrivacy = findViewById(R.id.tvPricacy);
        // Xu ly su kien cho button
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Khai bao inntent
                Intent callStart = new Intent(Start_Game.this, PlayActivity.class);
                // Khoi dong
                startActivity(callStart);
            }
        });


        btnTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khai bao Intent
                Intent myTutorial = new Intent(Start_Game.this, Tutorial.class);
                // Khoi dong
                startActivity(myTutorial);
            }
        });

        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khai bao Intent
                Intent myPrivacy = new Intent(Start_Game.this, Privacy_policy.class);
                // Khoi dong
                startActivity(myPrivacy);
            }
        });
    }
}