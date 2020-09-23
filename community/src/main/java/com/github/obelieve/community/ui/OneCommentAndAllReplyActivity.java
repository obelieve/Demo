package com.github.obelieve.community.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.github.obelieve.utils.AppDataUtil;
import com.github.obelieve.community.R;
import com.github.obelieve.event.community.CommentReplyNumNotifyEvent;
import com.zxy.frame.base.ApiBaseActivity;
import com.zxy.frame.utils.StatusBarUtil;
import com.zxy.frame.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * 帖子的一条评论及评论的全部回复详情页面
 */
public class OneCommentAndAllReplyActivity extends ApiBaseActivity {

    public static String POST_ID = "post_id";
    public static String COMMENT_ID = "comment_id";

    @BindView(R.id.fl_container)
    FrameLayout fl_container;

    //帖子id
    int mPostId;
    //评论id
    int mCommentId;

    private int mReplyNum;

    @Override
    protected int layoutId() {
        return R.layout.activity_one_comment_and_all_reply;
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        setNeedNavigate();
        setActivityTheme();
        setMyTitle("全部回复");
        mPostId = getIntent().getIntExtra(OneCommentAndAllReplyActivity.POST_ID, 0);
        mCommentId = getIntent().getIntExtra(OneCommentAndAllReplyActivity.COMMENT_ID, 0);
        if (mPostId == 0 || mCommentId == 0) {
            ToastUtil.show("评论不存在");
            finish();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_container, OneCommentAndAllReplyFragment.getInstance(mPostId, mCommentId), OneCommentAndAllReplyFragment.class.getSimpleName())
                .commit();
        EventBus.getDefault().register(this);
    }

    private void setActivityTheme() {
        getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        if (mLightStatusBar) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 设置状态栏底色白色
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().setStatusBarColor(getColor(R.color.common_transparent_black));
                // 设置状态栏字体黑色
                StatusBarUtil.setWindowLightStatusBar(mActivity,true);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOneCommentAndAllReplyNotifyEvent(CommentReplyNumNotifyEvent event) {
        if (event.isReplyRemoveNotify()) {
            mReplyNum = mReplyNum - 1;
        } else {
            mReplyNum = event.getAllReplyNum();
        }
        setMyTitle("全部回复" + AppDataUtil.convertZanComment(mReplyNum));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
