package com.zxy.demo.hw;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @Description: 写出一个程序，接受一个十六进制的数，输出该数值的十进制表示（多组同时输入）。
 * 输入描述：输入一个十六进制的数值字符串。     输出描述：输出该数值的十进制字符串。
 * 例：输入：0xA     输出：10
 */
public class HJ5_2_write {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Map<String,Integer> map = new HashMap<>();
        map.put("A",10);
        map.put("B",11);
        map.put("C",12);
        map.put("D",13);
        map.put("E",14);
        map.put("F",15);
        while (input.hasNext()) {
            String va = input.next();
            va = va.replace("0x","");
            int count = 0;
            for(int i = va.length()-1;i>=0;i--){
                Integer num = map.get(va.charAt(i)+"");
                if(num==null){
                    count +=Integer.parseInt(va.charAt(i)+"") *16;
                }else{
                    count+=num*mi(va.length()-1-i);
                }
            }
            System.out.println(count);
        }
    }

    public static int mi(int count){
        int value = 1;
        for(int i=count;i>0;i--){
            value*=16;
        }
        return value;
    }
}
