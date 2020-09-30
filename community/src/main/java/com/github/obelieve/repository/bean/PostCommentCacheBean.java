package com.github.obelieve.repository.bean;

import android.util.SparseArray;

public class PostCommentCacheBean {

    private int mUserId;
    private SparseArray<String> mPostCommentArray;
    private SparseArray<SparseArray<String>> mPostCommentReplyArray;
    private SparseArray<SparseArray<SparseArray<String>>> mPostCommentReply2ReplyArray;

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public SparseArray<String> getPostCommentArray() {
        return mPostCommentArray;
    }

    public void setPostCommentArray(SparseArray<String> postCommentArray) {
        mPostCommentArray = postCommentArray;
    }

    public SparseArray<SparseArray<String>> getPostCommentReplyArray() {
        return mPostCommentReplyArray;
    }

    public void setPostCommentReplyArray(SparseArray<SparseArray<String>> postCommentReplyArray) {
        mPostCommentReplyArray = postCommentReplyArray;
    }

    public SparseArray<SparseArray<SparseArray<String>>> getPostCommentReply2ReplyArray() {
        return mPostCommentReply2ReplyArray;
    }

    public void setPostCommentReply2ReplyArray(SparseArray<SparseArray<SparseArray<String>>> postCommentReply2ReplyArray) {
        mPostCommentReply2ReplyArray = postCommentReply2ReplyArray;
    }
}
