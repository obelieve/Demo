package com.zxy.demo.captcha.swipe;

/**
 * 拼图
 * 1.滚动条；2.刷新（0位置 ，图片随机 ，可触发 ），3.图片
 *
 * 2.触摸方式
 *
 * 状态：
 * 0.初始化：参数初始化
 *
 * 1.开始：处于0位置
 * 2.滑动：
 * 	//验证提示View
 * 	//超过次数提示回调
 * 3.松开：->验证成功，（滚动条不能触发，图片完整）
 * 	->验证失败，（滚动条可触发并复位，记录错误次数,成功清零 //TODO）
 *
 * 初始化：
 * 参数初始化
 *
 * 开始:
 * 1.滚动条：-> 0 ，可滚动
 * 2.图片： 滑块和阴影、（位置记录）
 *
 * 移动状态：
 * 1.滚动条：移动
 * 2.图片：滑块移动，阴影固定
 *
 * 结束状态：
 * 1.滚动条：成功（不能移动）、失败（复位，可移动）
 * 2.图片：成功（滑块和阴影重叠）、失败（分离显示）
 */
public class SwipeCaptchaHelper {

    public static final int MAX_FAILED_COUNT = 5;

    /**
     * 状态
     */
    public enum Status {
        /**
         * 参数初始化
         */
        INIT,
        /**
         * 开始
         */
        START,
        /**
         * 移动
         */
        MOVE,
        /**
         * 结束
         */
        END,
        ;
    }

    /**
     * 状态回调
     */
    public interface StatusCallback {

        void onInit();

        void onStart();

        void onMove();

        void onEnd();
    }

    /**
     * 结束回调
     */
    public interface EndCallback {

        void onSuccess();

        void onFailure();

        void onMaxFailed();
    }

}
