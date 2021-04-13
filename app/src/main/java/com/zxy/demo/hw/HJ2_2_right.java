package com.zxy.demo.hw;

import java.util.Scanner;

/**
 * @Description: 写出一个程序，接受一个由字母和数字组成的字符串，和一个字符，然后输出输入字符串中含有该字符的个数。不区分大小写。
 * 输入描述：第一行输入一个有字母和数字以及空格组成的字符串，第二行输入一个字符。
 * 输出描述：输出输入字符串中含有该字符的个数。
 * 例：输入："ABCDEF A"      输出：1
 */
public class HJ2_2_right {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while (input.hasNext()) {
            String line1 = input.nextLine();
            String line2 = input.nextLine();
            int i = 0;
            int count =0;
            while(i<line1.length()){
                String a = ""+line1.charAt(i);
                if(a.toLowerCase().equals(line2.toLowerCase())){
                    count++;
                }
                i++;
            }
            System.out.println(count);
        }
    }
}
