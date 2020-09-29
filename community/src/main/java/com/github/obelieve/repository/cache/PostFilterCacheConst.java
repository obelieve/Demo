package com.github.obelieve.repository.cache;

import android.text.TextUtils;

import com.github.obelieve.community.bean.PostFilterCache;
import com.github.obelieve.repository.CacheRepository;
import com.github.obelieve.repository.cache.constant.PreferenceConst;
import com.zxy.frame.net.gson.MGson;
import com.zxy.frame.utils.SPUtil;

import java.util.HashSet;
import java.util.Set;

public class PostFilterCacheConst {

    private static PostFilterCacheConst sPostFilterCacheConst = new PostFilterCacheConst();

    private Set<Integer> mPostIds = new HashSet<>();
    private Set<Integer> mUserIds = new HashSet<>();

    public static PostFilterCacheConst getInstance() {
        return sPostFilterCacheConst;
    }

    private PostFilterCacheConst() {
        String json = SPUtil.getInstance().getString( PreferenceConst.SP_POST_FILTER_CACHE);
        if (!TextUtils.isEmpty(json)) {
            try {
                PostFilterCache filterCache = MGson.newGson().fromJson(json, PostFilterCache.class);

                if (CacheRepository.getInstance().getUserEntity() != null &&
                        CacheRepository.getInstance().getUserEntity().user_id != 0 &&
                        CacheRepository.getInstance().getUserEntity().user_id == filterCache.getCurUserId()) {
                    if (filterCache.getPostIds() != null && filterCache.getPostIds().size() > 0) {
                        mPostIds.addAll(filterCache.getPostIds());
                    }
                    if (filterCache.getUserIds() != null && filterCache.getUserIds().size() > 0) {
                        mUserIds.addAll(filterCache.getUserIds());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void clear() {
        SPUtil.getInstance().putString(PreferenceConst.SP_POST_FILTER_CACHE, "");
        mPostIds.clear();
        mUserIds.clear();
    }

    public void save() {
        if ((mPostIds.size() > 0 || mUserIds.size() > 0) &&
                CacheRepository.getInstance().getUserEntity() != null &&
                CacheRepository.getInstance().getUserEntity().user_id != 0) {
            PostFilterCache filterCache = new PostFilterCache();
            filterCache.setPostIds(mPostIds);
            filterCache.setUserIds(mUserIds);
            filterCache.setCurUserId(CacheRepository.getInstance().getUserEntity().user_id);
            SPUtil.getInstance().putString(PreferenceConst.SP_POST_FILTER_CACHE, MGson.newGson().toJson(filterCache));
        }
    }


    public Set<Integer> getPostIds() {
        return mPostIds;
    }

    public void addPostId(int postId) {
        mPostIds.add(postId);
    }

    public Set<Integer> getUserIds() {
        return mUserIds;
    }

    public void addUserId(int userId) {
        mUserIds.add(userId);
    }


}
