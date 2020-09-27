package com.zxy.frame.utils;

import android.content.Intent;
import android.provider.MediaStore;

/**
 * Created by zxy on 2018/8/31 17:38.
 */

public class IntentUtil
{

    public static Intent selectPicture()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);//可以用流的方式打开
        intent.setType("image/*");
        return intent;
    }

    public static Intent takePicture()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        return intent;
    }
}
