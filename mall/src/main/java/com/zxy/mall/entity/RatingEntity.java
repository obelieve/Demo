package com.zxy.mall.entity;
import java.util.List;


public class RatingEntity {

    private String username;
    private long rateTime;
    private int deliveryTime;
    private int score;
    private int rateType;
    private String text;
    private String avatar;
    private List<String> recommend;
    public void setUsername(String username) {
         this.username = username;
     }
     public String getUsername() {
         return username;
     }

    public void setRateTime(long rateTime) {
         this.rateTime = rateTime;
     }
     public long getRateTime() {
         return rateTime;
     }

    public void setDeliveryTime(int deliveryTime) {
         this.deliveryTime = deliveryTime;
     }
     public int getDeliveryTime() {
         return deliveryTime;
     }

    public void setScore(int score) {
         this.score = score;
     }
     public int getScore() {
         return score;
     }

    public void setRateType(int rateType) {
         this.rateType = rateType;
     }
     public int getRateType() {
         return rateType;
     }

    public void setText(String text) {
         this.text = text;
     }
     public String getText() {
         return text;
     }

    public void setAvatar(String avatar) {
         this.avatar = avatar;
     }
     public String getAvatar() {
         return avatar;
     }

    public void setRecommend(List<String> recommend) {
         this.recommend = recommend;
     }
     public List<String> getRecommend() {
         return recommend;
     }

}
