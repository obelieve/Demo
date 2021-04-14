package com.zxy.demo.hw;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**

 * @Description: 1、中文大写金额数字前应标明“人民币”字样。中文大写金额数字应用壹、贰、叁、肆、伍、陆、柒、捌、玖、拾、佰、仟、万、
 * 亿、元、角、分、零、整等字样填写。（30分）
 * 2、中文大写金额数字到“元”为止的，在“元”之后，应写“整”字，如￥ 532.00应写成“人民币伍佰叁拾贰元整”。在“角”和“分”后面不写“整”字。（30分）
 * 3、阿拉伯数字中间有“0”时，中文大写要写“零”字，阿拉伯数字中间连续有几个“0”时，中文大写金额中间只写一个“零”字，如￥6007.14，应写
 * 成“人民币陆仟零柒元壹角肆分”。（40分）
 * 输入描述：输入一个double数         输出描述：输出人民币格式
 * 例：输入：151121.15               输出：人民币拾伍万壹仟壹佰贰拾壹元壹角伍分
 *               //*亿*万*仟*佰*拾*万*仟*佰*拾
 *               1
 *               10     *拾
 *               100    *佰*拾
 *               1000   *仟*佰*拾
 *               10000  *万（*仟*佰*拾）
 *               100000 *拾*万*仟*佰*拾
 *               1000000 *佰*拾*万*仟*佰*拾
 *               10000000 （*仟*佰*拾）*万（*仟*佰*拾）
 *               100000000 *亿*仟*佰*拾*万*仟*佰*拾
 *               100000000 *拾*亿*仟*佰*拾*万*仟*佰*拾
 *               1000000000 *佰*拾*亿*（仟*佰*拾*）万（*仟*佰*拾）
 *               10000000000 （*仟*佰*拾*）亿（*仟*佰*拾）*万（*仟*佰*拾）
 *               10000000000 *万*仟*佰*拾*亿*仟*佰*拾*万*仟*佰*拾
 *               10000000000 *拾*万*仟*佰*拾*亿*仟*佰*拾*万*仟*佰*拾
 *               10000000000 *佰*拾*万*仟*佰*拾*亿*仟*佰*拾*万*仟*佰*拾
 *               10000000000 *亿（*仟*佰*拾）*万（*仟*佰*拾）*亿（*仟*佰*拾）*万（*仟*佰*拾）
 */
public class HJ95_4_note {
    private final static char[] digit = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖', '拾', '佰', '仟', '万', '亿'};
    private final static char[] digitFlag = {'拾','佰','仟','万','拾','佰','仟','亿'};
    private final static char[] unit = {'元','角','分'};
    private final static char yuanTag = '整';
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextDouble()){
            double price = scanner.nextDouble();
            String[] priceTemp = (price+"").split("\\.");
            String[] priceValue = new String[2];
            if(priceTemp.length==1){
                priceValue[0] = priceTemp[0];
                priceValue[1] = "00";
            }else if(priceTemp.length==2){
                priceValue[0] = priceTemp[0];
                if(priceTemp[1].equals("0")){//xx.0去掉 .0
                    priceValue[1] = "00";
                }
                if(priceTemp[1].length()>2){
                    if(priceTemp[1].contains("E")){//指数型10次方
                        String add = "";
                        for(int i=0;i<Integer.parseInt(priceTemp[1].substring(priceTemp[1].lastIndexOf("E")+1));i++){
                            add+="0";
                        }
                        priceValue[0] = priceValue[0]+add;
                        priceTemp[1] = priceTemp[1].substring(0,priceTemp[1].lastIndexOf("E"));
                    }

                    if(priceTemp[1].length()>2){//排除x.0E10这种类型
                        priceValue[1] = priceTemp[1].substring(0,2);
                    }else{
                        priceValue[1] = priceTemp[1];
                    }
                }else {
                    priceValue[1] = priceTemp[1];
                }
            }
            String rmb = printRMB(priceValue[0]);
            if(priceValue[1].equals("00")){
                System.out.println("人民币"+rmb+unit[0]+yuanTag);
            }else{
                System.out.println("人民币"+rmb+unit[0]+pricePointRMB(priceValue[1]));
            }
        }
    }

    /**
     *  *元
     * @param yuan
     * @return
     */
    public static String printRMB(String yuan){
        StringBuilder result = new StringBuilder();
        String[] numStrArray = yuan.split("");
        char[] cnArray = new char[numStrArray.length];
        char[] cnFlagArray = new char[numStrArray.length];
        int flag = 0;
        for(int i=numStrArray.length-1;i>=0;i--){
            int num = Integer.parseInt(numStrArray[i]);
            cnArray[i] = digit[num];
            cnFlagArray[i] = digitFlag[flag%digitFlag.length];
            flag++;
        }
        for(int i=0;i<cnArray.length;i++){
            result.append(cnArray[i]+"" + cnFlagArray[i]);
        }
        return result.toString().replace(" ","");
    }

    /**
     * *角*分
     * @param point
     * @return
     */
    public static String pricePointRMB(String point){
        String result = "";
        String[] value = point.split("");
        if(value.length==1){
            if("0".equals(value[0])){
                return result;
            }
            result+=digit[Integer.parseInt(value[0])]+""+unit[1];
        }else if(value.length==2){
            result+=digit[Integer.parseInt(value[0])]+""+unit[1];
            result+=digit[Integer.parseInt(value[1])]+""+unit[2];
        }
        return result;
    }
}
