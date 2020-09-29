package com.github.obelieve.net;

import android.app.Activity;
import android.text.TextUtils;

import com.github.obelieve.App;
import com.github.obelieve.community.bean.BBSUserTrendsEntity;
import com.github.obelieve.community.bean.BBSUserZanEntity;
import com.github.obelieve.community.bean.SquareListsEntity;
import com.github.obelieve.login.entity.SendSMSEntity;
import com.github.obelieve.login.entity.UserEntity;
import com.github.obelieve.repository.cache.PostFilterCacheConst;
import com.github.obelieve.repository.cache.constant.SystemValue;
import com.zxy.frame.net.ApiBaseResponse;
import com.zxy.frame.net.ApiBaseSubscribe;
import com.zxy.frame.net.ApiService;
import com.zxy.frame.net.ApiServiceException;
import com.zxy.frame.utils.LogUtil;
import com.zxy.frame.utils.SPUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Admin
 * on 2020/8/24
 */
public class ApiServiceWrapper extends ApiService {

    @Deprecated
    public static void testLogin() {
        String token = SPUtil.getInstance().getString( "Token");
        if (!TextUtils.isEmpty(token)) {
            SystemValue.token = token;
            LogUtil.e("token 缓存=" + SystemValue.token);
            return;
        }
        wrap(App.getServiceInterface().sendSMS("13100002234", "1", null, null, null), SendSMSEntity.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<SendSMSEntity>>() {
                    @Override
                    public void onError(ApiServiceException e) {

                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<SendSMSEntity> response, boolean isProcessed) {
                        wrap(App.getServiceInterface().login("13100002234", "1234", null), UserEntity.class)
                                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<UserEntity>>() {
                                    @Override
                                    public void onError(ApiServiceException e) {
                                    }

                                    @Override
                                    public void onSuccess(ApiBaseResponse<UserEntity> response1, boolean isProcessed) {
                                        SystemValue.token = response1.getEntity().token;
                                        SPUtil.getInstance().putString("Token", SystemValue.token);
                                        LogUtil.e("token 请求=" + SystemValue.token);
                                    }
                                });
                    }
                });
    }

    /**
     * 第三方登录
     *
     * @param third_type
     * @param open_id
     * @param access_token
     * @param subscribe
     */
    public static void thridLogin(String third_type, String open_id, String access_token, ApiBaseSubscribe<ApiBaseResponse<UserEntity>> subscribe) {
        wrap(App.getServiceInterface().thridLogin(third_type, open_id, access_token, SystemValue.pushToken), UserEntity.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<UserEntity>>() {
                    @Override
                    public void onError(ApiServiceException e) {
                        if (subscribe != null) {
                            subscribe.onError(e);
                        }
                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<UserEntity> response, boolean isProcessed) {
                        if (subscribe != null) {
                            subscribe.onSuccess(response, isProcessed);
                        }
                    }
                });
    }

    /**
     * 第三方绑定手机
     *
     * @param username
     * @param code
     * @param open_type
     * @param open_id
     * @param subscribe
     */
    public static void thridBindPhone(String username, String code, String open_type, String open_id, ApiBaseSubscribe<ApiBaseResponse<UserEntity>> subscribe) {
        wrap(App.getServiceInterface().thridBindPhone(username, code, open_type, open_id, SystemValue.pushToken), UserEntity.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<UserEntity>>() {
                    @Override
                    public void onError(ApiServiceException e) {
                        if (subscribe != null) {
                            subscribe.onError(e);
                        }
                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<UserEntity> response, boolean isProcessed) {
                        if (subscribe != null) {
                            subscribe.onSuccess(response, isProcessed);
                        }
                    }
                });
    }

    /**
     * 社区广场列表
     *
     * @param activity
     * @param page
     * @param keyword
     * @param subscribe
     */
    public static void squarePost(Activity activity, int page, String keyword, ApiBaseSubscribe<ApiBaseResponse<SquareListsEntity>> subscribe) {
        wrap(App.getServiceInterface().squarePost(page, keyword), SquareListsEntity.class).subscribe(new ApiBaseSubscribe<ApiBaseResponse<SquareListsEntity>>(activity) {
            @Override
            public void onError(ApiServiceException e) {
                if (subscribe != null) {
                    subscribe.onError(e);
                }
            }

            @Override
            public void onSuccess(ApiBaseResponse<SquareListsEntity> response, boolean isProcessed) {
                fromCacheFilterPost(response.getEntity());
                if (subscribe != null) {
                    subscribe.onNext(response);
                }
            }

            private void fromCacheFilterPost(SquareListsEntity entity) {
                List<SquareListsEntity.PostListBean> beans = entity.getPost_list();
                Set<SquareListsEntity.PostListBean> deleteBeans = new HashSet<>();
                if (beans != null) {
                    for (SquareListsEntity.PostListBean bean : beans) {
                        if (bean != null) {
                            boolean isContinue = false;
                            for (Integer userId : PostFilterCacheConst.getInstance().getUserIds()) {
                                if (userId == bean.getUser_id()) {
                                    deleteBeans.add(bean);
                                    isContinue = true;
                                    break;
                                }
                            }
                            if (isContinue) {
                                continue;
                            }
                            for (Integer postId : PostFilterCacheConst.getInstance().getPostIds()) {
                                if (postId == bean.getPost_id()) {
                                    deleteBeans.add(bean);
                                    break;
                                }
                            }
                        }
                    }
                    for (SquareListsEntity.PostListBean bean : deleteBeans) {
                        beans.remove(bean);
                    }
                }
            }
        });
    }

    /**
     * 社区精选列表
     *
     * @param activity
     * @param page
     * @param subscribe
     */
    public static void goodPost(Activity activity, int page, ApiBaseSubscribe<ApiBaseResponse<SquareListsEntity>> subscribe) {
        wrap(App.getServiceInterface().goodPost(page), SquareListsEntity.class).subscribe(new ApiBaseSubscribe<ApiBaseResponse<SquareListsEntity>>(activity) {
            @Override
            public void onError(ApiServiceException e) {
                if (subscribe != null) {
                    subscribe.onError(e);
                }
            }

            @Override
            public void onSuccess(ApiBaseResponse<SquareListsEntity> response, boolean isProcessed) {
                fromCacheFilterPost(response.getEntity());
                if (subscribe != null) {
                    subscribe.onNext(response);
                }
            }

            private void fromCacheFilterPost(SquareListsEntity entity) {
                List<SquareListsEntity.PostListBean> beans = entity.getPost_list();
                Set<SquareListsEntity.PostListBean> deleteBeans = new HashSet<>();
                if (beans != null) {
                    for (SquareListsEntity.PostListBean bean : beans) {
                        if (bean != null) {
                            boolean isContinue = false;
                            for (Integer userId : PostFilterCacheConst.getInstance().getUserIds()) {
                                if (userId == bean.getUser_id()) {
                                    deleteBeans.add(bean);
                                    isContinue = true;
                                    break;
                                }
                            }
                            if (isContinue) {
                                continue;
                            }
                            for (Integer postId : PostFilterCacheConst.getInstance().getPostIds()) {
                                if (postId == bean.getPost_id()) {
                                    deleteBeans.add(bean);
                                    break;
                                }
                            }
                        }
                    }
                    for (SquareListsEntity.PostListBean bean : deleteBeans) {
                        beans.remove(bean);
                    }
                }
            }
        });
    }

    /**
     * 错误日志
     *
     * @param activity
     * @param log
     */
    public static void errorLog(Activity activity, String log) {
        wrap(App.getServiceInterface().errorLog(log), String.class).subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>(activity) {
            @Override
            public void onError(ApiServiceException e) {

            }

            @Override
            public void onSuccess(ApiBaseResponse<String> response, boolean isProcessed) {

            }
        });
    }

    /**
     * 发布帖子
     *
     * @param content
     * @param media
     * @param media_scale
     */
    public static void sendPost(String content, String media, String media_scale, ApiBaseSubscribe<ApiBaseResponse<String>> subscribe) {
        wrap(App.getServiceInterface().sendPost(content, media, "image", media_scale), String.class)
                .subscribe(subscribe);
    }

    /**
     * 个人主页动态列表接口
     *
     * @param activity
     * @param userId
     * @param page
     * @param subscribe
     */
    public static void bbsUserTrends(Activity activity, int userId, int page, ApiBaseSubscribe<ApiBaseResponse<BBSUserTrendsEntity>> subscribe) {
        wrap(App.getServiceInterface().bbsUserTrends(userId, page), BBSUserTrendsEntity.class).subscribe(new ApiBaseSubscribe<ApiBaseResponse<BBSUserTrendsEntity>>(activity) {
            @Override
            public void onError(ApiServiceException e) {
                if (subscribe != null) {
                    subscribe.onError(e);
                }
            }

            @Override
            public void onSuccess(ApiBaseResponse<BBSUserTrendsEntity> response, boolean isProcessed) {
                fromCacheFilterPost(response.getEntity());
                if (subscribe != null) {
                    subscribe.onNext(response);
                }
            }

            private void fromCacheFilterPost(BBSUserTrendsEntity entity) {
                List<SquareListsEntity.PostListBean> beans = entity.getPost_list();
                Set<SquareListsEntity.PostListBean> deleteBeans = new HashSet<>();
                if (beans != null) {
                    for (SquareListsEntity.PostListBean bean : beans) {
                        if (bean != null) {
                            boolean isContinue = false;
                            for (Integer userId : PostFilterCacheConst.getInstance().getUserIds()) {
                                if (userId == bean.getUser_id()) {
                                    deleteBeans.add(bean);
                                    isContinue = true;
                                    break;
                                }
                            }
                            if (isContinue) {
                                continue;
                            }
                            for (Integer postId : PostFilterCacheConst.getInstance().getPostIds()) {
                                if (postId == bean.getPost_id()) {
                                    deleteBeans.add(bean);
                                    break;
                                }
                            }
                        }
                    }
                    for (SquareListsEntity.PostListBean bean : deleteBeans) {
                        beans.remove(bean);
                    }
                }
            }
        });
    }

    /**
     * 个人主页点赞列表接口
     *
     * @param activity
     * @param userId
     * @param page
     * @param subscribe
     */
    public static void bbsUserZan(Activity activity, int userId, int page, ApiBaseSubscribe<ApiBaseResponse<BBSUserZanEntity>> subscribe) {
        wrap(App.getServiceInterface().bbsUserZan(userId, page), BBSUserZanEntity.class).subscribe(new ApiBaseSubscribe<ApiBaseResponse<BBSUserZanEntity>>(activity) {
            @Override
            public void onError(ApiServiceException e) {
                if (subscribe != null) {
                    subscribe.onError(e);
                }
            }

            @Override
            public void onSuccess(ApiBaseResponse<BBSUserZanEntity> response, boolean isProcessed) {
                fromCacheFilterPost(response.getEntity());
                if (subscribe != null) {
                    subscribe.onNext(response);
                }
            }

            private void fromCacheFilterPost(BBSUserZanEntity entity) {
                List<SquareListsEntity.PostListBean> beans = entity.getPost_list();
                Set<SquareListsEntity.PostListBean> deleteBeans = new HashSet<>();
                if (beans != null) {
                    for (SquareListsEntity.PostListBean bean : beans) {
                        if (bean != null) {
                            boolean isContinue = false;
                            for (Integer userId : PostFilterCacheConst.getInstance().getUserIds()) {
                                if (userId == bean.getUser_id()) {
                                    deleteBeans.add(bean);
                                    isContinue = true;
                                    break;
                                }
                            }
                            if (isContinue) {
                                continue;
                            }
                            for (Integer postId : PostFilterCacheConst.getInstance().getPostIds()) {
                                if (postId == bean.getPost_id()) {
                                    deleteBeans.add(bean);
                                    break;
                                }
                            }
                        }
                    }
                    for (SquareListsEntity.PostListBean bean : deleteBeans) {
                        beans.remove(bean);
                    }
                }
            }
        });
    }

    /**
     * 更新用户信息
     *
     * @param activity
     * @param nickname
     * @param sex
     * @param city
     * @param province
     * @param birthday
     * @param avatar
     * @param subscriber
     */
    public static void updateUserInfo(Activity activity, String nickname, String sex, String city, String province, String birthday, String avatar, ApiBaseSubscribe<ApiBaseResponse<String>> subscriber) {
        String pCity;
        if (TextUtils.isEmpty(province) || TextUtils.isEmpty(city)) {
            pCity = province + city;
        } else {
            pCity = province + "," + city;// 省份城市（中文） 逗号隔开
        }
        wrap(App.getServiceInterface().updateUserInfo(nickname, sex, pCity, birthday, avatar), String.class).subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>(activity) {

            @Override
            public void onError(ApiServiceException e) {
                if (subscriber != null) {
                    subscriber.onError(e);
                }
            }

            @Override
            public void onSuccess(ApiBaseResponse<String> response, boolean isProcessed) {
                if (subscriber != null) {
                    subscriber.onNext(response);
                }
            }
        });
    }
}
