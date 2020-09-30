package com.github.obelieve.event.message;

/**
 * 消息未读数变更 刷新通知
 * Created by Admin
 * on 2020/9/9
 */
public class MessageUnReadNumRefreshEvent {
    private boolean mResumeRefresh;

    public MessageUnReadNumRefreshEvent(boolean resumeRefresh) {
        mResumeRefresh = resumeRefresh;
    }

    public boolean isResumeRefresh() {
        return mResumeRefresh;
    }
}
