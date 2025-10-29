package com.example.map_btl_g08;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class Tinhdiem_Score extends AppCompatActivity {
    private long startTime;
    private long elapsedTime;
    private ImageView imgStop, imgLoa, imgX;
    private TextView txtHighScore, txtTime, txtScore;
    private ImageView[] moles;

    private int score = 0;
    private int currentMole = -1;
    private boolean isPlaying = false;

    private Handler handler = new Handler();
    private Runnable moleRunnable;
    private CountDownTimer countDownTimer;
    private Random random = new Random();

    // ĐIỀU CHỈNH: Tăng thời gian game và thời gian chuột xuất hiện
    private long gameDurationMs = 30000; // 120 giây (2 phút)
    private long moleInterval = 2000;     // Tăng lên 1.5 giây giữa các lần xuất hiện
    private long moleVisibleTime = 500;  // Chuột hiển thị 1.2 giây (CHẬM HƠN)

    // ĐIỀU CHỈNH: Tăng điểm số
    private static final int NORMAL_MOLE_SCORE = 50;    // Tăng từ 10 lên 50
    private static final int SPECIAL_MOLE_SCORE = 100;  // Điểm cho chuột đặc biệt
    private static final int BONUS_MOLE_SCORE = 200;    // Điểm bonus

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinhdiem);

        anhXaId();
        khoiTaoMoles();
        setupClickListeners();
        startGame();
    }

    private void anhXaId() {
        imgStop = findViewById(R.id.img_stop);
        imgLoa = findViewById(R.id.img_loa);
        imgX = findViewById(R.id.img_x);
        txtHighScore = findViewById(R.id.txt_highscore);
        txtTime = findViewById(R.id.txt_time);
        txtScore = findViewById(R.id.txt_score);
    }

    private void khoiTaoMoles() {
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
    }

    private void setupClickListeners() {
        imgX.setOnClickListener(v -> finish());

        imgStop.setOnClickListener(v -> {
            if (isPlaying) {
                endGame();
            }
        });

        imgLoa.setOnClickListener(v -> {
            // Xử lý âm thanh tại đây
        });

        // Sự kiện click cho từng chuột - TÍNH ĐIỂM CAO HƠN
        for (int i = 0; i < moles.length; i++) {
            int index = i;
            moles[i].setOnClickListener(v -> {
                if (isPlaying && index == currentMole && moles[index].getVisibility() == View.VISIBLE) {
                    // TĂNG ĐIỂM: Mỗi chuột +50 điểm thay vì +10
                    score += NORMAL_MOLE_SCORE;
                    updateScore();
                    hideMole(index);
                    currentMole = -1;

                    // THÊM: Hiệu ứng khi click trúng (tuỳ chọn)
                    showClickEffect(moles[index]);
                }
            });
        }
    }

    public void startGame() {
        score = 0;
        updateScore();
        isPlaying = true;

        txtTime.setText("2:00"); // Cập nhật thời gian hiển thị
        startTimer();
        startMoleSpawner();
    }

    public void endGame() {
        isPlaying = false;
        stopMoleSpawner();
        hideAllMoles();

        // Tính thời gian đã chơi
        elapsedTime = System.currentTimeMillis() - startTime;
        long secondsPlayed = elapsedTime / 1000;
        long minutes = secondsPlayed / 60;
        long seconds = secondsPlayed % 60;
        String gameTime = String.format("%d:%02d", minutes, seconds);

        // Chuyển đến màn hình tổng điểm và truyền dữ liệu
        Intent intent = new Intent(Tinhdiem_Score.this, Tongdiem_Score.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("GAME_TIME", gameTime);
        intent.putExtra("ELAPSED_TIME_MS", elapsedTime);
        startActivity(intent);
        finish();
    }

    public void startTimer() {
        if (countDownTimer != null) countDownTimer.cancel();

        countDownTimer = new CountDownTimer(gameDurationMs, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                String timeLeft = String.format("%d:%02d", minutes, seconds);
                txtTime.setText(timeLeft);
            }

            @Override
            public void onFinish() {
                txtTime.setText("0:00");
                endGame(); // Gọi endGame thay vì finish()
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

        // ĐIỀU CHỈNH: Tăng thời gian chuột hiển thị lên 1.2 giây (CHẬM HƠN)
        handler.postDelayed(() -> {
            if (currentMole == idx) {
                hideMole(idx);
                currentMole = -1;
            }
        }, moleVisibleTime); // Sử dụng moleVisibleTime thay vì moleInterval/2
    }

    // THÊM: Hiệu ứng khi click trúng chuột (tuỳ chọn)
    private void showClickEffect(ImageView mole) {
        // Có thể thêm hiệu ứng scale hoặc animation tại đây
        mole.animate()
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(100)
                .withEndAction(() -> mole.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(100))
                .start();
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
        txtScore.setText(String.valueOf(score));

        // Cập nhật high score
        try {
            int currentHighScore = Integer.parseInt(txtHighScore.getText().toString());
            if (score > currentHighScore) {
                txtHighScore.setText(String.valueOf(score));
            }
        } catch (NumberFormatException e) {
            txtHighScore.setText(String.valueOf(score));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isPlaying) endGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
        stopMoleSpawner();
    }
}