package com.zxy.mall.entity.mock;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.zxy.frame.net.gson.MGson;
import com.zxy.mall.entity.FoodEntity;
import com.zxy.mall.entity.GoodEntity;
import com.zxy.mall.entity.RatingEntity;
import com.zxy.mall.entity.SellerEntity;
import com.zxy.utility.AssetsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据模拟
 */
public class MockRepository {

    static MockEntity sMockEntity;

    private static Handler sHandler = new Handler(Looper.myLooper());

    public static void init(Context context){
        String json = AssetsUtil.getAssetsContent(context,"data.json");
        sMockEntity = MGson.newGson().fromJson(json,MockEntity.class);
    }


    public static SellerEntity getSeller(){
        return sMockEntity.getSeller();
    }

    public static List<GoodEntity> getGoodList(){
        return sMockEntity.getGoods();
    }

    public static List<RatingEntity> getRatingList(){
        return sMockEntity.getRatings();
    }

    public static List<FoodEntity> getFoodList(){
        List<FoodEntity> list = new ArrayList<>();
        Map<String,List<FoodEntity>> map = new HashMap<>();
        if(sMockEntity.getGoods()!=null){
            for(GoodEntity entity:sMockEntity.getGoods()){
                String key = entity.getType()+"@"+entity.getName();
                List<FoodEntity> cList;
                if(!map.containsKey(key)){
                    cList = new ArrayList<>();
                    map.put(key,cList);
                }
                cList= map.get(key);
                if(entity.getFoods()!=null){
                    for(int i=0;i<entity.getFoods().size();i++){
                        FoodEntity foodEntity = entity.getFoods().get(i);
                        foodEntity.setGoods_type(entity.getType());
                        foodEntity.setGoods_type_name(entity.getName());
                        if(i==0){
                            foodEntity.setTop(true);
                        }
                        cList.add(foodEntity);
                    }
                }
            }
            for(Map.Entry<String,List<FoodEntity>> entry:map.entrySet()){
                list.addAll(entry.getValue());
            }
        }
        return list;
    }

    public static Handler getHandler() {
        return sHandler;
    }
}
