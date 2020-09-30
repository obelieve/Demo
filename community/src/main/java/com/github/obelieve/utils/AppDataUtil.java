package com.github.obelieve.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.github.obelieve.App;
import com.github.obelieve.community.R;
import com.github.obelieve.login.entity.UserEntity;
import com.github.obelieve.repository.CacheRepository;
import com.github.obelieve.repository.cache.constant.PreferenceConst;
import com.github.obelieve.repository.cache.constant.SystemValue;
import com.github.obelieve.utils.others.TranslationState;
import com.zxy.frame.application.BaseApplication;
import com.zxy.frame.dialog.CommonDialog;
import com.zxy.frame.utils.storage.SPUtil;
import com.zxy.frame.utils.info.SystemInfoUtil;

/**
 * 应用相关的逻辑调用
 */
public class AppDataUtil {

    /**
     * 价格显示 （0.00~）
     *
     * @param num
     * @return
     */
    public static String convertPrice(String num) {
        return TextUtils.isEmpty(num) || num.equals("0") ? "0.00" : num;
    }

    /**
     * 超过99 显示 99+
     *
     * @param count
     * @return
     */
    public static String convertZanComment(int count) {
        String c;
        final int MAX = 99;
        if (count <= MAX) {
            c = "" + count;
        } else {
            c = MAX + "+";
        }
        return c;
    }

    /**
     * TextView 显示 99+
     *
     * @param tv
     * @param count
     */
    public static void showZanComment(TextView tv, int count) {
        showZanComment(tv, "%s", count);
    }

    /**
     * TextView 显示 99+
     *
     * @param tv
     * @param format string...%s...
     * @param count
     */
    public static void showZanComment(TextView tv, String format, int count) {
        if (format != null) {
            try {
                tv.setText(String.format(format, convertZanComment(count)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 是否显示社区相关弹窗
     *
     * @param activity
     * @return
     */
    public static boolean isAlertShow(Activity activity) {
        if (TextUtils.isEmpty(SystemValue.token)) {
            ActivityUtil.gotoLoginActivity(activity, true);
            return true;
        } else {
            UserEntity userEntity = CacheRepository.getInstance().getUserEntity();
            if (userEntity != null) {
                if (TextUtils.isEmpty(userEntity.nickname)) {
                    CommonDialog dialog = new CommonDialog(activity);
                    dialog.setContent("您还未设置昵称");
                    dialog.setNegativeButton("取消", null);
                    dialog.setPositiveButton("去设置", dialog1 -> {
                        dialog1.dismiss();
                        ActivityUtil.gotoUserInfoActivity(activity, false, true);
                    });
                    dialog.show();
                    return true;
                } else if (TextUtils.isEmpty(userEntity.mobile)) {
                    CommonDialog dialog = new CommonDialog(activity);
                    dialog.setContent("根据《国家网络安全法》规定\n绑定手机号才能发言");
                    dialog.setNegativeButton("再看看", null);
                    dialog.setPositiveButton("去绑定", dialog1 -> {
                        dialog1.dismiss();
//                        LoginTypeActivity.startSettingsBindPhone(activity); //TODO 绑定手机号 跳转
                    });
                    dialog.show();
                    return true;
                } else {
                    return false;
                }

            } else {
                return false;
            }
        }
    }

    /**
     * 社区-文本内容 是否显示“全文”标识（全文/收起）
     *
     * @param content
     * @return
     */
    public static boolean calculateShowCheckAllText(String content) {
        Paint textPaint = new Paint();
        textPaint.setTextSize(SystemInfoUtil.dp2px(App.getContext(), 16));
        float textWidth = textPaint.measureText(content);
        float maxContentViewWidth = SystemInfoUtil.screenWidth(App.getContext()) - SystemInfoUtil.dp2px(App.getContext(), 74);
        float maxLines = textWidth / maxContentViewWidth;
        return maxLines > 4;
    }


    /**
     * 社区-内容长按时，显示弹窗
     *
     * @param context
     * @param onItemClickPopupMenuListener
     * @param position
     * @param view
     * @param translationState
     */
    public static void showPopupMenu(Context context, OnItemClickPopupMenuListener onItemClickPopupMenuListener,
                                     int position, View view, TranslationState translationState) {
//        if (translationState != null) {
//            PopupMenu popup = new PopupMenu(context, view);
//            if (translationState == TranslationState.START) {
//                popup.getMenuInflater().inflate(R.menu.popup_menu_start, popup.getMenu());
//            } else if (translationState == TranslationState.CENTER) {
//                popup.getMenuInflater().inflate(R.menu.popup_menu_center, popup.getMenu());
//            } else if (translationState == TranslationState.END) {
//                popup.getMenuInflater().inflate(R.menu.popup_menu_end, popup.getMenu());
//            }
//            popup.setOnMenuItemClickListener(item -> {
//                switch (item.getItemId()) {
//                    case R.id.copy:
//                        if (onItemClickPopupMenuListener != null) {
//                            onItemClickPopupMenuListener.onItemClickCopy(position);
//                        }
//                        break;
//                    case R.id.collection:
//                        if (onItemClickPopupMenuListener != null) {
//                            onItemClickPopupMenuListener.onItemClickCollection(position);
//                        }
//                        break;
//                    case R.id.hide_translation:
//                        if (onItemClickPopupMenuListener != null) {
//                            onItemClickPopupMenuListener.onItemClickHideTranslation(position);
//                        }
//                        break;
//                    default:
//                        break;
//                }
//                return true;
//            });
//            popup.show(); //showing popup menu
//        }
    }

    /**
     * 显示隐私政策弹窗
     *
     * @param activity
     * @param listener
     */
    public static void showPrivacyDialog(final Activity activity, OnComfirmListener listener) {
        String s = "欢迎使用，请认真阅读《软件许可及隐私政策》，您同意后方可使用。";
        SpannableString spannableString = new SpannableString(s);
        int i = s.indexOf("《软件许可及隐私政策》");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(BaseApplication.getContext().getResources().getColor(R.color.comment_text_blue_color));
        spannableString.setSpan(colorSpan, i, i + "《软件许可及隐私政策》".length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
//                ActivityUtil.gotoWebActivity(mActivity, UrlConst.PRIVACY_URL, getResources().getString(R.string.settings_privacy_policy), false);
            }
        }, i, i + "《软件许可及隐私政策》".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        CommonDialog commonDialog = new CommonDialog(activity);
        commonDialog.setContent(spannableString);
        commonDialog.setNegativeButton("查看详情", new CommonDialog.onCancelListener() {
                    @Override
                    public void onClickCancel(Dialog dialog) {
//                        ActivityUtil.gotoWebActivity(mActivity, UrlConst.PRIVACY_URL, getResources().getString(R.string.settings_privacy_policy), false);
                    }
                }
        );
        commonDialog.setPositiveButton("同意并继续", new CommonDialog.onSubmitListener() {
            @Override
            public void onClickSubmit(Dialog dialog) {
                SPUtil.getInstance().putBoolean(PreferenceConst.KEY_FIRST_ENTER, false);
                dialog.dismiss();
                if (listener != null)
                    listener.onConfirm();
            }
        });
        commonDialog.setCanceledOnTouchOutside(false);
        commonDialog.setCancelable(false);
        commonDialog.show();
    }

    public interface OnComfirmListener {
        void onConfirm();
    }

    public interface OnItemClickPopupMenuListener {

        void onItemClickCopy(int position);

        void onItemClickHideTranslation(int position);

        void onItemClickCollection(int position);
    }
}
