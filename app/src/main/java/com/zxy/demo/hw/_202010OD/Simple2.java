package com.zxy.demo.hw._202010OD;

import java.util.Scanner;

/**
 * 简单题2：消除相邻的2个相同字符 cost:30m
 * 输入：
 * 小写英文字母的字符串 ，如果包含非小写字母的字符串则返回0
 * 输出：
 * 消除相邻的2个相同字符的字符串
 */
public class Simple2 {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        String str = scanner.next();
        boolean b = true;
        for(int i=0;i<str.length();i++){
            if(!((str.charAt(i)>='a'&&str.charAt(i)<='z')||
                    (str.charAt(i)>='A'&&str.charAt(i)<='Z'))){
                System.out.println(0);
                return;
            }
        }
        if(b){
            String s = clear(str);
            System.out.println(s.length());
        }
    }

    public static String clear(String str){
        int start = 0;
        while (start!=-1){
                if(start<str.length()){
                    if(start!=str.length()-1&&str.charAt(start)==str.charAt(start+1)){
                        str = str.replace(str.substring(start,start+2),"");
                        start = 0;
                    }else{
                       if(start==str.length()-1){
                           start=-1;
                       }else{
                           start++;
                       }
                    }
                }else{
                    start=-1;
                }

        }
        return str;
    }

}
