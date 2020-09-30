package com.github.obelieve.community.bean;

import java.util.Set;

public class PostFilterCache {
    private int curUserId;
    private Set<Integer> userIds;
    private Set<Integer> postIds;

    public int getCurUserId() {
        return curUserId;
    }

    public void setCurUserId(int curUserId) {
        this.curUserId = curUserId;
    }

    public Set<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(Set<Integer> userIds) {
        this.userIds = userIds;
    }

    public Set<Integer> getPostIds() {
        return postIds;
    }

    public void setPostIds(Set<Integer> postIds) {
        this.postIds = postIds;
    }
}
