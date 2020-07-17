package com.news.mediaplayer.view;

/**
 * Created by Admin
 * on 2020/7/13
 */
public class ListSelectViewDataImpl implements ListSelectView.IListSelectViewData {

    private String name;
    private boolean select;

    public ListSelectViewDataImpl(String name) {
        this.name = name;
    }

    public ListSelectViewDataImpl(String name, boolean select) {
        this.name = name;
        this.select = select;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isSelected() {
        return select;
    }

    @Override
    public void setSelected(boolean selected) {
        this.select = selected;
    }
}
