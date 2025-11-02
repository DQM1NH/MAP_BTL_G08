package com.example.map_btl_g08;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Privacy_policy extends AppCompatActivity {
    ImageView btnClose;
    TextView tvPrivacy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        // anh xa id
        btnClose = findViewById(R.id.btnClose);
        tvPrivacy = findViewById(R.id.tvPrivacy_first);
        tvPrivacy.setText(getString(R.string.text_privacy));
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thoat ra
                finish();
            }
        });
    }
}