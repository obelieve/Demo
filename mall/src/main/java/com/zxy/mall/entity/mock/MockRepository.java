package com.zxy.mall.entity.mock;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.SparseArray;

import com.zxy.frame.net.gson.MGson;
import com.zxy.mall.entity.FoodEntity;
import com.zxy.mall.entity.GoodsEntity;
import com.zxy.mall.entity.RatingEntity;
import com.zxy.mall.entity.SellerEntity;
import com.zxy.utility.AssetsUtil;

import java.util.ArrayList;
import java.util.List;

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

    public static List<GoodsEntity> getGoodList(){
        return sMockEntity.getGoods();
    }

    public static List<RatingEntity> getRatingList(){
        return sMockEntity.getRatings();
    }

    public static FoodEntity getFoodEntity(String type,String foodName){
        List<GoodsEntity> list = sMockEntity.getGoods();
        if(list!=null){
            for(GoodsEntity entity:list){
                if(!TextUtils.isEmpty(type)&&type.equals(entity.getName())){
                    if(entity.getFoods()!=null){
                        for(FoodEntity foodEntity:entity.getFoods()){
                            if(!TextUtils.isEmpty(foodName)&&foodName.equals(foodEntity.getName())){
                                return foodEntity;
                            }
                        }
                    }
                }
            }
        }
        return new FoodEntity();
    }

    public static List<FoodEntity> getFoodList(){
        List<FoodEntity> list = new ArrayList<>();
        SparseArray<List<FoodEntity>> array= new SparseArray<>();
        if(sMockEntity.getGoods()!=null){
            int key =0;
            for(GoodsEntity entity:sMockEntity.getGoods()){
                List<FoodEntity> cList;
                if(array.get(key)==null){
                    cList = new ArrayList<>();
                    array.put(key,cList);
                }
                cList= array.get(key);
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
                key++;
            }
            for(int i=0;i<array.size();i++){
                list.addAll(array.get(i));
            }
        }
        return list;
    }

    public static Handler getHandler() {
        return sHandler;
    }
}
