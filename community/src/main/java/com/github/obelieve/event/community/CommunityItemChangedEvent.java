package com.github.obelieve.event.community;

/**
 * 社区详情（评论/点赞数 是否点赞）更新 通知消息
 * Created by Admin
 * on 2020/9/2
 */
public class CommunityItemChangedEvent {
    private int mPostId;
    private int mCommentCount;
    private int mZanCount;
    private boolean mZan;

    public CommunityItemChangedEvent(int postId, int commentCount, int zanCount, boolean zan) {
        this.mPostId = postId;
        this.mCommentCount = commentCount;
        this.mZanCount = zanCount;
        this.mZan = zan;
    }

    public int getPostId() {
        return mPostId;
    }

    public void setPostId(int mPostId) {
        this.mPostId = mPostId;
    }

    public int getCommentCount() {
        return mCommentCount;
    }

    public void setCommentCount(int mCommentCount) {
        this.mCommentCount = mCommentCount;
    }

    public int getZanCount() {
        return mZanCount;
    }

    public void setZanCount(int mZanCount) {
        this.mZanCount = mZanCount;
    }

    public boolean isZan() {
        return mZan;
    }

    public void setZan(boolean zan) {
        mZan = zan;
    }
}
