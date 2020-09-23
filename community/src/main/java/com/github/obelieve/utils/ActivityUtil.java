package com.github.obelieve.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.github.obelieve.bean.VersionUpdateEntity;
import com.github.obelieve.community.ui.OneCommentAndAllReplyActivity;
import com.github.obelieve.community.ui.PersonalPageActivity;
import com.github.obelieve.community.ui.UpdateDetailActivity;
import com.github.obelieve.login.LoginDialogActivity;
import com.github.obelieve.main.MainActivity;
import com.github.obelieve.me.ui.SettingsActivity;
import com.github.obelieve.me.ui.UserInfoActivity;
import com.github.obelieve.repository.cache.constant.SystemValue;
import com.github.obelieve.ui.UpdateSelfActivity;


public class ActivityUtil {

    /**
     * 首页
     *
     * @param context
     */
    public static void gotoMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /*** 登录 ***/

    /**
     * 登录（弹窗样式）
     *
     * @param context
     * @return
     */
    public static boolean gotoLoginActivity(Context context) {
        return gotoLoginActivity(context, true);
    }

    /**
     * 登录
     *
     * @param context
     * @param isDialog 是否登录的弹窗样式
     * @return
     */
    public static boolean gotoLoginActivity(Context context, boolean isDialog) {
        if (TextUtils.isEmpty(SystemValue.token)) {
            if (isDialog) {
                if (context instanceof Activity) {
                    ActivityUtil.gotoLoginDialogActivity((Activity) context);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 登录弹窗
     *
     * @param activity
     */
    public static void gotoLoginDialogActivity(Activity activity) {
        activity.startActivity(new Intent(activity, LoginDialogActivity.class));
    }

    /**
     * 完善个人信息
     *
     * @param context
     */
    public static void gotoUserInfoActivity(Context context, boolean isRegister) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra(UserInfoActivity.EXTRA_FROM_REGISTER, isRegister);
        context.startActivity(intent);
    }

    /**
     * 跳转设置昵称
     *
     * @param context
     */
    public static void gotoUserInfoActivity(Context context, boolean isRegister, boolean isSetTheName) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra(UserInfoActivity.EXTRA_FROM_REGISTER, isRegister);
        intent.putExtra(UserInfoActivity.EXTRA_FROM_SET_THE_NAME, isSetTheName);
        context.startActivity(intent);
    }


    /**
     * 设置页
     *
     * @param context
     */
    public static void gotoSettingsActivity(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }


    /**
     * 打开升级对话框
     *
     * @param context 上下文
     */
    public static void gotoUpdateActivity(Activity context, VersionUpdateEntity entity) {
        Intent intent = new Intent(context, UpdateSelfActivity.class);
        intent.putExtra(UpdateSelfActivity.EXTRA_UPDATE_DATA, entity);
        context.startActivity(intent);
        context.overridePendingTransition(0, 0);
    }


    /**
     * 内置浏览器
     *
     * @param context
     * @param url
     * @param title
     * @param isFromNotify
     */
    public static void gotoWebActivity(Context context, String url, String title, boolean isFromNotify) {
//        Intent intent = new Intent(context, WebViewActivityI.class);
//        intent.putExtra(WebViewActivityI.EXTRA_NAME_URL, url);
//        intent.putExtra(WebViewActivityI.EXTRA_NAME_TITLE, title);
//        intent.putExtra(WebViewActivityI.EXTRA_IS_FORM_NOTIFY, isFromNotify);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
    }

    /*** 社区 ***/

    /**
     * 启动帖子详情
     *
     * @param activity
     * @param postId
     * @param userId
     * @param tag
     */
    public static void gotoUpdateDetailActivity(Context activity, int postId, int userId, String tag) {
        gotoUpdateDetailActivity(activity, postId, userId, 0, tag);
    }

    /**
     * 启动帖子详情
     *
     * @param activity
     * @param postId
     * @param userId
     * @param tag
     */
    public static void gotoUpdateDetailActivity(Context activity, int postId, int userId, int from_cid, String tag) {
        Intent intent = new Intent(activity, UpdateDetailActivity.class);
        intent.putExtra(UpdateDetailActivity.POST_ID, postId);
        intent.putExtra(UpdateDetailActivity.USER_ID, userId);
        intent.putExtra(UpdateDetailActivity.FROM_CID, from_cid);
        intent.putExtra(UpdateDetailActivity.TAG, tag);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /**
     * 个人主页
     *
     * @param context
     * @param userId
     */
    public static void gotoPersonalPageActivity(Context context, int userId) {
        Intent intent = new Intent(context, PersonalPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }


    /**
     * 评论-回复列表
     *
     * @param activity
     * @param postId
     * @param commentId
     */
    public static void gotoOneCommentAndAllReplyActivity(Activity activity, int postId, int commentId) {
        Intent intent = new Intent(activity, OneCommentAndAllReplyActivity.class);
        intent.putExtra(OneCommentAndAllReplyActivity.POST_ID, postId);
        intent.putExtra(OneCommentAndAllReplyActivity.COMMENT_ID, commentId);
        activity.startActivity(intent);
    }

}
