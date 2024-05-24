package com.example.green_hero.utils;

import android.content.Context;
import android.widget.Toast;

public class Actions {
    public static void trophyToast(Context context) {
        Toast.makeText(context, "Trophy unlocked!", Toast.LENGTH_SHORT).show();
    }
}
