package com.zxy.demo.hw;

import java.util.Scanner;

/**
 * @Description: 写出一个程序，接受一个正浮点数值，输出该数值的近似整数值。如果小数点后数值大于等于5,向上取整；小于5，则向下取整。
 * 输入描述：输入一个正浮点数值       输出描述：输出该数值的近似整数值
 * 例：输入：5.5         输出：6
 */
public class HJ7_1_right {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            int a = (int)(Math.round(scanner.nextFloat()));
            System.out.println(a);
        }
    }
}
