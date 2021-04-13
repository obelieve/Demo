package com.zxy.demo.hw;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * @Description: 输入一个int型整数，按照从右向左的阅读顺序，返回一个不含重复数字的新的整数。
 * 输入描述：输入一个int型整数      输出描述：按照从右向左的阅读顺序，返回一个不含重复数字的新的整数
 * 例：输入：9876673         输出：37689
 */
public class HJ9_2_right {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while (input.hasNextInt()){
            String num = ""+input.nextInt();
            Set<Integer> set = new HashSet<>();
            for(int i=num.length()-1;i>=0;i--){
                int value = Integer.parseInt(num.charAt(i)+"");
                if(!set.contains(value)){
                    System.out.print(value);
                    set.add(value);
                }
            }
            System.out.println();

        }
    }
}
