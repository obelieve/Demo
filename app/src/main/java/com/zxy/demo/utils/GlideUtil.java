package com.zxy.demo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.zxy.demo.R;
import com.zxy.demo.base.App;

public class GlideUtil {

    /**
     * 默认加载图片
     *
     * @param model
     * @return
     */
    public static RequestBuilder<Drawable> load(Object model)
    {
        return Glide.with(App.getContext()).load(model).apply(new RequestOptionsDefault());
    }


    /**
     * 默认加载用户头像
     *
     * @param model
     * @return
     */
    public static RequestBuilder<Drawable> loadHeadImage(Object model)
    {
        return Glide.with(App.getContext()).load(model).apply(new RequestOptionsHeadImage());
    }


    public static <T> void loadAsBitmap(T model, Target<Bitmap> target)
    {
        Glide.with(App.getContext()).asBitmap().load(model).into(target);
    }

    public static class RequestOptionsDefault extends RequestOptions
    {
        public RequestOptionsDefault()
        {
            placeholder(R.drawable.default_bg1);
            error(R.drawable.default_bg1);
            dontAnimate();
        }
    }

    public static class RequestOptionsHeadImage extends RequestOptions
    {
        public RequestOptionsHeadImage()
        {
            placeholder(R.drawable.default_avatar);
            error(R.drawable.default_avatar);
            dontAnimate();
        }
    }
}
