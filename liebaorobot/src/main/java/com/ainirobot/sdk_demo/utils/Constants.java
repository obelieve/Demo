package com.ainirobot.sdk_demo.utils;

/**
 * @author Orion
 * @time 2018/9/11
 */
public class Constants {

    public static final String FRAGMENT_FUNC_LIST = "fragment_func_list";
    public static final String FRAGMENT_SPEECH = "fragment_speech";
    public static final String FRAGMENT_NAVIGATION = "fragment_navigation";
    public static final String FRAGMENT_MAP = "fragment_map";
    public static final String FRAGMENT_MOVE = "fragment_move";
    public static final String FRAGMENT_FACE = "fragment_face";
    public static final String FRAGMENT_TEST = "fragment_test";
    public static final String FRAGMENT_DOC = "fragment_doc";
    public static final String FRAGMENT_WELCOME_SCENE = "fragment_welcome_scene";
    public static final String FRAGMENT_NAVIGATION_SCENE = "fragment_navigation_scene";
    public static final String FRAGMENT_CHARGE = "fragment_charge";
    public static final String FRAGMENT_SYSTEM = "fragment_system";

    //
    public static final int MSG_REQUEST = 100;
    public static final int MSG_HWREPORT = 101;
    public static final int MSG_SUSPEND = 102;
    public static final int MSG_RECOVERY = 103;

    public static final int REQUEST_ID_DEFAULT = 10;

    public static final String BUNDLE_ID = "bundle_id";
    public static final String BUNDLE_REQUEST_TYPE = "bundle_request_type";
    public static final String BUNDLE_REQUEST_TEXT = "bundle_request_text";
    public static final String BUNDLE_REQUEST_PARAM = "bundle_request_param";
    public static final String BUNDLE_CMD_COMMAND = "bundle_cmd_command";
    public static final String BUNDLE_CMD_STATUS = "bundle_cmd_status";

    //message
    public static final String REQUEST_TYPE_SPEECH = "req_speech_wakeup";
    public static final String REQUEST_TYPE_CRUISE = "robot_navigation&cruise";
    public static final String REQUEST_TYPE_GUIDE = "guide&guide";
    public static final String REQUEST_TYPE_WEATHER = "weather&get_weather";
    public static final String REQUEST_TYPE_CHAT = "chat&chat";
    public static final String REQUEST_TYPE_CALENDAR = "calendar&search_calendar";
    public static final String REQUEST_TYPE_TELL_ME_WHY= "tell_me_why&common";
    public static final String REQUEST_TYPE_ASK_SKILL = "chat&ask_skill";
    public static final String REQUEST_TYPE_STOP = "general_command&stop";

    // typew
    /**
     * 语音唤醒
     * New request:  type is:req_speech_wakeup text is:null reqParam = 2
     */
    public static final String REQ_SPEECH_WAKEUP = "req_speech_wakeup";

    //
    public static final int MSG_SPEECH = 110;

    //
    public static final long START_NAVIGATION_TIME_OUT = 10 * 1000;
    public static final double COORDINATE_DEVIATION = 0.5;
    public static final int INVALID = -1;

    /**
     * 场景模式，可以考虑将场景模式与语义进行匹配
     */
    public static class Mode {
        /**
         * 空闲模式
         */
        public static int IDLE_MODE = 0;
        /**
         * 欢迎模式
         */
        public static int WELCOME_MODE = 1;
        /**
         * 导航模式
         */
        public static int LEADING_MODE = 2;
    }

    /**
     * EventBus开启fragment界面
     */
    public class FRAGMENT_TYPE {
        public static final int WELCOME = 1;
        public static final int NAVIGATION = 2;
        public static final int UPDATE_API = 3;
    }
}
