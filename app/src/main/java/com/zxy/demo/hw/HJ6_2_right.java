package com.zxy.demo.hw;

import java.util.Scanner;

/**
 * @Description: 功能:输入一个正整数，按照从小到大的顺序输出它的所有质因子（重复的也要列举）（如180的质因子为2 2 3 3 5 ）,最后一个数
 * 后面也要有空格。
 * 输入描述：输入一个long型整数     输出描述：按照从小到大的顺序输出它的所有质数的因子，以空格隔开。最后一个数后面也要有空格。
 * 例：输入：180     输出：2 2 3 3 5
 */
public class HJ6_2_right {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while (input.hasNextLong()){
            long value = input.nextLong();
            long k = (long) Math.sqrt(value);//通过91%用例，不用算全部value，只要算大于平方根不整除
            String result = "";
            int i = 2;
            while (true){
                if(value%i==0){
                   value = value/i;
                   result = result + i+" ";
                    if(value==1){//整除为1，退出
                        break;
                    }
                }else{
                    //未整除，累加1，再整除
                    i++;
                    if(i>k){//整除为本身值，退出
                        result = result + value+" ";
                        break;
                    }
                }
            }
            System.out.println(result);
        }
    }
}
