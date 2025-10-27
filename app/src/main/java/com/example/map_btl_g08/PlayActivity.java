package com.example.map_btl_g08;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlayActivity extends AppCompatActivity {
    ImageView[] moles;
    TextView tvScore;
    TextView tvTimer;
    Button startBtn;
    int score = 0;
    int currentMole = -1;
    Handler handler = new Handler();
    private Runnable moleRunnable;

    long gameDurationMs = 60000;   // 30s
    long initialInterval = 800;    // tốc độ ban đầu (ms)
    long minInterval = 300;        // nhanh nhất
    long moleInterval = initialInterval;

    CountDownTimer countDownTimer;
    boolean isPlaying = false;
    private Random random = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        // Anh xa id
        tvScore = findViewById(R.id.tvScore);
        tvTimer = findViewById(R.id.tvTimer);

        moles = new ImageView[]{
                findViewById(R.id.moleTop1),
                findViewById(R.id.moleTop2),
                findViewById(R.id.moleTop3),
                findViewById(R.id.moleBelow1),
                findViewById(R.id.moleBelow2),
                findViewById(R.id.moleBelow3),
                findViewById(R.id.moleBottom1),
                findViewById(R.id.moleBottom2),
                findViewById(R.id.moleBottom3)
        };

        // Gan su kien click cho tung mole
        for (int i = 0; i < moles.length; i++) {
            int index = i;
            moles[i].setOnClickListener(v -> {
                if (isPlaying && index == currentMole && moles[index].getVisibility() == View.VISIBLE) {
                    score++;
                    updateScore();
                    hideMole(index);
                    currentMole = -1;
                }
            });
        }

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) startGame();
                else stopGame();
            }
        });

        updateScore();
        tvTimer.setText((gameDurationMs / 1000) + "s");
    }
    public void startGame() {
        score = 0;
        updateScore();

        moleInterval = initialInterval;
        isPlaying = true;
//        startBtn.setText("Stop");

        startTimer();
        startMoleSpawner();
    }

    public void stopGame() {
        isPlaying = false;
//        startBtn.setText("Start");
        if (countDownTimer != null) countDownTimer.cancel();
        stopMoleSpawner();
        hideAllMoles();
    }

    public void startTimer() {
        if (countDownTimer != null) countDownTimer.cancel();

        countDownTimer = new CountDownTimer(gameDurationMs, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;
                tvTimer.setText(secondsLeft + "s");

//                // Tang độ khó (giảm thời gian xuất hiện)
//                double elapsed = (double) (gameDurationMs - millisUntilFinished);
//                double ratio = elapsed / gameDurationMs;
//                moleInterval = (long) (initialInterval - (initialInterval - minInterval) * ratio);
//                if (moleInterval < minInterval) moleInterval = minInterval;
            }

            @Override
            public void onFinish() {
                tvTimer.setText("0s");
                isPlaying = false;
                stopMoleSpawner();
                hideAllMoles();
//                startBtn.setText("Start");
                showGameOverDialog();
            }
        }.start();
    }

    public void startMoleSpawner() {
        moleRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isPlaying) return;
                showRandomMole();
                handler.postDelayed(this, moleInterval);
            }
        };
        handler.post(moleRunnable);
    }

    public void stopMoleSpawner() {
        if (moleRunnable != null) handler.removeCallbacks(moleRunnable);
        moleRunnable = null;
    }

    public void showRandomMole() {
        hideCurrentMole();
        int idx = random.nextInt(moles.length);
        currentMole = idx;
        moles[idx].setVisibility(View.VISIBLE);

        // Tu dong an sau nua thoi gian interval
        handler.postDelayed(() -> {
            if (currentMole == idx) {
                hideMole(idx);
                currentMole = -1;
            }
        }, Math.max(moleInterval / 2, 300));
    }

    public void hideMole(int idx) {
        if (idx >= 0 && idx < moles.length) {
            moles[idx].setVisibility(View.INVISIBLE);
        }
    }

    public void hideCurrentMole() {
        if (currentMole >= 0 && currentMole < moles.length) {
            hideMole(currentMole);
            currentMole = -1;
        }
    }

    public void hideAllMoles() {
        for (ImageView mole : moles) mole.setVisibility(View.INVISIBLE);
        currentMole = -1;
    }

    public void updateScore() {
        tvScore.setText(score);
    }

    public void showGameOverDialog() {
        // Khai bao Intent
        Intent callContinue = new Intent(PlayActivity.this, Continue.class);
        // Khoi dong
        startActivity(callContinue);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isPlaying) stopGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
        stopMoleSpawner();
    }
}