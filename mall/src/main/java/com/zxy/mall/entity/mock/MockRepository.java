package com.zxy.mall.entity.mock;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.zxy.frame.net.gson.MGson;
import com.zxy.mall.entity.GoodEntity;
import com.zxy.mall.entity.RatingEntity;
import com.zxy.mall.entity.SellerEntity;
import com.zxy.utility.AssetsUtil;

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

    public static List<GoodEntity> getGoodList(){
        return sMockEntity.getGoods();
    }

    public static List<RatingEntity> getRatingList(){
        return sMockEntity.getRatings();
    }


    public static Handler getHandler() {
        return sHandler;
    }
}
