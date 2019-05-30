package com.zxy.demo.mock_data;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class GoodsData {

    private int id;//id
    private String name;//名称
    private String content;//商品内容
    private String category;//类别
    private float origin_price;//原价
    private float discount_price;//折扣价
    private List<String> goods_labels;//商品标签
    private String date_label;//日期标签
    private int inventory;//库存
    private String goods_brand;//商品牌子

    public static List<GoodsData> getList() {
        return getList(100);
    }

    public static List<GoodsData> getList(int size) {

        List<GoodsData> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            GoodsData data = new GoodsData();
            data.setId(UUID.randomUUID().hashCode());
            data.setName(RandomString.getName());
            data.setContent(RandomString.getContent());
            data.setCategory(CategoryEnum.values()[random.nextInt(CategoryEnum.values().length)].toString());
            data.setOrigin_price(random.nextFloat());
            data.setDiscount_price(random.nextFloat());
            data.setGoods_labels(Arrays.asList(RandomString.getName(),RandomString.getName(),RandomString.getName()));
            data.setDate_label(DateLebelEnum.values()[random.nextInt(DateLebelEnum.values().length)].toString());
            data.setInventory(2);
            data.setGoods_brand(BrandEnum.values()[random.nextInt(BrandEnum.values().length)].toString());
        }
        return list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getOrigin_price() {
        return origin_price;
    }

    public void setOrigin_price(float origin_price) {
        this.origin_price = origin_price;
    }

    public float getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(float discount_price) {
        this.discount_price = discount_price;
    }

    public List<String> getGoods_labels() {
        return goods_labels;
    }

    public void setGoods_labels(List<String> goods_labels) {
        this.goods_labels = goods_labels;
    }

    public String getDate_label() {
        return date_label;
    }

    public void setDate_label(String date_label) {
        this.date_label = date_label;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public String getGoods_brand() {
        return goods_brand;
    }

    public void setGoods_brand(String goods_brand) {
        this.goods_brand = goods_brand;
    }

    public static class RandomString {

        static Random random = new Random();

        public static String getName() {
            return getString(10);
        }

        public static String getContent() {
            return getString(30);
        }

        private static String getString(int size) {
            byte[] bytes = new byte[size];
            for(int i=0;i<size;i++){
                bytes[i]=(byte)random.nextInt(128);
            }
            return new String(bytes, Charset.forName("utf-8"));
        }
    }

    public enum LabelEnum {
        NULL("", ""), DISCOUNT("折扣", "#E94659"), NEW_PRODUCT("新品", "#49A7C9");

        private String name;
        private String color;

        public String getName() {
            return name;
        }

        public String getColor() {
            return color;
        }

        LabelEnum(String name, String color) {
            this.name = name;
            this.color = color;
        }

        @Override
        public String toString() {
            return "LabelEnum{" +
                    "name='" + name + '\'' +
                    ", color='" + color + '\'' +
                    '}';
        }
    }

    public enum CategoryEnum {

        FRUITS("水果"), VEGETABLES("蔬菜"), MEAT_AND_EGG("肉禽蛋");

        private String name;

        CategoryEnum(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "CategoryEnum{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    public enum DateLebelEnum {

        TODAY("今日上市"), YESTERDAY("昨天上市");

        private String name;

        DateLebelEnum(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "DateLebelEnum{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    public enum BrandEnum {

    }


    @Override
    public String toString() {
        return "GoodsData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", category=" + category +
                ", origin_price=" + origin_price +
                ", discount_price=" + discount_price +
                ", goods_labels=" + goods_labels +
                ", date_label=" + date_label +
                ", inventory=" + inventory +
                ", goods_brand=" + goods_brand +
                '}';
    }
}
