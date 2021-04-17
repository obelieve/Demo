package com.zxy.demo.hw;


import java.util.Scanner;

/**
 * @Description: 百鸡百钱问题。公元前五世纪，我国古代数学家张丘建在《算经》一书中提出了“百鸡问题”：鸡翁一值钱五，鸡母一值钱三，
 * 鸡雏三值钱一。百钱买百鸡，问鸡翁、鸡母、鸡雏各几何？
 * 输入：任何一个整数，为了运行程序         输出：list，list为鸡翁、鸡母、鸡雏组合的列表。
 * 例：输入：1           输出：0 25 75
 *                           4 18 78
 *                           8 11 81
 *                           12 4 84
 */
public class HJ72_2_right {

    public static void main(String[] args) throws Exception{
        Scanner scanner = new Scanner(System.in);
        if(scanner.hasNext()){
            for100_100();
        }
    }

     //100 100
    /** x = 5  1
     *  y = 3  1
     *  z = 1  3
     * f(x,y,z) = x + y +z = 100
     * f(x,y,z) = 5x+3y+z/3 = 100
     *            z*2/3-2y-4x = 0
     */
    // 100 0
    public static void for100_100(){
        for(int i=0;i<=20;i++){
            int price = 100-i*5;
            y(price,i);
        }
    }

    public static void y(int price,int x){
        for(int y=0;y<price/3;y++){
            int priceY = price-y*3;
            z(priceY,x,y);
        }
    }

    public static void z(int price,int x,int y){
        if(x+y+price*3==100){
            System.out.println(x+" "+y+" "+price*3);
        }
    }
}
