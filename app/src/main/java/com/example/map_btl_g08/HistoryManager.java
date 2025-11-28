package com.example.map_btl_g08;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryManager {

    // Thêm lịch sử truy cập
    public static void addHistory(Context context, String screenName) {
        SharedPreferences prefs = context.getSharedPreferences("history_data", Context.MODE_PRIVATE);

        int count = prefs.getInt("history_count", 0) + 1;  // tăng số thứ tự

        String currentTime = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault())
                .format(new Date());

        String record = screenName + " | " + currentTime;

        prefs.edit()
                .putString("history_" + count, record)
                .putInt("history_count", count)
                .apply();
    }

    // Lấy toàn bộ lịch sử
    public static List<String> getHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("history_data", Context.MODE_PRIVATE);

        int count = prefs.getInt("history_count", 0);
        List<String> list = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            String item = prefs.getString("history_" + i, null);
            if (item != null) list.add(item);
        }
        return list;
    }

    // Xoá lịch sử
    public static void clearHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("history_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = prefs.edit();

        int count = prefs.getInt("history_count", 0);

        for (int i = 1; i <= count; i++) {
            e.remove("history_" + i);
        }
        e.putInt("history_count", 0);
        e.apply();
    }
}
