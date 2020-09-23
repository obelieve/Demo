package com.news.captchalib;


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
