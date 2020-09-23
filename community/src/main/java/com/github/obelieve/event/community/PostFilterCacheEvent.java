package com.github.obelieve.event.community;

public class PostFilterCacheEvent {

    private String tag;
    private int post_id;
    private int user_id;

    public PostFilterCacheEvent() {
    }

    public PostFilterCacheEvent(String tag, int post_id, int user_id) {
        this.tag = tag;
        this.post_id = post_id;
        this.user_id = user_id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
