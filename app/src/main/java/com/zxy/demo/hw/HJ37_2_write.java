package com.zxy.demo.hw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * @Description: 有一只兔子，从出生后第3个月起每个月都生一只兔子，小兔子长到第三个月后每个月又生一只兔子，假如兔子都不死，问
 * 每个月的兔子总数为多少？
 * 输入描述：输入int型表示month     输出描述：输出兔子总数int类型
 * 例：输入：9     输出：34
 */
public class HJ37_2_write {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextInt()){
            int month = scanner.nextInt();
            int count = tuCount(month);
            System.out.println(count);
        }
    }

    public static int tuCount(int month){
        if(month<3){
            return 1;
        }else{
            int count = 1;
            for(int i=month-2;i>=1;i--){
                count+=tuCount(i);
            }
            return count;
        }
    }
}
