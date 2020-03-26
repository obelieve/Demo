package com.zxy.frame.utils.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.Transition;


public class GlideUtil {

    private static DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();

    /**
     * 默认加载
     *
     * @param context
     * @param path
     * @param iv
     */
    public static void loadImage(Context context, String path, ImageView iv) {
        Glide.with(context).load(path).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory)).into(iv);
    }

    /**
     * 默认加载
     *
     * @param context
     * @param path
     * @param iv
     */
    @SuppressWarnings("unchecked")
    public static void loadImageRound(Context context, String path, ImageView iv) {
        Glide.with(context).load(path).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory)).transform(new MultiTransformation(new CenterCrop(),  new GlideRoundTransform(context, path != null && path.contains(".gif")))).into(iv);
    }

    /**
     * 设置加载中以及加载失败图片
     *
     * @param context
     * @param path
     * @param iv
     * @param loadingImage
     * @param errorImage
     */
    public static void loadImage(Context context, String path, ImageView iv, int loadingImage, int errorImage) {
        Glide.with(context).load(path).placeholder(loadingImage).error(errorImage).into(iv);
    }

    /**
     * 设置加载中以及加载失败图片并且指定大小
     *
     * @param context
     * @param path
     * @param width
     * @param height
     * @param mImageView
     * @param loadingImage
     * @param errorImage
     */
    public static void loadImage(Context context, String path, int width, int height, ImageView mImageView, int loadingImage, int errorImage) {
        Glide.with(context).load(path).override(width, height).placeholder(loadingImage).error(errorImage).into(mImageView);
    }


    /**
     * 设置缩略图支持,会先加载缩略图
     *
     * @param context
     * @param path
     * @param mImageView
     */
    public static void loadImageThumbnail(Context context, String path, ImageView mImageView) {
        Glide.with(context).load(path).thumbnail(0.1f).into(mImageView);
    }

    /**
     * 加载单图，自适应高宽
     *
     * @param context
     * @param path
     * @param imageView
     * @param maxsize
     */
    public static void loadImageAutoSize(Context context, String path, ImageView imageView, float maxsize) {
        Glide.with(context).clear(imageView);
        Glide.with(context).setDefaultRequestOptions(new RequestOptions()).load(path).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                setImageSize(path, context, resource, imageView, maxsize, path.contains(".gif"));
            }

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                super.onLoadStarted(placeholder);
                imageView.setBackgroundResource(android.R.color.darker_gray);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                imageView.setBackgroundResource(android.R.color.darker_gray);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });

    }

    private static void setImageSize(String path, Context context, Drawable resource, ImageView imageView, float maxsize, boolean isGif) {
        int width = resource.getIntrinsicWidth();
        int height = resource.getIntrinsicHeight();
        imageView.requestLayout();
        if (width >= height && maxsize < width) {
            imageView.getLayoutParams().width = (int) maxsize;
            float f = maxsize / width;
            imageView.getLayoutParams().height = (int) (height * f);
        } else if (height >= width && maxsize < height) {
            imageView.getLayoutParams().height = (int) maxsize;
            float f = maxsize / height;
            imageView.getLayoutParams().width = (int) (width * f);
        } else {
            imageView.getLayoutParams().height = height;
            imageView.getLayoutParams().width = width;
        }
        Glide.with(context).load(path).transform(new GlideRoundTransform(context, isGif)).into(imageView);
    }


    /**
     * 设置缓存策略
     * 策略解说：
     * all:缓存源资源和转换后的资源
     * none:不作任何磁盘缓存
     * source:缓存源资源
     * result：缓存转换后的资源
     */
    public static void loadImageDiskCache(Context context, String path, ImageView mImageView) {
        Glide.with(context).load(path).placeholder(android.R.color.darker_gray).error(android.R.color.darker_gray).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView);
    }
}

