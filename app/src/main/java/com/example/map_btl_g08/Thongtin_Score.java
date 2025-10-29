package com.example.map_btl_g08;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Thongtin_Score extends AppCompatActivity {


     ImageView img_home;
     Button btn_continue; // THÊM NÚT CONTINUE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtindiem);

        img_home = findViewById(R.id.img_home);
        btn_continue = findViewById(R.id.btn_continue);

        // Xử lý sự kiện click cho nút HOME - QUAY VỀ TRANG ĐẦU
        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // THÊM: Xử lý sự kiện click cho nút CONTINUE
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình tính điểm (activity_tinhdiem.xml)
                Intent intent = new Intent(Thongtin_Score.this, Tinhdiem_Score.class);
                startActivity(intent);
            }
        });
    }
}