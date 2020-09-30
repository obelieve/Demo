package com.github.obelieve.community.viewmodel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.github.obelieve.App;
import com.github.obelieve.community.R;
import com.github.obelieve.repository.CacheRepository;
import com.github.obelieve.utils.AppDataUtil;
import com.zxy.frame.base.ApiBaseActivity;
import com.zxy.frame.net.ApiBaseResponse;
import com.zxy.frame.net.ApiBaseSubscribe;
import com.zxy.frame.net.ApiService;
import com.zxy.frame.net.ApiServiceException;
import com.zxy.frame.utils.SystemInfoUtil;
import com.zxy.frame.utils.ToastUtil;


/**
 * 发送评论
 */
public class SendCommentModule {

    private Dialog mCommentDialog;

    private Activity mActivity;
    private int mPostId;
    private int mCommentId;
    private VM mVM;

    public SendCommentModule(ApiBaseActivity activity, int postId) {
        mActivity = activity;
        mPostId = postId;
        mVM = ViewModelProviders.of(activity).get(VM.class);
    }

    public VM getVM() {
        return mVM;
    }

    public void setCommentId(int mCommentId) {
        this.mCommentId = mCommentId;
    }

    public Dialog getDialog() {
        return mCommentDialog;
    }

    public void showSendCommentDialog(String hint, String tag) {
        if (mCommentDialog != null) {
            mCommentDialog.dismiss();
        }
        mCommentDialog = new Dialog(mActivity, R.style.BottomDialog);
        mCommentDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (dialog instanceof Dialog) {
                    Dialog d = (Dialog) dialog;
                    if (d.getWindow() != null) {
                        View view = d.getWindow().getDecorView();
                        View clickView = view.findViewById(R.id.btn_ok);
                        if (clickView != null && clickView.getTag() == null) {
                            EditText et = view.findViewById(R.id.edit_comment);
                            if (et != null) {
                                CacheRepository.getInstance().setPostCommentReply(mPostId, mCommentId, et.getText().toString());
                            }
                        }
                    }
                }
            }
        });
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.layout_comment_dialog, null);
        EditText editComment = contentView.findViewById(R.id.edit_comment);
        editComment.setHint(hint);
        editComment.setTag(tag);
        if (tag == null) {
            String tempComment = CacheRepository.getInstance().getPostCommentReply(mPostId, mCommentId);
            if (!TextUtils.isEmpty(tempComment)) {
                editComment.setText(tempComment);
            }
        }
        contentView.findViewById(R.id.btn_ok).setOnClickListener(view1 ->
        {
            view1.setTag(true);
            if (view1.getContext() instanceof Activity) {
                if (AppDataUtil.isAlertShow((Activity) view1.getContext())) {
                    return;
                }
            }
            if (editComment.getTag() == null) {
                mVM.sendCommentReply(mActivity, mPostId, mCommentId, editComment.getText().toString());
            } else {
                try {
                    String[] strings = ((String) editComment.getTag()).split(",");
                    if (strings.length == 3) {
                        mVM.sendCommentReply(mActivity, mPostId, mCommentId, editComment.getText().toString(), Integer.valueOf(strings[0]), strings[1], Integer.valueOf(strings[2]));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.show("回复错误！");
                }
            }
        });
        mCommentDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = mActivity.getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = SystemInfoUtil.dp2px(App.getContext(), 60);
        contentView.setLayoutParams(layoutParams);
        mCommentDialog.getWindow().setGravity(Gravity.BOTTOM);
        mCommentDialog.show();
        editComment.postDelayed(() -> {
            editComment.requestFocus();
            InputMethodManager inputMethodManager =
                    (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }, 50);
    }


    /**
     * 发表评论ViewModel
     */
    public static class VM extends ViewModel {
        //是否加载
        private MutableLiveData<Boolean> showdialog = new MutableLiveData<>();

        //评论发布成功
        private MutableLiveData<Boolean> commentSuccess = new MutableLiveData<>();

        public MutableLiveData<Boolean> getShowdialog() {
            return showdialog;
        }

        public MutableLiveData<Boolean> getCommentSuccess() {
            return commentSuccess;
        }

        /**
         * 评论回复
         *
         * @param post_id
         * @param comment_id
         * @param content
         */
        public void sendCommentReply(Activity activity, int post_id, int comment_id, String content) {
            showdialog.postValue(true);
            ApiService.wrap(App.getServiceInterface().sendPostComment(post_id, content, comment_id), String.class)
                    .subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>(activity) {
                        @Override
                        public void onError(ApiServiceException e) {
                            CacheRepository.getInstance().setPostCommentReply(post_id, comment_id, content);
                            showdialog.postValue(false);
                            ToastUtil.show(e.message);
                        }

                        @Override
                        public void onSuccess(ApiBaseResponse<String> response, boolean isProcessed) {
                            CacheRepository.getInstance().setPostCommentReply(post_id, comment_id, "");
                            showdialog.postValue(false);
                            commentSuccess.postValue(isProcessed);
                        }
                    });
        }

        /**
         * 评论回复
         *
         * @param post_id
         * @param comment_id
         * @param content
         */
        public void sendCommentReply(Activity activity, int post_id, int comment_id, String content, int to_uid, String to_uname, int to_rid) {
            showdialog.postValue(true);
            ApiService.wrap(App.getServiceInterface().sendPostComment(post_id, content, comment_id, to_uid, to_uname, to_rid), String.class)
                    .subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>(activity) {
                        @Override
                        public void onError(ApiServiceException e) {
                            CacheRepository.getInstance().setPostCommentReply2Reply(post_id, comment_id, to_rid, content);
                            showdialog.postValue(false);
                            ToastUtil.show(e.message);
                        }

                        @Override
                        public void onSuccess(ApiBaseResponse<String> response, boolean isProcessed) {
                            CacheRepository.getInstance().setPostCommentReply2Reply(post_id, comment_id, to_rid, "");
                            showdialog.postValue(false);
                            commentSuccess.postValue(isProcessed);
                        }
                    });
        }
    }
}

