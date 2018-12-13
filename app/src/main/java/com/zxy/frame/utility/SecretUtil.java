package com.zxy.frame.utility;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2017/11/28.
 */

public class SecretUtil
{

    /**
     * @param data
     * @return
     */
    public static String byte2String(byte[] data)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.length; i++)
        {
            String s = Integer.toHexString(data[i] & 0xFF);
            if (s.length() == 1)
                s += "0";
            builder.append(s);
        }
        return builder.toString();
    }

    /**
     * MD5
     *
     * @param content
     * @return
     */
    public static String MD5(String content)
    {
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(content.getBytes("utf-8"));
            final byte[] bytes = messageDigest.digest();
            return String.valueOf(byte2String(bytes));
        } catch (Exception e)
        {
            return content;
        }
    }

    /**
     * SHA1
     *
     * @param content
     * @return
     */
    public static String SHA1(String content)
    {
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(content.getBytes("utf-8"));
            final byte[] bytes = messageDigest.digest();
            return String.valueOf(byte2String(bytes));
        } catch (Exception e)
        {
            return content;
        }
    }

    /**
     * @param content
     * @param token   secret key
     * @return null, if decryptAES is failure or (content == null|token == null)
     */
    public static String encryptBase64AES(String content, String token)
    {
        if (content == null || token == null)
            return null;
        byte[] bytes = null;
        bytes = encryptAES(content.getBytes(), token);
        if (bytes != null)
            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        return null;
    }

    /**
     * @param base64Str
     * @param token     secret key
     * @return null, if decryptAES is failure or (base64Str == null|token == null)
     */
    public static String decryptBase64AES(String base64Str, String token)
    {
        if (base64Str == null || token == null)
            return null;
        byte[] base64Bytes = Base64.decode(base64Str, Base64.NO_WRAP);
        byte[] bytes = decryptAES(base64Bytes, token);
        if (bytes != null)
            return new String(bytes);
        return null;
    }

    /**
     * AES encryptAES
     *
     * @param content
     * @param password
     * @return
     */
    public static byte[] encryptAES(byte[] content, String password)
    {
        try
        {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = null;
            if (android.os.Build.VERSION.SDK_INT >= 17)
            {
                secureRandom = SecureRandom.getInstance("SHA1PRNG", "Crypto");
            } else
            {
                secureRandom = SecureRandom.getInstance("SHA1PRNG");
            }
            secureRandom.setSeed(password.getBytes());
            kgen.init(128, secureRandom);

            SecretKey secretKey = kgen.generateKey();

            byte[] enCodeFormat = secretKey.getEncoded();

            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] result = cipher.doFinal(content);

            return result;

        } catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        } catch (InvalidKeyException e)
        {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        } catch (BadPaddingException e)
        {
            e.printStackTrace();
        } catch (NoSuchProviderException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES decryptAES
     *
     * @param content
     * @param password
     * @return
     */
    public static byte[] decryptAES(byte[] content, String password)
    {
        try
        {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = null;
            if (android.os.Build.VERSION.SDK_INT >= 17)
            {
                secureRandom = SecureRandom.getInstance("SHA1PRNG", "Crypto");
            } else
            {
                secureRandom = SecureRandom.getInstance("SHA1PRNG");
            }
            secureRandom.setSeed(password.getBytes());
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            return result;

        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        } catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
        } catch (InvalidKeyException e)
        {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        } catch (BadPaddingException e)
        {
            e.printStackTrace();
        } catch (NoSuchProviderException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
