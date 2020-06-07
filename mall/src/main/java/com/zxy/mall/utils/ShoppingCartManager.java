package com.zxy.mall.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShoppingCartManager {

    private static ShoppingCartManager sShoppingCartManager = new ShoppingCartManager();
    private static final int DEF_PRICE = 20;
    private Map<String, GoodsBean> mGoodsBeanMap = new HashMap<>();
    private Set<Listener> mListenerSet = new HashSet<>();

    private ShoppingCartManager() {

    }

    public static ShoppingCartManager getInstance() {
        return sShoppingCartManager;
    }

    public void addListener(Listener listener) {
        mListenerSet.add(listener);
    }

    public void removeListener(Listener listener) {
        mListenerSet.remove(listener);
    }


    public float deliveryPrice() {
        return DEF_PRICE;
    }

    public synchronized GoodsBean findGoods(String id) {
        return mGoodsBeanMap.get(id);
    }

    public synchronized List<GoodsBean> getGoodsList() {
        return new ArrayList<>(mGoodsBeanMap.values());
    }


    public synchronized int getCount() {
        int count = 0;
        for (GoodsBean bean : mGoodsBeanMap.values()) {
            count += bean.getCount();
        }
        return count;
    }

    public synchronized float getSumPrice() {
        float price = 0;
        for (GoodsBean bean : mGoodsBeanMap.values()) {
            price += bean.getPrice() * bean.getCount();
        }
        return price;
    }


    public synchronized void addGoods(String id, String name, float price, int count, int maxCount) {
        if (TextUtils.isEmpty(id)) return;
        GoodsBean bean = mGoodsBeanMap.get(id);
        if (bean == null) {
            bean = new GoodsBean();
            bean.setId(id);
            bean.setCount(count);
            bean.setName(name);
            bean.setPrice(price);
            bean.setMaxCount(maxCount);
            mGoodsBeanMap.put(id, bean);
        } else {
            bean.setId(id);
            bean.setCount(count);
            bean.setName(name);
            bean.setPrice(price);
            bean.setMaxCount(maxCount);
        }
        for (Listener listener : mListenerSet) {
            listener.onGoodsNotifyChanged(bean.getId(), bean.getName(), bean.getPrice(), bean.getCount(), bean.getMaxCount());
        }
    }

    public synchronized void removeGoods(String id, String name, float price,int count) {
        if (TextUtils.isEmpty(id)) return;
        GoodsBean bean = mGoodsBeanMap.get(id);
        if (bean != null) {
            if (bean.getCount() == 0) {
                mGoodsBeanMap.remove(id);
            } else {
                bean.setId(id);
                bean.setCount(count);
                bean.setName(name);
                bean.setPrice(price);
            }
            for (Listener listener : mListenerSet) {
                listener.onGoodsNotifyChanged(bean.getId(), bean.getName(), bean.getPrice(), bean.getCount(), bean.getMaxCount());
            }
        }
    }

    public synchronized void clearAll() {
        for (Listener listener : mListenerSet) {
            listener.onClearAll(new ArrayList<>(mGoodsBeanMap.keySet()));
        }
        mGoodsBeanMap.clear();
    }

    public static class GoodsBean {

        private String mId;
        private String mName;
        private float mPrice;
        private int mCount;
        private int mMaxCount;

        public String getId() {
            return mId;
        }

        public void setId(String id) {
            mId = id;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public float getPrice() {
            return mPrice;
        }

        public void setPrice(float price) {
            mPrice = price;
        }

        public int getCount() {
            return mCount;
        }

        public void setCount(int count) {
            mCount = count;
        }

        public int getMaxCount() {
            return mMaxCount;
        }

        public void setMaxCount(int maxCount) {
            mMaxCount = maxCount;
        }

        @Override
        public String toString() {
            return "GoodsBean{" +
                    "mId='" + mId + '\'' +
                    ", mName='" + mName + '\'' +
                    ", mPrice=" + mPrice +
                    ", mCount=" + mCount +
                    ", mMaxCount=" + mMaxCount +
                    '}';
        }
    }

    public interface Listener {
        void onGoodsNotifyChanged(String id, String name, float price, int count, int maxCount);

        void onClearAll(List<String> list);
    }
}
