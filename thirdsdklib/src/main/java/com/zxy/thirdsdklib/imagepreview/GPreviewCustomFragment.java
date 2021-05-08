package com.zxy.thirdsdklib.imagepreview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.previewlibrary.enitity.IThumbViewInfo;
import com.previewlibrary.view.BasePhotoFragment;
import com.zxy.frame.utils.ToastUtil;
import com.zxy.frame.utils.storage.StorageUtil;
import com.zxy.thirdsdklib.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GPreviewCustomFragment extends BasePhotoFragment {

    private IThumbViewInfo b;
    private Dialog mBottomDialog;
    private final String RELATIVE_SAVE_PICTURE_PATH = Environment.DIRECTORY_DCIM + File.separator + "Camera";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        b = getBeanViewInfo();
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showBottomDialog(getActivity());
                return false;
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024) {
            if (PermissionUtil.hasAllPermissionsGranted(grantResults)) {
                savePicture(getContext());
            } else {
                ToastUtil.show(getResources().getString(R.string.permission_grant_tips));
            }

        }
    }

    @SuppressLint("CheckResult")
    private void savePicture(final Context context) {
        if (b != null && !TextUtils.isEmpty(b.getUrl()) && StorageUtil.isSdExsit()) {
            final String imgUrl = b.getUrl();
            final String filename;
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof GifDrawable) {
                filename = System.currentTimeMillis() + ".gif";
            } else {
                filename = System.currentTimeMillis() + ".jpg";
            }
            Observable.create(new ObservableOnSubscribe<Boolean>() {
                @Override
                public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                    try {
                        String cachePath = getGlideCacheImagePath(imgUrl);
                        boolean bool = savePictureToAlbum(context, cachePath, filename);
                        emitter.onNext(bool);
                        emitter.onComplete();
                    } catch (Exception e) {
                        emitter.onError(e);
                    }
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean bool) throws Exception {
                    if (bool) {
                        ToastUtil.show(getResources().getString(R.string.picture_saved) + " "+RELATIVE_SAVE_PICTURE_PATH+" " + getResources().getString(R.string.folder));
                    } else {
                        ToastUtil.show(getResources().getString(R.string.save_failed));
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    ToastUtil.show(getResources().getString(R.string.save_failed));
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_photo_layout, container, false);
    }

    public void showBottomDialog(final Context context) {
        if (mBottomDialog == null) {
            mBottomDialog = new Dialog(context, R.style.BottomDialog);
            mBottomDialog.setCanceledOnTouchOutside(true);
            SelectPictureActionView view = new SelectPictureActionView(context);
            view.setCallback(new SelectPictureActionView.Callback() {
                @Override
                public void onSavePicture() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if ((context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                            savePicture(context);
                        } else {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024);
                        }
                    }else{
                        savePicture(context);
                    }
                }

                @Override
                public void onCancel() {
                    mBottomDialog.dismiss();
                }
            });
            mBottomDialog.setContentView(view);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            view.setLayoutParams(layoutParams);
            if (mBottomDialog.getWindow() != null)
                mBottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        }
        mBottomDialog.show();
    }

    /**
     * oldPath文件保存到新的newPath文件下
     *
     * @param context
     * @param oldPath
     * @param newPath
     * @return
     */
    public boolean copyFile(Context context, String oldPath, final String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                File file = new File(newPath);
                MediaStore.Images.Media.insertImage(context.getContentResolver(), BitmapFactory.decodeFile(file.getAbsolutePath()), file.getName(), null);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                context.sendBroadcast(intent);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    /**
     * 保存图片到相册
     *
     * @param context
     * @param oldPath
     * @param newFilename
     * @return
     */
    public boolean savePictureToAlbum(Context context, String oldPath, String newFilename) {
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, newFilename);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
            File dirFile = new File(Environment.getExternalStorageDirectory().getPath() +
                    File.separator + RELATIVE_SAVE_PICTURE_PATH);
            if(!dirFile.exists()){
                dirFile.mkdirs();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, RELATIVE_SAVE_PICTURE_PATH);
            } else {
                values.put(MediaStore.MediaColumns.DATA, Environment.getExternalStorageDirectory().getPath() +
                        File.separator + RELATIVE_SAVE_PICTURE_PATH+File.separator + newFilename);
            }
            Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver resolver = context.getContentResolver();

            Uri insertUri = resolver.insert(external, values);
            OutputStream os = null;
            if (insertUri != null) {
                try {
                    os = resolver.openOutputStream(insertUri);
                    if (os == null) {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }
            }
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    os.write(buffer, 0, byteread);
                }
                inStream.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    private String getGlideCacheImagePath(String imgUrl) {
        String path = null;
        FutureTarget<File> future = Glide.with(this)
                .load(imgUrl)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        try {
            File cacheFile = future.get();
            path = cacheFile.getAbsolutePath();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return path;
    }


    public static class PermissionUtil {

        /**
         * 判断是否缺少权限
         */
        public static boolean lacksPermission(Context mContexts, String permission) {
            return ContextCompat.checkSelfPermission(mContexts, permission) == PackageManager.PERMISSION_DENIED;
        }

        public static boolean hasAllPermissionsGranted(int[] grantResults) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    return false;
                }
            }
            return true;
        }
    }
}
