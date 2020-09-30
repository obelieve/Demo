package com.github.obelieve.utils;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.v3.BottomMenu;

/**
 * Created by Admin
 * on 2020/9/10
 */
public class BottomMenuUtil {

    public static void show(Activity activity, String[] menuTexts, OnItemClickListener onItemClickListener) {
        if (activity instanceof AppCompatActivity)
            BottomMenu.show((AppCompatActivity) activity, menuTexts, new OnMenuItemClickListener() {
                @Override
                public void onClick(String text, int index) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(text, index);
                    }
                }
            });
    }

    public interface OnItemClickListener {
        void onClick(String text, int index);
    }
}
