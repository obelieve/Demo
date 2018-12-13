package com.zxy.frame.utility;

import android.app.Activity;
import android.content.Context;
import android.os.storage.StorageManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtil {

    /**
     * directory encoding
     *
     * @param file
     * @param destEncoding
     * @throws IOException
     */
    public static void convertDirectoryEncoding(File file, final String destEncoding) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file2 : files) {
                convertDirectoryEncoding(file2, destEncoding);
            }
        } else {
            convertFileEncoding(file.getAbsolutePath(), destEncoding);
        }
    }

    /**
     * file encoding
     *
     * @param filePath
     * @param destEncoding
     * @throws IOException
     */
    public static void convertFileEncoding(String filePath, String destEncoding) throws IOException {
        File file = new File(filePath);
        if (!file.isFile())
            return;
        BufferedReader reader = null;
        Writer writer = null;
        String fileDir = filePath.substring(0, filePath.lastIndexOf(File.separator));
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
        String tempFilePath = fileName + ".temp";
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            writer = new OutputStreamWriter(new FileOutputStream(new File(fileDir, tempFilePath)), destEncoding);
            String string = null;
            while ((string = reader.readLine()) != null) {
                writer.write(string + '\n');
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                throw e;
            }
        }
        file.delete();
        new File(fileDir, tempFilePath).renameTo(file);
    }

    /**
     * delete file(contains subfile)
     *
     * @param path
     */
    public static void deleteFile(String path) {

        File file = new File(path);
        if (file.isDirectory()) {
            File[] listFile = file.listFiles();
            if (listFile.length > 0)
                for (File file2 : listFile) {
                    deleteFile(file2.getAbsolutePath());
                }
        }
        file.delete();
    }

    /**
     * file length,contains subfile
     *
     * @param file
     * @return
     */
    public static long getAllFileLength(File file) {
        long length = 0;
        if (file == null || file.exists() == false)
            return -1;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file2 : files) {
                    length += getAllFileLength(file2);
                }
            }
        } else {
            length += file.length();
        }
        return length;
    }

    /**
     * @param sourceFile      required exist
     * @param descriptionFile
     * @param isOverwrite     whether overwrite if descriptionFile exist.
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File descriptionFile, boolean isOverwrite)
            throws IOException {
        if (sourceFile == null || sourceFile.exists() == false) {
            throw new IOException("copyFile->sourceFile==null or is not exists !");
        }
        if (descriptionFile == null) {
            throw new IOException("copyFile->descriptionFile==null !");
        }
        if (descriptionFile.exists()) {
            if (isOverwrite == false) {
                throw new IOException("copyFile->descriptionFile has existed:"
                        + descriptionFile.getAbsolutePath());
            }
        }
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(sourceFile));
            out = new BufferedOutputStream(new FileOutputStream(descriptionFile));
            byte[] buffer = new byte[1024 * 4];
            int pos = 0;
            while ((pos = in.read(buffer)) != -1) {
                out.write(buffer, 0, pos);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new IOException(e);
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                throw new IOException(e);
            }
        }
    }

    /**
     * @param sourceFile      sourceFile required exist
     * @param descriptionFile destinationFile
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File descriptionFile) throws IOException {
        copyFile(sourceFile, descriptionFile, true);
    }

    /**
     * @param srcFile      !=null&& exist()==true
     * @param desDirectory isDirectory()==true
     * @throws IOException if failure
     */
    public static void copyToDirectory(File srcFile, File desDirectory)
            throws IOException {
        if (srcFile == null || srcFile.exists() == false)
            throw new IOException(
                    "copyToDirectory-> srcFile==null or is not exists !");
        if (desDirectory == null) {
            throw new IOException("copyToDirectory-> desDirectory==null !");
        }
        if (!desDirectory.isDirectory()) {
            throw new IOException(
                    "copyToDirectory-> desDirectory is not directory  or is not exists!");
        }
        if (srcFile.isDirectory()) {
            File createDir = new File(desDirectory.getAbsolutePath(),
                    srcFile.getName());
            if (createDir.exists() == false) {
                if (createDir.mkdir() == false) {
                    throw new IOException("copyToDirectory-> mkdir:"
                            + createDir.getAbsolutePath() + " is failure !");
                }
            }
            File[] files = srcFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    copyToDirectory(file, createDir);
                }
            }
        } else {
            copyFile(srcFile, new File(desDirectory, srcFile.getName()));
        }
    }

    public static void writeBytesDescriptionFile(InputStream is, File file) throws IOException {
        FileOutputStream fos = null;
        try {
            byte[] data = new byte[2048];
            int len = 0;
            fos = new FileOutputStream(file);
            while ((len = is.read(data)) > -1) {
                fos.write(data, 0, len);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    public static String encodeMD5(String str) {
        return encodeMD5(new ByteArrayInputStream(str.getBytes()));
    }

    public static String encodeMD5(InputStream in) {
        if (in == null)
            return "";
        byte[] bytes = new byte[8192];
        MessageDigest md = null;
        int len = 0;
        try {
            md = MessageDigest.getInstance("MD5");
            while ((len = in.read(bytes)) != -1) {
                md.update(bytes, 0, len);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] digest = md.digest();
        StringBuilder resultSBuilder = new StringBuilder();
        BigInteger bigInteger = new BigInteger(digest).abs();
        resultSBuilder.append(bigInteger.toString(16));
        return resultSBuilder.toString();

    }

    /**
     * 文件挂载
     */
    public static String getVolumeState(Context context, String filePath) {
        String mountedState = null;
        StorageManager mStorageManager;
        Method mGetState = null;
        mStorageManager = (StorageManager) context
                .getSystemService(Activity.STORAGE_SERVICE);
        try {
            mGetState = mStorageManager.getClass().getMethod("getVolumeState", String.class);
            try {
                mountedState = (String) mGetState.invoke(mStorageManager, filePath);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return mountedState;
    }

}
