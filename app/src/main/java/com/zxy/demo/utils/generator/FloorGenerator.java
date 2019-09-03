package com.zxy.demo.utils.generator;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by zxy on 2019/09/03.
 */
public interface FloorGenerator {

    void saveToFile();

    void saveNewBitmapToFile(Context context, Bitmap bitmap);

    void saveDataToFile(int[][] surface);

    void saveJsonToFile();
}
