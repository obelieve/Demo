package com.zxy.demo.hw;

import java.util.Scanner;
import java.util.regex.Pattern;

/**

 * @Description: 密码按如下规则进行计分，并根据不同的得分为密码进行安全等级划分。
 * 一、密码长度： 5分: 小于等于4个字符    10分: 5到7字符                25分: 大于等于8个字符
 * 二、字母：     0分: 没有字母          10分: 全都是小（大）写字母     20分: 大小写混合字母
 * 三、数字:      0分: 没有数字          10分: 1个数字                20分: 大于1个数字
 * 四、符号:      0分: 没有符号          10分: 1个符号                25分: 大于1个符号
 * 五、奖励:      2分: 字母和数字         3分: 字母、数字和符号         5分: 大小写字母、数字和符号
 *
 * 最后的评分标准:  >= 90: 非常安全      >= 80: 安全（Secure）      >= 70: 非常强      >= 60: 强（Strong）
 *                >= 50: 一般（Average）    >= 25: 弱（Weak）      >= 0:  非常弱
 * 对应的输出为：VERY_SECURE, SECURE, VERY_STRONG, STRONG, AVERAGE, WEAK, VERY_WEAK,
 * 例： 输入：38$@NoNoNo     输出：VERY_SECURE
 */
public class HJ87_3_write {

    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        while (input.hasNext()){
            String str = input.next();
            String result = rank(scan_num_symbol_abc(str)+scanLength(str));
            System.out.println(result);
        }
    }

    public static int scanLength(String str){
        if(str.length()>=8){
            return 25;
        }else if(str.length()>=5){
            return 10;
        }else {
            return 5;
        }
    }

    public static int scan_num_symbol_abc(String str){
        int num = 0;
        int abc = 0;
        int ABC = 0;
        int symbol = 0;
        int count = 0;
        for(int i=0;i<str.length();i++){
            char c = str.charAt(i);
            if(c>='0'&&c<='9'){
                num++;
            }
            if((c>='a'&&c<='z')){
                abc++;
            }
            if((c>='A'&&c<='Z')){
                ABC++;
            }
            if(!(c>='0'&&c<='9')&&!(c>='a'&&c<='z')&&!(c>='A'&&c<='Z')){
                symbol++;
            }
        }
        if(abc==str.length()||ABC==str.length()){
            count+=10;
        }else if(abc>0&&ABC>0){
            count+=20;
        }

        if(num>1){
            count+= 20;
        }else if(num==1){
            count+= 10;
        }

        if(symbol>1){
            count+=25;
        }else if(symbol==1){
            count+= 10;
        }
        if(num>0&&abc>0&&ABC>0&&symbol>0){
            count+=5;
        }else if(num>0&&(abc>0||ABC>0)&&symbol>0){
            count+=3;
        }else if(num>0&&(abc>0||ABC>0)){
            count+=2;
        }
        return count;
    }

    public static String rank(int value){
        String result;
        if(value>=90){
            result="VERY_SECURE";
        }else if(value>=80){
            result="SECURE";
        }else if(value>=70){
            result="VERY_STRONG";
        }else if(value>=60){
            result="STRONG";
        }else if(value>=50){
            result="AVERAGE";
        }else if(value>=25){
            result="WEAK";
        }else {
            result="VERY_WEAK";
        }
        return result;
    }
}
