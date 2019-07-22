package com.zxy.unit_test;

/**
 * Created by admin on 2019/2/19.
 */

public class Log {

    public static void println() {
        println(null, 2);
    }

    public static void println(String s) {
        println(s,2);
    }

    private static void println(String s, int track) {
        StackTraceElement[] elements = new Throwable().getStackTrace();
        String value = null;
        if (s == null || s.equals("")) {
            value = elements[track].getMethodName();
        } else {
            value = s;
        }
        System.out.println("[" + elements[track].getClassName() + "." + elements[track].getMethodName() + " line:" + elements[track].getLineNumber() + "]:" + value);
    }
}
