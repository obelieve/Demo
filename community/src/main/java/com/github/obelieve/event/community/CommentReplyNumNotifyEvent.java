package com.github.obelieve.event.community;

/**
 * 评论-回复数变更
 */
public class CommentReplyNumNotifyEvent {

    private int mAllReplyNum;

    private boolean mReplyRemoveNotify;//回复删除通知

    private CommentReplyNumNotifyEvent() {

    }

    /**
     * 所有回复数更新通知
     *
     * @param allReplyNum
     * @return
     */
    public static CommentReplyNumNotifyEvent getAllReplyNumInstance(int allReplyNum) {
        CommentReplyNumNotifyEvent event = new CommentReplyNumNotifyEvent();
        event.setAllReplyNum(allReplyNum);
        return event;
    }

    /**
     * 回复删除通知
     *
     * @return
     */
    public static CommentReplyNumNotifyEvent getReplyRemoveInstance() {
        CommentReplyNumNotifyEvent event = new CommentReplyNumNotifyEvent();
        event.setReplyRemoveNotify(true);
        return event;
    }

    public void setAllReplyNum(int allReplyNum) {
        mAllReplyNum = allReplyNum;
    }

    public int getAllReplyNum() {
        return mAllReplyNum;
    }

    public boolean isReplyRemoveNotify() {
        return mReplyRemoveNotify;
    }

    public void setReplyRemoveNotify(boolean replyRemoveNotify) {
        mReplyRemoveNotify = replyRemoveNotify;
    }
}
