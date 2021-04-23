package com.zxy.demo;

public class Main {

    /**
     * 1.涉及到很多位操作符
     * @param args
     */
    public static void main(String[] args){
        int a = 0 << 30;
        int b = 1 << 30;//1073741824 match_parent
        int c = 2 << 30;//-2147483648 wrap_content
        String toBinary = Integer.toBinaryString(2);
        System.out.println("Main值 = a:"+a+" b:"+b+" c:"+c+" toBinaryString："+toBinary);
    }
}