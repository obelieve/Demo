package com.github.obelieve.repository.cache;

/**
 * Created by Admin
 * on 2020/9/3
 */
public interface IPostCommentCacheBean {

    void setPostComment(int postId, String text);

    String getPostComment(int postId);

    void setPostCommentReply(int postId, int commentId, String text);

    String getPostCommentReply(int postId, int commentId);

    void setPostCommentReply2Reply(int postId, int commentId, int replyId, String text);

    String getPostCommentReply2Reply(int postId, int commentId, int replyId);

    void clearPostCommentCacheBean();
}
