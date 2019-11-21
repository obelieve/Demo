package com.example.entity;

public class SlidingItem {
	private String title;
	private int icon;
	private String count="0";
	private boolean isCounterVisible=false;
	public SlidingItem(){
		
	}
	public SlidingItem(String title,int icon){
		this.title=title;
		this.icon=icon;
	}
	public SlidingItem(String title,int icon,boolean isCounterVisible,String count){
		this.title=title;
		this.icon=icon;
		this.isCounterVisible=isCounterVisible;
		this.count=count;
	}
	public String getTitle(){
		return this.title;
	}
	public int getIcon() {
		return icon;
	}
	public String getCount() {
		return count;		
	}
	public boolean getIsCounterVisible() {
		return this.isCounterVisible;		
	}
	public void setTitle(String title) {
		this.title=title;		
	}
	public void setIcon(int icon) {
		this.icon=icon;
	}
	public void setCount(String count) {
		this.count=count;		
	}
	public void setIsCounterVisible(Boolean isCounterVisible) {
		this.isCounterVisible=isCounterVisible;		
	}
}
