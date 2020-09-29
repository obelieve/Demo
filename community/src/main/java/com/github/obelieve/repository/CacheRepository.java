package com.github.obelieve.repository;

import android.app.Activity;
import android.content.Context;

import com.github.obelieve.login.entity.UserEntity;
import com.github.obelieve.repository.bean.TempEditPostBean;
import com.github.obelieve.repository.cache.GlobalCacheUtil;
import com.github.obelieve.repository.cache.IPostCommentCacheBean;
import com.github.obelieve.repository.cache.IPostFilterBean;
import com.github.obelieve.repository.cache.ITempEditPostBean;
import com.github.obelieve.repository.cache.IUserEntity;
import com.github.obelieve.repository.cache.PostFilterCacheConst;
import com.github.obelieve.repository.cache.UserHelper;
import com.github.obelieve.repository.cache.constant.PreferenceConst;
import com.zxy.frame.net.gson.MGson;
import com.zxy.frame.utils.SPUtil;

import java.util.Set;

/**
 * Created by Admin
 * on 2020/9/3
 */
public class CacheRepository implements IPostCommentCacheBean, IUserEntity, ITempEditPostBean , IPostFilterBean {

    private static CacheRepository sCacheRepository = new CacheRepository();

    public static CacheRepository getInstance() {
        return sCacheRepository;
    }

    @Override
    public void setPostComment(int postId, String text) {
        GlobalCacheUtil.getInstance().setPostComment(postId, text);
    }

    @Override
    public String getPostComment(int postId) {
        return GlobalCacheUtil.getInstance().getPostComment(postId);
    }

    @Override
    public void setPostCommentReply(int postId, int commentId, String text) {
        GlobalCacheUtil.getInstance().setPostCommentReply(postId, commentId, text);
    }

    @Override
    public String getPostCommentReply(int postId, int commentId) {
        return GlobalCacheUtil.getInstance().getPostCommentReply(postId, commentId);
    }

    @Override
    public void setPostCommentReply2Reply(int postId, int commentId, int replyId, String text) {
        GlobalCacheUtil.getInstance().setPostCommentReply2Reply(postId, commentId, replyId, text);
    }

    @Override
    public String getPostCommentReply2Reply(int postId, int commentId, int replyId) {
        return GlobalCacheUtil.getInstance().getPostCommentReply2Reply(postId, commentId, replyId);
    }

    @Override
    public void clearPostCommentCacheBean() {
        GlobalCacheUtil.getInstance().clearPostCommentCacheBean();
    }

    @Override
    public void initUserEntity() {
        UserHelper.getInstance().initUserEntity();
    }

    @Override
    public UserEntity getUserEntity() {
        return UserHelper.getInstance().getUserEntity();
    }

    @Override
    public void setUserEntity(String json, UserEntity userEntity, Context context) {
        UserHelper.getInstance().setUserEntity(json, userEntity, context);
    }

    @Override
    public void refreshUserEntity(Activity activity) {
        UserHelper.getInstance().getUserInfo(activity);
    }

    @Override
    public void refreshIMTokenFromUserEntity(String imToken) {
        UserHelper.getInstance().refreshIMToken(imToken);
    }

    @Override
    public void refreshTokenFromUserEntity(String token) {
        UserHelper.getInstance().refreshToken(token);
    }

    @Override
    public void clearUserEntity(Activity activity) {
        UserHelper.getInstance().clearData(activity);
    }

    @Override
    public TempEditPostBean getTempEditPostBean() {
        TempEditPostBean bean = null;
        String tempPost = SPUtil.getInstance().getString( PreferenceConst.SP_TEMP_POST);
        if (tempPost != null && tempPost.length() > 0) {
            try {
                bean = MGson.newGson().fromJson(tempPost, TempEditPostBean.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    @Override
    public void saveTempEditPostBean(TempEditPostBean bean) {
        SPUtil.getInstance().putString(PreferenceConst.SP_TEMP_POST, bean != null ? MGson.newGson().toJson(bean) : "");
    }

    @Override
    public void clearTempEditPostBean() {
        saveTempEditPostBean(null);
    }

    @Override
    public void clearPostFilter() {
        PostFilterCacheConst.getInstance().clear();
    }

    @Override
    public void savePostFilter() {
        PostFilterCacheConst.getInstance().save();
    }

    @Override
    public Set<Integer> getPostIdsFromPostFilter() {
        return PostFilterCacheConst.getInstance().getPostIds();
    }

    @Override
    public void addPostIdFromPostFilter(int postId) {
        PostFilterCacheConst.getInstance().addPostId(postId);
    }

    @Override
    public Set<Integer> getUserIdsFromPostFilter() {
        return PostFilterCacheConst.getInstance().getUserIds();
    }

    @Override
    public void addUserIdPostFilter(int userId) {
        PostFilterCacheConst.getInstance().addPostId(userId);
    }
}
