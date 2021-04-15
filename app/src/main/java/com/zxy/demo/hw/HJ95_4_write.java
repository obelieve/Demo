package com.zxy.demo.hw;


import java.math.BigInteger;
import java.util.Scanner;

/**

 * @Description: 1、中文大写金额数字前应标明“人民币”字样。中文大写金额数字应用壹、贰、叁、肆、伍、陆、柒、捌、玖、拾、佰、仟、万、
 * 亿、元、角、分、零、整等字样填写。（30分）
 * 2、中文大写金额数字到“元”为止的，在“元”之后，应写“整”字，如￥ 532.00应写成“人民币伍佰叁拾贰元整”。在“角”和“分”后面不写“整”字。（30分）
 * 3、阿拉伯数字中间有“0”时，中文大写要写“零”字，阿拉伯数字中间连续有几个“0”时，中文大写金额中间只写一个“零”字，如￥6007.14，应写
 * 成“人民币陆仟零柒元壹角肆分”。（40分）
 * 输入描述：输入一个double数         输出描述：输出人民币格式
 * 例：输入：151121.15               输出：人民币拾伍万壹仟壹佰贰拾壹元壹角伍分
 */

/**
 * 解题思路
 * 1.按照 *100   来分整数和小数部分。
 * 2.利用整除、求余，把(“拾、佰、仟、万、亿”)单位前后分割 （递归）。
 * 3.对于（'佰', '仟', '万', '亿'），余数部分(百<10、千<100等)补零。
 */
public class HJ95_4_write {
    private final static char[] digit = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖', '拾', '佰', '仟', '万', '亿'};

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextDouble()){
            StringBuilder result = new StringBuilder();
            double num = scanner.nextDouble();
            long wholeNum = (long)num;
            int point = (int)(num*100-wholeNum*100+0.001);
            int[] pointNum = new int[2];
            pointNum[0]=point/10;
            pointNum[1]=point%10;
            convert(wholeNum,result);
            if(pointNum[0]==0&&pointNum[1]==0){
                result.append("整");
            }
            if(pointNum[0]!=0){
                result.append(digit[pointNum[0]]);
                result.append("角");
            }
            if(pointNum[1]!=0){
                result.append(digit[pointNum[1]]);
                result.append("分");
            }
            System.out.println(result);
        }

    }


    public static void convert(long num,StringBuilder result){
        if(num>=1_0000_0000){
            long i = num/1_0000_0000;
            long j = num%1_0000_0000;
            convert(i,result);
            result.append("亿");
            if(j!=0){
                if(j<10000){
                    result.append("零");
                }
                convert(j,result);
            }
        }else if(num>=1_0000){
            long i = num/1_0000;
            long j = num%1_0000;
            convert(i,result);
            result.append("万");
            if(j!=0){
                if(j<1000){
                    result.append("零");
                }
                convert(j,result);
            }
        }else if(num>=1_000){
            long i = num/1_000;
            long j = num%1_000;
            convert(i,result);
            result.append("仟");
            if(j!=0){
                if(j<100){
                    result.append("零");
                }
                convert(j,result);
            }
        }else if(num>=100){
            long i = num/100;
            long j = num%100;
            convert(i,result);
            result.append("佰");
            if(j!=0){
                if(j<10){
                    result.append("零");
                }
                convert(j,result);
            }
        }else if(num>=10){
            long i = num/10;
            long j = num%10;
            convert(i,result);
            result.append("拾");
            if(j!=0){
                convert(j,result);
            }
        }else {
            result.append(digit[(int) num]);
        }
    }
}
