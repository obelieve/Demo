package com.zxy.demo.hw;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * @Description: 编写一个函数，计算字符串中含有的不同字符的个数。字符在ACSII码范围内(0~127)，换行表示结束符，不算在字符里。不在范
 * 围内的不作统计。多个相同的字符只计算一次（输入：abaca，输出：3）。
 * 输入描述：输入N个字符，字符在ACSII码范围内。        输出描述：输出范围在(0~127)字符的个数。
 * 例：输入：abc     输出：3
 */
public class HJ10_3_right {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while (input.hasNext()){
            String str = input.next();
            Set<Integer> set = new HashSet<>();
            for(int i=0;i<str.length();i++){
                set.add((int)str.charAt(i));
            }
            System.out.println(set.size());
        }
    }
}
