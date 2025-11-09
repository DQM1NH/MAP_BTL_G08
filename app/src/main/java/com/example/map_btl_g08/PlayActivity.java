package com.example.map_btl_g08;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;
import androidx.appcompat.app.AppCompatActivity;

public class PlayActivity extends AppCompatActivity {
    ImageView[] moles;
    TextView tvScore;
    TextView tvTimer;
//    Button startBtn;
    int score = 0;
    int currentMole = -1;
    int bestScore;
    Handler handler = new Handler();
    private Runnable moleRunnable;

    long gameDurationMs = 60000;   // 60s
    long initialInterval = 1500;    // tốc độ ban đầu (ms)
    long moleInterval = initialInterval;

    CountDownTimer countDownTimer;
    boolean isPlaying = false;
    private boolean hasContinued = false;
    final Random random = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // Ánh xạ view
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

        // Lấy dữ liệu khi người chơi chọn “Continue”
        Intent intent = getIntent();
        int scoreContinue = intent.getIntExtra("score_continue", 0);
        long timeContinue = intent.getLongExtra("time_continue", 0);
        boolean isContinue = intent.getBooleanExtra("is_continue", false);

        if (scoreContinue > 0) {
            score = scoreContinue;
        }
        if (timeContinue > 0) {
            gameDurationMs = timeContinue;
        }
        if (isContinue) {
            hasContinued = true;
        }

        updateScore();
        tvTimer.setText((gameDurationMs / 1000) + "s");

        // Sự kiện bấm vào chuột
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

        // Kiem tra neu co save game truoc do
        SharedPreferences prefs = getSharedPreferences("game_save", MODE_PRIVATE);
        boolean wasPlaying = prefs.getBoolean("isPlaying", false);

        if (wasPlaying) {
            // Co tien trinh cu -> khoi phuc
            score = prefs.getInt("score", 0);
            gameDurationMs = prefs.getLong("timeLeft", 60000);
            hasContinued = prefs.getBoolean("hasContinued", false);

            updateScore();
            tvTimer.setText((gameDurationMs / 1000) + "s");

            // Hoi nguoi choi co muon tiep tuc khong
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Tiep tuc choi?")
                    .setMessage("Ban co muon tiep tuc choi?")
                    .setPositiveButton("Co", (dialog, which) -> {
                        startGame(); // tiep tuc
                    })
                    .setNegativeButton("Khong", (dialog, which) -> {
                        clearSavedGame(); // xoa save
                        SharedPreferences prefs1 = getSharedPreferences("game_data", MODE_PRIVATE);
                        bestScore = prefs1.getInt("best_score", 0);

                        // So sánh và cập nhật nếu điểm mới cao hơn
                        if (score > bestScore) {
                            bestScore = score;
                            SharedPreferences.Editor editor = prefs1.edit();
                            editor.putInt("best_score", bestScore);
                            editor.apply();
                            // Khai bao Intent
                            Intent myIntent_No = new Intent(PlayActivity.this, Best_Score.class);
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
                            Intent myIntent_No = new Intent(PlayActivity.this, End_game.class);
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
                    })
                    .setCancelable(false)
                    .show();
            return;
        }
        startGame();
    }
    public void startGame() {
        if (score == 0) {
            updateScore();
        }

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
        }, Math.max(moleInterval / 2, 1000));
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
        tvScore.setText(String.valueOf(score));
    }

    public void showGameOverDialog() {
        clearSavedGame();
        if (!hasContinued) {
            // Khai bao Intent
            Intent callContinue = new Intent(PlayActivity.this, Continue.class);
            // lay du lieu score
            int score = Integer.parseInt(tvScore.getText().toString());
            // Dong goi du lieu vao Bundle
            Bundle myscore = new Bundle();
            // Dua du lieu vaom Bundle
            myscore.putInt("score", score);
            // Dua bundle vao Intent
            callContinue.putExtra("mypackage", myscore);
            // Khoi dong
            startActivity(callContinue);
        }else{
            SharedPreferences prefs = getSharedPreferences("game_data", MODE_PRIVATE);
            bestScore = prefs.getInt("best_score", 0);

            // So sánh và cập nhật nếu điểm mới cao hơn
            if (score > bestScore) {
                bestScore = score;
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("best_score", bestScore);
                editor.apply();
                // Khai bao Intent
                Intent myIntent_No = new Intent(PlayActivity.this, Best_Score.class);
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
                Intent myIntent_No = new Intent(PlayActivity.this, End_game.class);
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
    }
    public void saveGameState(long timeLeftMs){
        SharedPreferences prefs = getSharedPreferences("game_save", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("score", score);
        editor.putLong("timeLeft", timeLeftMs);
        editor.putBoolean("isPlaying", isPlaying);
        editor.putBoolean("hasContinued", hasContinued);
        editor.apply();
    }
    public void clearSavedGame() {
        SharedPreferences prefs = getSharedPreferences("game_save", MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isPlaying && countDownTimer != null) {
            countDownTimer.cancel();
            long currentTimeLeft = 0;
            try {
                String timeText = tvTimer.getText().toString().replace("s", "");
                currentTimeLeft = Long.parseLong(timeText) * 1000;
            } catch (Exception e) {
                currentTimeLeft = 0;
            }
            saveGameState(currentTimeLeft);
        }
        stopMoleSpawner();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
        stopMoleSpawner();
    }
}