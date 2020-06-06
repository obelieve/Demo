package com.zxy.mall.entity.mock;
import com.zxy.mall.entity.GoodsEntity;
import com.zxy.mall.entity.RatingEntity;
import com.zxy.mall.entity.SellerEntity;

import java.util.List;

public class MockEntity {

    private SellerEntity seller;
    private List<GoodsEntity> goods;
    private List<RatingEntity> ratings;

    public void setSeller(SellerEntity seller) {
         this.seller = seller;
     }
     public SellerEntity getSeller() {
         return seller;
     }

    public void setGoods(List<GoodsEntity> goods) {
         this.goods = goods;
     }
     public List<GoodsEntity> getGoods() {
         return goods;
     }

    public void setRatings(List<RatingEntity> ratings) {
         this.ratings = ratings;
     }
     public List<RatingEntity> getRatings() {
         return ratings;
     }

}
