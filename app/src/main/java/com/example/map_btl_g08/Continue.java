package com.example.map_btl_g08;

import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue);
        btnYes = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // Khai bao Intent
//                Intent myIntent_Yes = new Intent(Continue.this, PlayActivity.class);
//                // Khoi dong
//                startActivity(myIntent_Yes);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khai bao Intent
                Intent myIntent_No = new Intent(Continue.this, End_game.class);
                // Khoi dong
                startActivity(myIntent_No);
            }
        });
    }
}