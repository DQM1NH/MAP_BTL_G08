package com.example.map_btl_g08;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class History_Activity extends AppCompatActivity {
    ListView lv;
    Button btnClear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // anh xa id
        lv = findViewById(R.id.lv);
        btnClear = findViewById(R.id.btnClear);

        loadHistory();
        // Xu ly su kien click
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryManager.clearHistory(History_Activity.this);
                loadHistory();
            }
        });

    }
    private void loadHistory() {
        List<String> historyList = HistoryManager.getHistory(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyList);
        lv.setAdapter(adapter);
    }
}