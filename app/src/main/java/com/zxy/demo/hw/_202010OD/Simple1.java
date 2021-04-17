package com.zxy.demo.hw._202010OD;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 简单题 1  用例通过率：90%  cost:1h50m
 * 成语接龙：
 * 单词尾部和接入下一个单词的首部相同，
 * 如果下一个单词首部 匹配有多个：
 * 1.选长度长的
 * 2.长度相同，选择单词序列低的。
 * 输入
 * k  第几个单词开始
 * n  数组的单词数量
 * 多个单词 数组的单词
 * 输出
 * 一个按字母先后排序的字符串
 */
public class Simple1{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();
        int n = scanner.nextInt();
        String[] words = new String[n];
        for(int i=0;i<n;i++){
            words[i] = scanner.next();
        }
        String maxLengthStr = result(k,words);
        System.out.println(maxLengthStr);
    }

    public static String result(int start,String[] words){
        int cur = start;
        List<Integer> list = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        while (cur!=-1){
            list.add(cur);
            cur = getNextWord(cur,words,temp);
        }
        String result = "";
        for(int i:list){
            result+=words[i];
        }
        return result;
    }

    public static int getNextWord(int start,String[] words,List<Integer> temp){
        //#
        String s = words[start];
        char end = s.charAt(s.length()-1);
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<words.length;i++){
            if(i!=start&&!temp.contains(i)&&end==words[i].charAt(0)){
                list.add(i);
            }
        }
        if(list.size()>0){
            if(list.size()==1){
                temp.add(list.get(0));
                return list.get(0);
            }else{
                return matchWord(list,words,temp);
            }
        }
        return -1;
    }

    public static int matchWord(List<Integer> list,String[] words,List<Integer> temp){
        String[] match = new String[list.size()];
        List<Integer> m = new ArrayList<>();
        int maxLength = 0;

        for(int i=0;i<list.size();i++){
            match[i]=words[list.get(i)];
            if(match[i].length()>maxLength){
                maxLength = match[i].length();
            }
        }
        for(int i=0;i<list.size();i++){
            match[i]=words[list.get(i)];
            if(match[i].length()==maxLength){
                m.add(list.get(i));
            }
        }
        if(m.size()==1){
            temp.add(m.get(0));
            return m.get(0);
        }else{
            int maxW = m.get(0);
            for(int i=1;i<m.size();i++){
                maxW = maxWord(maxW,m.get(i),words);
            }
            temp.add(maxW);
            return maxW;
        }
    }

    public static int maxWord(int s1,int s2,String[] words){
        String str1 = words[s1];
        String str2 = words[s2];
        for(int i=0;i<str1.length();i++){
            if(str1.charAt(i)<str2.charAt(i)){
                return s1;
            }else if(str1.charAt(i)>str2.charAt(i)){
                return s2;
            }
        }
        return s2;
    }

}