package com.zxy.frame.http;

import com.zxy.frame.utility.CheckUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zxy on 2018/8/13 15:23.
 */

public class Downloader
{

    private final String ERROR_NOT_EXIST_DIRECTORY = "文件目录不存在：";
    /**
     * 超时连接最多5次
     */
    private final int TIMEOUT_CONNECT = 5;

    public static final int SUCCESS = 0;
    public static final int FAILURE = 1;
    public static final int PROGRESS_UPDATE = 2;
    public static final int COMPLETED = 3;

    private static Downloader sDownloader;
    private static String mDefDownloadDir;

    private List<Callback> mCallbacks = new ArrayList<>();
    private HashSet<String> mHashSet = new HashSet<String>();
    private Map<String, Integer> mHashMap = Collections
            .synchronizedMap(new HashMap<String, Integer>());

    private Downloader()
    {

    }

    public static Downloader getInstance()
    {
        if (sDownloader == null)
        {
            synchronized (Downloader.class)
            {
                if (sDownloader == null)
                {
                    sDownloader = new Downloader();
                }
            }
        }
        return sDownloader;
    }

    public void setDefDownloadDir(String defDownloadDir)
    {
        mDefDownloadDir = defDownloadDir;
    }

    public void addDownloadCallback(Callback callback)
    {
        mCallbacks.add(callback);
    }

    public void removeDownloadCallback(Callback callback)
    {
        if (mCallbacks.contains(callback))
        {
            mCallbacks.remove(callback);
        }
    }

    public String getDefDownloadDir()
    {
        return mDefDownloadDir;
    }


    /**
     * @param url
     * @throws if DefDownloadDir ==null,throw new NullPointerException
     */
    public void download(final String url)
    {
        CheckUtil.checkNotNull(mDefDownloadDir);
        download(url, mDefDownloadDir, getDownloadFileName(url));
    }

    public void download(final String url, String fileDir, String fileName)
    {
        try
        {
            if (url == null) return;
            new URL(url);
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
            return;
        }
        synchronized (url)
        {
            if (mHashSet.contains(url))
            {
                return;
            }
            if (mHashMap.get(url) == null || mHashMap.get(url) > TIMEOUT_CONNECT)
                mHashMap.put(url, 1);
            mHashSet.add(url);
        }
        final Info info = new Info(url);
        info.setFileDir(fileDir);
        info.setFileName(fileName);
        Request request = new Request.Builder().get().url(url).build();
        Call call = OkHttpUtil.getInstance().getOkHttpClient().newCall(request);
        call.enqueue(new TempCallBack(info)
        {
            @Override
            public void onResponse(Call call, Response response)
            {
                InputStream in = response.body().byteStream();
                long contentLength = response.body().contentLength();
                mInfo.setContentLength(contentLength);
                onSuccess(mInfo);
                try
                {
                    downloadToFile(call, this, mInfo, in, url);
                    mHashSet.remove(url);
                } catch (IOException e)
                {
                    Downloader.this.onFailure(mInfo);
                    mHashSet.remove(url);

                    int tempInt = mHashMap.get(url);
                    if (tempInt <= TIMEOUT_CONNECT)
                    {
                        download(url);
                        mHashMap.put(url, tempInt + 1);
                    }
                } finally
                {
                    call.cancel();
                }
            }

            @Override
            public void onFailure(Call call, IOException e)
            {
                mInfo.setErrorMsg(e.getMessage());
                Downloader.this.onFailure(mInfo);
                call.cancel();
                mHashSet.remove(url);

                int tempInt = mHashMap.get(url);
                if (tempInt <= TIMEOUT_CONNECT)
                {
                    download(url, mInfo.getFileDir(), mInfo.getFileName());
                    mHashMap.put(url, tempInt + 1);
                }
            }
        });
    }

    private abstract class TempCallBack implements okhttp3.Callback
    {
        Info mInfo;

        TempCallBack(Info info)
        {
            mInfo = info;
        }
    }


