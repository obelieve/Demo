package com.github.obelieve.repository.cache;

import java.util.Set;

/**
 * Created by Admin
 * on 2020/9/3
 */
public interface IPostFilterBean {
    void clearPostFilter();
    void savePostFilter();
    Set<Integer> getPostIdsFromPostFilter();
    void addPostIdFromPostFilter(int postId);
    Set<Integer> getUserIdsFromPostFilter();
    void addUserIdPostFilter(int userId);
}
