package com.zxy.demo.hw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
 * @Description: 删除字符串中出现次数最少的字符，若多个字符出现次数一样，则都删除。输出删除这些单词后的字符串，字符串中其它字符保持
 * 原来的顺序。
 * 输入描述：字符串只包含小写英文字母，不考虑非法输入，输入的字符串长度小于等于20个字节。
 * 输出描述：删除字符串中出现次数最少的字符后的字符串。
 * 例：输入：abcdd       输出：dd
 */
public class HJ23_3_write {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String next = scanner.next();
            int i=0;
            Map<Character,Integer> map = new HashMap<>();
            //1.字符串，每个字符出现的次数
            while (i<=next.length()-1){
                Integer value = map.get(next.charAt(i));
                if(value==null){
                    map.put(next.charAt(i),1);
                }else{
                    map.put(next.charAt(i),1+value);
                }
                i++;
            }
            int min=0;
            boolean first = true;
            List<Character> minList = new ArrayList<>();
            //2.获取出现次数最少的字符
            for(Map.Entry<Character,Integer> entry:map.entrySet()){
                if(first){
                    min = entry.getValue();
                    minList.add(entry.getKey());
                    first=false;
                }
                if(entry.getValue()<min){
                    minList.clear();
                    min =entry.getValue();
                    minList.add(entry.getKey());
                }else if(entry.getValue()==min){
                    minList.add(entry.getKey());
                }
            }
            //3.把出现最少的字符移除
            for(int j=0;j<minList.size();j++){
                next = next.replace(minList.get(j)+"","");
            }

            System.out.println(next);
        }
    }

}
