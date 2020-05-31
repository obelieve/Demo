package com.zxy.mall.entity;
import java.util.List;

public class FoodEntity {

    private boolean top;
    private int goods_type;
    private String goods_type_name;
    private String name;
    private int price;
    private String oldPrice;
    private String description;
    private int sellCount;
    private int rating;
    private String info;
    private List<RatingEntity> ratings;
    private String icon;
    private String image;

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public int getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(int goods_type) {
        this.goods_type = goods_type;
    }

    public String getGoods_type_name() {
        return goods_type_name;
    }

    public void setGoods_type_name(String goods_type_name) {
        this.goods_type_name = goods_type_name;
    }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setPrice(int price) {
         this.price = price;
     }
     public int getPrice() {
         return price;
     }

    public void setOldPrice(String oldPrice) {
         this.oldPrice = oldPrice;
     }
     public String getOldPrice() {
         return oldPrice;
     }

    public void setDescription(String description) {
         this.description = description;
     }
     public String getDescription() {
         return description;
     }

    public void setSellCount(int sellCount) {
         this.sellCount = sellCount;
     }
     public int getSellCount() {
         return sellCount;
     }

    public void setRating(int rating) {
         this.rating = rating;
     }
     public int getRating() {
         return rating;
     }

    public void setInfo(String info) {
         this.info = info;
     }
     public String getInfo() {
         return info;
     }

    public void setRatings(List<RatingEntity> ratings) {
         this.ratings = ratings;
     }
     public List<RatingEntity> getRatings() {
         return ratings;
     }

    public void setIcon(String icon) {
         this.icon = icon;
     }
     public String getIcon() {
         return icon;
     }

    public void setImage(String image) {
         this.image = image;
     }
     public String getImage() {
         return image;
     }

}
