package com.zxy.im.utils;

import java.security.MessageDigest;

/**
 * Created by zxy on 2018/12/12 15:46.
 */

public class MessageDigestUtils
{
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};


    public static char[] encodeHex(byte[] data, boolean toLowerCase)
    {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    private static char[] encodeHex(byte[] data, char[] toDigits)
    {
        final int length = data.length;
        final char[] out = new char[length << 1];
        for (int i = 0, j = 0; i < length; i++)
        {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    public static String SHA1(String content)
    {
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(content.getBytes("utf-8"));
            final byte[] bytes = messageDigest.digest();
            return String.valueOf(encodeHex(bytes, true));
        } catch (Exception e)
        {
            return content;
        }
    }
}
