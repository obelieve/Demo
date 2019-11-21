package com.example.variety.define;

public class DrawArea {
	private float minX=0;
	private float maxX=0;
	private float minY=0;
	private float maxY=0;
	/*
	 *init方法为，初始化
	 */
	public void init(float x,float y){
		minX=x;
		maxX=x;
		minY=y;
		maxY=y;
	}
	/*
	 *setXY()方法根据参数x获得以后剪切图片的最大，最小x,y坐标值
	 */
	public void setXY(float x,float y){
		minX =(x <minX ? x:minX);
		maxX =(x >maxX ? x:maxX);
		minY =(y <minY ? y:minY);
		maxY =(y >maxY ? y:maxY);
	}
	public float getMinx(){
		return minX;
	}
	public float getMaxX(){
		return maxX;
	}
	public float getMinY() {
		return minY;
	}
	public float getMaxY() {
		return maxY;
	}

}
