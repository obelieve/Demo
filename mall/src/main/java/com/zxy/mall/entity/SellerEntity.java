package com.zxy.mall.entity;
import java.util.List;

public class SellerEntity {

    private String name;
    private String description;
    private int deliveryTime;
    private double score;
    private double serviceScore;
    private double foodScore;
    private double rankRate;
    private int minPrice;
    private int deliveryPrice;
    private int ratingCount;
    private int sellCount;
    private String bulletin;
    private List<SupportEntity> supports;
    private String avatar;
    private List<String> pics;
    private List<String> infos;
    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setDescription(String description) {
         this.description = description;
     }
     public String getDescription() {
         return description;
     }

    public void setDeliveryTime(int deliveryTime) {
         this.deliveryTime = deliveryTime;
     }
     public int getDeliveryTime() {
         return deliveryTime;
     }

    public void setScore(double score) {
         this.score = score;
     }
     public double getScore() {
         return score;
     }

    public void setServiceScore(double serviceScore) {
         this.serviceScore = serviceScore;
     }
     public double getServiceScore() {
         return serviceScore;
     }

    public void setFoodScore(double foodScore) {
         this.foodScore = foodScore;
     }
     public double getFoodScore() {
         return foodScore;
     }

    public void setRankRate(double rankRate) {
         this.rankRate = rankRate;
     }
     public double getRankRate() {
         return rankRate;
     }

    public void setMinPrice(int minPrice) {
         this.minPrice = minPrice;
     }
     public int getMinPrice() {
         return minPrice;
     }

    public void setDeliveryPrice(int deliveryPrice) {
         this.deliveryPrice = deliveryPrice;
     }
     public int getDeliveryPrice() {
         return deliveryPrice;
     }

    public void setRatingCount(int ratingCount) {
         this.ratingCount = ratingCount;
     }
     public int getRatingCount() {
         return ratingCount;
     }

    public void setSellCount(int sellCount) {
         this.sellCount = sellCount;
     }
     public int getSellCount() {
         return sellCount;
     }

    public void setBulletin(String bulletin) {
         this.bulletin = bulletin;
     }
     public String getBulletin() {
         return bulletin;
     }

    public void setSupports(List<SupportEntity> supports) {
         this.supports = supports;
     }
     public List<SupportEntity> getSupports() {
         return supports;
     }

    public void setAvatar(String avatar) {
         this.avatar = avatar;
     }
     public String getAvatar() {
         return avatar;
     }

    public void setPics(List<String> pics) {
         this.pics = pics;
     }
     public List<String> getPics() {
         return pics;
     }

    public void setInfos(List<String> infos) {
         this.infos = infos;
     }
     public List<String> getInfos() {
         return infos;
     }

}
