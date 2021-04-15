package com.zxy.demo.hw;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @Description: 根据输入的日期，计算是这一年的第几天。详细描述：输入某年某月某日，判断这一天是这一年的第几天？
 * 输入描述：输入三行，分别是年，月，日           输出描述：成功:返回outDay输出计算后的第几天;失败:返回-1
 * 例：输入：2012            输出：366
 *          12
 *          31
 */

/**
 * 思路
 * 1.闰年 被400整除的 或者 能够被4整除不能被100整除的。
 *
 */
public class HJ73_3_write {
    public static void main(String[] args) {
        int[] yearArray = new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
        int[] yearArray366 = Arrays.copyOf(yearArray,yearArray.length);
        yearArray366[1] = 29;
        Scanner input = new Scanner(System.in);
        while (input.hasNextInt()){
            int year = input.nextInt();
            int month = input.nextInt();
            int day = input.nextInt();
            if(!is366(year)){
                if(day>yearArray[month-1]){
                    System.out.println(-1);
                }else{
                    int count = 0;
                    for(int i=0;i<month-1;i++){
                        count+=yearArray[i];
                    }
                    count+=day;
                    System.out.println(count);
                }
            }else{
                if(day>yearArray366[month-1]){
                    System.out.println("-1");
                }else{
                    int count = 0;
                    for(int i=0;i<month-1;i++){
                        count+=yearArray366[i];
                    }
                    count+=day;
                    System.out.println(count);
                }
            }
        }
    }

    public static boolean is366(int year){
        if(year%400==0||(year%4==0&&year%100!=0)){
            return true;
        }
        return false;
    }
}