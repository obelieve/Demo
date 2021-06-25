package com.zxy.demo;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class Utils {
    public static void wholeUser(Activity activity, ImageView iv) {
        Glide.with(activity)
                .load("https://pic.netbian.com/uploads/allimg/180826/113958-1535254798fc1c.jpg")
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .override(1000,1000)
                .into(iv);
    }
}
