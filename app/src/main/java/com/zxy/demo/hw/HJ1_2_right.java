package com.zxy.demo.hw;

import java.util.Scanner;

/**
 * # 字符串	简单
 * @Description: 计算字符串最后一个单词的长度，单词以空格隔开。
 * 输入描述：一行字符串，非空，长度小于5000       输出描述：整数N，最后一个单词的长度
 * 例：输入："hello world"     输出：5
 */
public class HJ1_2_right {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String line = scanner.nextLine();
            int lastIndex = line.lastIndexOf(" ");
            System.out.println(line.substring(lastIndex+1).length());
        }
    }
}
