package com.romide.terminal.activity;

import android.content.Context;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class PasteHistory {
    private static final String CONFIG_FILE = "ski_paste_history";

    private Context mContext;

    public PasteHistory(Context context) {
        this.mContext = context;
    }

    private String getTimeString() {
        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formater.format(new Date());
    }

    public void pushHistory(String content) {
        if (!content.isEmpty()) {
            mContext.getSharedPreferences(CONFIG_FILE, Context.MODE_PRIVATE)
                    .edit()
                    .putString(getTimeString(), content)
                    .apply();
        }
    }

    public Map<String, ?> listHistory() {
        return mContext.getSharedPreferences(CONFIG_FILE, Context.MODE_PRIVATE).getAll();
    }

    public void clearHistory() {
        mContext.getSharedPreferences(CONFIG_FILE, Context.MODE_PRIVATE).edit().clear().apply();
    }

    public void removeHistory(String key) {
        mContext.getSharedPreferences(CONFIG_FILE, Context.MODE_PRIVATE).edit().remove(key).apply();
    }
}
