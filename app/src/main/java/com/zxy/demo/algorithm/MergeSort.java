package com.zxy.demo.algorithm;

import org.jetbrains.annotations.NotNull;

/**
 * 合并排序
 */
public class MergeSort {

    public static void main(String[] args) {

    }

    public void sort(@NotNull int[] array){
        int size = array.length/2;
        int sizeL = size+1;
        int sizeR = (array.length-size)+1;
        int[] arrayL = new int[sizeL];
        int[] arrayR = new int[sizeR];
        //初始化值
        for(int i=0;i<size;i++){
            arrayL[i] = array[i];
        }
        for(int i=size;i<array.length;i++){
            arrayR[i] = array[i];
        }
        //哨兵
        arrayL[size] = Integer.MAX_VALUE;
        arrayR[array.length-size] = Integer.MAX_VALUE;
        int i = 0;
        int j = 0;
        //
    }

    public void merge(@NotNull int[] left,@NotNull int[] right){

    }
}
