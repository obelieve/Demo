package com.zxy.demo.hw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * @Description: 输入n个整数，输出其中最小的k个。
 * 输入描述：第一行输入两个整数n、k，n为数组元素个数，k为需要输出的最小的个数，第二行输入一个整数数组
 * 输出描述：输出一个整数数组
 * 例：输入：5 2             输出：1 2
 *          1 3 5 7 2
 */
public class HJ58_2_write {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while (input.hasNextLine()){
            String[] nk = input.nextLine().split(" ");
            int n = Integer.parseInt(nk[0]);
            int k = Integer.parseInt(nk[1]);
            String[] arrayStr = input.nextLine().split(" ");
            List<Integer> list = new ArrayList<>();
            if(n==arrayStr.length){
                for(int i=0;i<arrayStr.length;i++){
                    list.add(Integer.parseInt(arrayStr[i]));
                }
                Collections.sort(list);
                for(int i=0;i<k;i++){
                    System.out.print(list.get(i));
                    if(i!=k-1){
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }
        }
    }
}