    private String getDownloadFileName(String url)
    {

        String fileName = null;

        if (CheckUtil.isEmpty(url))
        {
            return null;
        }
        // trim url
        url = url.trim();
        try
        {
            if (url.contains("/"))
            {
                fileName = url.substring(url.lastIndexOf('/') + 1);
                if (fileName.contains("?"))
                {
                    fileName = fileName.substring(0, fileName.indexOf('?'));
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        if (!CheckUtil.isEmpty(fileName))
        {
            return fileName;
        }
        return null;
    }

    private void downloadToFile(Call call, okhttp3.Callback callback,
                                Info info, InputStream in, String url) throws IOException
    {

        String fileName = info.getFileName();
        String tempFileName = info.getFileName() + ".temp";
        File fileDir = new File(info.getFileDir());
        if (!fileDir.exists())
        {
            boolean b = fileDir.mkdirs();
            if (b == false)
            {
                info.setErrorMsg(ERROR_NOT_EXIST_DIRECTORY + info.getFileDir());
                onFailure(info);
                return;
            }
        }
        File file = new File(fileDir.getAbsolutePath(), fileName);
        if (file.exists())
            file.delete();
        File tempFile = new File(fileDir.getAbsolutePath(), tempFileName);
        if (tempFile.exists())
            tempFile.delete();
        FileOutputStream out = null;
        try
        {
            long startTime = System.currentTimeMillis();
            out = new FileOutputStream(tempFile);
            long curLength = 0;
            int netLength = 0;
            int bufferLength = 0;
            byte[] buffer = new byte[1024 * 8];
            while ((bufferLength = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, bufferLength);
                curLength += bufferLength;
                netLength += bufferLength;
                if (System.currentTimeMillis() - startTime > 500)
                {
                    float netSpeedKB = ((float) netLength / (System
                            .currentTimeMillis() - startTime)) * 1000 / 1024;
                    BigDecimal b = new BigDecimal(netSpeedKB);
                    b = b.setScale(2, BigDecimal.ROUND_HALF_UP);
                    netSpeedKB = b.floatValue();
                    int remainTime = -1;
                    if (netSpeedKB != 0)
                    {
                        remainTime = (int) ((info.getContentLength() - curLength) / (netSpeedKB * 1024));
                    }
                    if (info.getContentLength() != 0)
                    {
                        info.setPercent(curLength
                                / (float) info.getContentLength());
                    }
                    info.setCurrentLength(curLength);
                    info.setNetSpeedKB(netSpeedKB);
                    info.setRemainTime(remainTime);
                    onProgressUpdate(info);
                    startTime = System.currentTimeMillis();
                    netLength = 0;
                }
            }
            tempFile.renameTo(file);
            info.setPercent(1.0F);
            onProgressUpdate(info);
            onCompleted(info);
        } catch (IOException e)
        {
            throw e;
        } finally
        {
            if (out != null)
            {
                try
                {
                    out.flush();
                    out.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onSuccess(Info info)
    {
        for (Callback callback : mCallbacks)
        {
            callback.onSuccess(info);
        }
    }

    private void onFailure(Info info)
    {
        for (Callback callback : mCallbacks)
        {
            callback.onFailure(info);
        }
    }

    private void onProgressUpdate(Info info)
    {
        for (Callback callback : mCallbacks)
        {
            callback.onProgressUpdate(info);
        }
    }

    private void onCompleted(Info info)
    {
        for (Callback callback : mCallbacks)
        {
            callback.onCompleted(info);
        }
    }

    public interface Callback
    {
        void onSuccess(Info info);

        void onFailure(Info info);

        void onProgressUpdate(Info info);

        void onCompleted(Info info);
    }


    public static class Info
    {
        private String url;
        private String fileDir;
        private String fileName;
        private long contentLength;
        private long currentLength;
        private float percent;
        private float netSpeedKB;
        private int remainTime;
        private String errorMsg;

        public Info(String urlString)
        {
            url = urlString;
        }

        public String getUrl()
        {
            return url;
        }

        public void setUrl(String url)
        {
            this.url = url;
        }

        public String getFileDir()
        {
            return fileDir;
        }

        public void setFileDir(String fileDir)
        {
            this.fileDir = fileDir;
        }

        public String getFileName()
        {
            return fileName;
        }

        public void setFileName(String fileName)
        {
            this.fileName = fileName;
        }

        public long getContentLength()
        {
            return contentLength;
        }

        public void setContentLength(long contentLength)
        {
            this.contentLength = contentLength;
        }

        public long getCurrentLength()
        {
            return currentLength;
        }

        public void setCurrentLength(long currentLength)
        {
            this.currentLength = currentLength;
        }

        public float getPercent()
        {
            return percent;
        }

        public void setPercent(float percent)
        {
            this.percent = percent;
        }

        public float getNetSpeedKB()
        {
            return netSpeedKB;
        }

        public void setNetSpeedKB(float netSpeedKB)
        {
            this.netSpeedKB = netSpeedKB;
        }

        public int getRemainTime()
        {
            return remainTime;
        }

        public void setRemainTime(int remainTime)
        {
            this.remainTime = remainTime;
        }

        public String getErrorMsg()
        {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg)
        {
            this.errorMsg = errorMsg;
        }

    }

}

