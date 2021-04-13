package com.zxy.demo.hw;

import java.util.Scanner;

/**
 * @Description: 连续输入字符串，请按长度为8拆分每个字符串后输出到新的字符串数组；长度不是8整数倍的字符串请在后面补数字0，空字符串不处理。
 * 输入描述：连续输入字符串(输入2次,每个字符串长度小于100)      输出描述：输出到长度为8的新字符串数组
 * 例：输入：abc                 输出：abc00000
 *          123456789                12345678
 *                                   90000000
 */
public class HJ4_2_right {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String line = scanner.nextLine();
            while (line.length()>8){
                System.out.println(line.substring(0,8));
                line = line.substring(8);
            }
            if(line.length()>0){
                int left = 8-line.length();
                for(int i=0;i<left;i++){
                    line+="0";
                }
                System.out.println(line);
            }
        }
    }
}
