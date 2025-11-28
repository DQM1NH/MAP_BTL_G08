package com.example.map_btl_g08;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash_Screen_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // Load lich su
        HistoryManager.addHistory(this, "Splash_Sceen_Activity");

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Splash_Screen_Activity.this, Start_Game.class);
            startActivity(intent);
            finish();   // đóng splash screen
        }, 4000);
    }
}