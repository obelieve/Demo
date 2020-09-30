package com.github.obelieve.repository.cache;

import android.text.TextUtils;
import android.util.SparseArray;

import com.github.obelieve.login.entity.UserEntity;
import com.github.obelieve.repository.CacheRepository;
import com.github.obelieve.repository.bean.PostCommentCacheBean;
import com.github.obelieve.repository.cache.constant.PreferenceConst;
import com.zxy.frame.net.gson.MGson;
import com.zxy.frame.utils.storage.SPUtil;


public class GlobalCacheUtil {

    private static final GlobalCacheUtil sGlobalCacheUtil = new GlobalCacheUtil();
    private PostCommentCacheBean mPostCommentCacheBean;

    private GlobalCacheUtil() {
    }

    public static GlobalCacheUtil getInstance() {
        return sGlobalCacheUtil;
    }



    private PostCommentCacheBean getPostCommentCacheBean() {
        if (mPostCommentCacheBean == null) {
            String json = SPUtil.getInstance().getString( PreferenceConst.SP_POST_COMMENT_CACHE);
            if (!TextUtils.isEmpty(json)) {
                try {
                    mPostCommentCacheBean = MGson.newGson().fromJson(json, PostCommentCacheBean.class);
                    if (mPostCommentCacheBean != null) {
                        UserEntity entity = CacheRepository.getInstance().getUserEntity();
                        if (!(entity != null && mPostCommentCacheBean.getUserId() == entity.user_id)) {
                            mPostCommentCacheBean = null;
                            SPUtil.getInstance().getString( PreferenceConst.SP_POST_COMMENT_CACHE, "");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    SPUtil.getInstance().getString( PreferenceConst.SP_POST_COMMENT_CACHE, "");
                }
            }
            if (mPostCommentCacheBean == null) {
                mPostCommentCacheBean = new PostCommentCacheBean();
                mPostCommentCacheBean.setPostCommentArray(new SparseArray<>());
                mPostCommentCacheBean.setPostCommentArray(new SparseArray<>());
                mPostCommentCacheBean.setPostCommentReply2ReplyArray(new SparseArray<>());
            }
        }
        return mPostCommentCacheBean;
    }


    public void clearPostCommentCacheBean() {
        mPostCommentCacheBean = null;
        SPUtil.getInstance().putString(PreferenceConst.SP_POST_COMMENT_CACHE, "");
    }

    public String getPostComment(int postId) {
        PostCommentCacheBean bean = getPostCommentCacheBean();
        SparseArray<String> array = bean.getPostCommentArray();
        if (array != null) {
            return array.get(postId);
        }
        return null;
    }

    public void setPostComment(int postId, String text) {
        PostCommentCacheBean bean = getPostCommentCacheBean();
        SparseArray<String> array = bean.getPostCommentArray();
        if (array == null) {
            array = new SparseArray<>();
        }
        array.put(postId, text);
        bean.setPostCommentArray(array);
    }

    public String getPostCommentReply(int postId, int commentId) {
        PostCommentCacheBean bean = getPostCommentCacheBean();
        SparseArray<SparseArray<String>> postArray = bean.getPostCommentReplyArray();
        if (postArray != null) {
            SparseArray<String> commentReplyArray = postArray.get(postId);
            if (commentReplyArray != null) {
                return commentReplyArray.get(commentId);
            }
        }
        return null;
    }

    public void setPostCommentReply(int postId, int commentId, String text) {
        PostCommentCacheBean bean = getPostCommentCacheBean();
        SparseArray<SparseArray<String>> postArray = bean.getPostCommentReplyArray();
        if (postArray == null) {
            postArray = new SparseArray<>();
        }
        SparseArray<String> commentReplyArray = postArray.get(postId);
        if (commentReplyArray == null) {
            commentReplyArray = new SparseArray<>();
        }
        commentReplyArray.put(commentId, text);
        postArray.put(postId, commentReplyArray);
        bean.setPostCommentReplyArray(postArray);
    }

    public String getPostCommentReply2Reply(int postId, int commentId, int replyId) {
        PostCommentCacheBean bean = getPostCommentCacheBean();
        SparseArray<SparseArray<SparseArray<String>>> postArray = bean.getPostCommentReply2ReplyArray();
        if (postArray != null) {
            SparseArray<SparseArray<String>> commentReplyArray = postArray.get(postId);
            if (commentReplyArray != null) {
                SparseArray<String> replyArray = commentReplyArray.get(commentId);
                if (replyArray != null) {
                    return replyArray.get(replyId);
                }
            }
        }
        return null;
    }

    public void setPostCommentReply2Reply(int postId, int commentId, int replyId, String text) {
        PostCommentCacheBean bean = getPostCommentCacheBean();
        SparseArray<SparseArray<SparseArray<String>>> postArray = bean.getPostCommentReply2ReplyArray();
        if (postArray == null) {
            postArray = new SparseArray<>();
        }
        SparseArray<SparseArray<String>> commentReplyArray = postArray.get(postId);
        if (commentReplyArray == null) {
            commentReplyArray = new SparseArray<>();
        }
        SparseArray<String> replyArray = commentReplyArray.get(commentId);
        if (replyArray == null) {
            replyArray = new SparseArray<>();
        }
        replyArray.put(replyId, text);
        commentReplyArray.put(commentId, replyArray);
        postArray.put(postId, commentReplyArray);
        bean.setPostCommentReply2ReplyArray(postArray);
    }
}
