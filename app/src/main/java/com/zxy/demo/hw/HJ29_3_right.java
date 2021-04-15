package com.zxy.demo.hw;

import java.util.Scanner;

/**
 * @Description: 对输入的字符串进行加解密，并输出，解密方法为加密的逆过程。
 * 规则：1.当内容是英文字母时则用该英文字母的后一个字母替换，同时字母变换大小写,如字母a时则替换为B；字母Z时则替换为a
 *      2.当内容是数字时则把该数字加1，如0替换1，1替换2，9替换0
 *      3.其他字符不做变化
 * 输入描述：输入一串要加密的密码          输出描述：输出加密后的字符
 *          输入一串加过密的密码                   输出解密后的字符
 * 例：输入：abcdefg                     输出：BCDEFGH
 *          BCDEFGH                          abcdefg
 */
public class HJ29_3_right {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean jiami = true;
        while (scanner.hasNext()){
            String next = scanner.next();
           int i = 0;
           StringBuilder sb = new StringBuilder();
           while (i<next.length()){
               char c = next.charAt(i);
               int offset;
               if(jiami){
                   offset = 1;
               }else{
                   offset = -1;
               }
               if(c>='A'&&c<='Z'){
                   if(offset==1&&c=='Z'){
                       c = 'a';
                   }else if(offset==-1&&c=='A'){
                       c = 'z';
                   }else{
                       c = (char)(((int)c-('A'-'a')+offset));
                   }
               }else if(c>='a'&&c<='z'){
                   if(offset==1&&c=='z'){
                       c = 'A';
                   }else if(offset==-1&&c=='a'){
                       c = 'Z';
                   }else {
                       c = (char) ((int) c + ('A' - 'a') + offset);
                   }
               }else if(c>='0'&&c<='9'){
                   if(offset==1&&c=='9'){
                       c = '0';
                   }else if(offset==-1&&c=='0'){
                       c='9';
                   }else{
                       c = (char)((int)c+offset);
                   }
               }
               i++;
               sb.append(c);
           }
           jiami = !jiami;
         System.out.println(sb.toString());
        }
    }
}

