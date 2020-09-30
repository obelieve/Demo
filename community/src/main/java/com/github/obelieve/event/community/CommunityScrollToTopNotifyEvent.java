package com.github.obelieve.event.community;

/**
 * Created by zxy
 * on 2020/4/24
 */
public class CommunityScrollToTopNotifyEvent {
    private int type;

    public CommunityScrollToTopNotifyEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
