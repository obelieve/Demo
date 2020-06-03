package com.zxy.mall.entity;
import com.zxy.frame.view.LeftRightRecyclerView;

import java.util.List;

public class GoodEntity implements LeftRightRecyclerView.ILeftData {

    private boolean selected;
    private String name;
    private int type;
    private List<FoodEntity> foods;
    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setType(int type) {
         this.type = type;
     }
     public int getType() {
         return type;
     }

    public void setFoods(List<FoodEntity> foods) {
         this.foods = foods;
     }
     public List<FoodEntity> getFoods() {
         return foods;
     }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public String LRRVTag() {
        return type+"";
    }
}
