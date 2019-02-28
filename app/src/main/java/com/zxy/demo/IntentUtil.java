package com.zxy.demo;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by admin on 2019/2/28.
 */

public class IntentUtil {

    /**
     *
     * 在Android N(7)以上(API 版本为24），当应用使用file:// 形式的Uri暴露给另一个应用时将会抛出该异常。而低于N之前的版本仍然可以使用file://的形式来共享Uri,但是十分不推荐这样做。
     　　原因在于使用file://Uri会有一些风险，比如：

     文件是私有的，接收file://Uri的app无法访问该文件。
     在Android6.0之后引入运行时权限，如果接收file://Uri的app没有申请READ_EXTERNAL_STORAGE权限，在读取文件时会引发崩溃。
     * @param activity
     * @param path
     */
    public static void startFileManager(Activity activity,String path){
        File file = new File(path);
        if(null==file || !file.exists()){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(activity,"com.zxy.fileprovider",new File(path));
            intent.setDataAndType(uri, "file/*");
        }else {
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "file/*");
        }

        try {
//            activity.startActivity(intent);
            activity.startActivity(Intent.createChooser(intent,"选择浏览工具"));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
