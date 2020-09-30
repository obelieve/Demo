package com.github.obelieve.net;

import android.text.TextUtils;

import com.github.obelieve.community.BuildConfig;
import com.github.obelieve.repository.cache.constant.PreferenceConst;
import com.zxy.frame.utils.storage.SPUtil;

/**
 * Created by TQ on 2018/1/9.
 */

public class UrlConst {

    /**
     * 基础url
     */
    //public static String BASE_URL = "http://192.168.0.162:9981/";
//    public static String BASE_URL = "http://api.test.2048.com/";
    public static String BASE_URL = BuildConfig.BASE_URL;

    static {
        if (BuildConfig.DEBUG) {
            String url = SPUtil.getInstance().getString(PreferenceConst.SP_BASE_URL);
            if (!TextUtils.isEmpty(url)) {
                BASE_URL = url;
            }
        }
    }

    //验证码
    public static final String SMS_URL = "/api/send_sms";
    //登录注册
    public static final String USER_LOGIN_URL = "/api/reg_or_log";
    //获取网易imtoken
    public static String DO_IMTOKEN = "/api/do_token";
    //底部导航接口
    public static String URL_GET_TAB = "/api/get_tab";

    //第三方登录注册
    public static final String USER_THIRD_LOGIN_URL = "/api/thrid_reg_or_log";
    //第三方绑定手机
    public static final String USER_BIND_PHONE_URL = "/api/bind_phone";
    //获取用户信息
    public static final String USER_INFO_URL = "/api/get_user_info";
    //完善信息
    public static final String USER_UPDATE_INFO_URL = "/api/perfect_per_info";
    //注销
    public static final String USER_LOGINOUT_URL = "/api/logout";

    //注销账户获取个人信息
    public static String USER_CANCEL_APPLY = "/api/user_cancel_apply";

    //注销账户
    public static final String USER_CANCEL = "/api/user_cancel";

    //token认证
    public static String REFRESH_OKEN_URL = "/api/refresh";
    //    //获取个人中心导航菜单(弃用)
//    public static String CENTER_MENU_URL = "/api/user_center_menu";
    //提示公告
    public static String NOTICE_INDEX_URL = "/api/get_notice_index";
    //公告信息列表
    public static String NOTICE_LIST_URL = "/api/get_notice_list";
    //公告详情
    public static String NOTICE_DETAIL_URL = "/api/get_notice_detail";
    //消息列表
    public static String MESSAGE_LIST_URL = "/api/get_message_list";
    //消息详情
    public static String MESSAGE_DETAIL_URL = "/api/get_message_detail";

    //赛事日期
    public static String MATCH_DATE_URL = "/api/get_match_date";
    //赛事比赛列表 - 关注
    public static String FOLLOW_MATCH_LIST_URL = "/api/get_follow_match_list";
    //赛事比赛列表 - 即时、赛程、赛事
    public static String MATCH_LIST_URL = "/api/get_match_list";
    //开启\关闭赛事提醒
    public static String MATCH_PUSH_URL = "/api/put_user_match_push";
    //是否开启直播
    public static String MATCH_LIVE_SWICH_URL = "/api/is_match_live";
    //获取赛事筛选列表
    public static String MATCH_FILTER_URL = "/api/get_competition_filter_list";
    //获取赛事直播
    public static String MATCH_LIVE_URL = "/api/get_match_live";
    //获取赛事详情
    public static String MATCH_INFO_URL = "/api/get_match_info";
    //获取赛事阵容
    public static String MATCH_LINEUP_URL = "/api/get_match_lineup";
    //获取赛事分析
    public static String MATCH_ANALYSIS_URL = "/api/get_match_analysis";
    //获取赛事实时数据
    public static String MATCH_REAL_DATA_URL = "/api/get_match_realtime";

    //获取球队基础信息
    public static String TEAM_DETAIL_URL = "/api/get_team_detail";
    //获取球队荣誉
    public static String TEAM_HONOR_URL = "/api/get_team_honor";
    //获取球队队员
    public static String TEAM_PLAYER_URL = "/api/get_team_player";
    //获取球队赛程
    public static String TEAM_MATCH_URL = "/api/get_team_match";
    //获取球员资料
    public static String PLAYER_DETAIL_URL = "/api/get_player_detail";

    //获取赛事及赛季导航
    public static String SEASON_MENU_URL = "/api/get_season_menu";
    //获取积分榜-联赛
    public static String SEASON_TABLE_URL = "/api/get_season_table_yy";
    //获取积分榜-杯赛
    public static String SEASON_TABLE_BS_URL = "/api/get_season_table_bs";
    //获取球员榜\球队榜导航接口
    public static String STATS_MENU_URL = "/api/get_stats_menu";
    //获取球员榜
    public static String STATS_PLAYER_URL = "/api/get_stats_player";
    //获取球队榜
    public static String STATS_TEAM_URL = "/api/get_stats_team";
    //获取赛程阶段轮次数据(杯赛、联赛)
    public static String COMPETITION_STAGE_URL = "/api/competition_stage";
    //获取球赛轮次比赛数据(杯赛、联赛)
    public static String STAGE_MATCH_URL = "/api/stage_match";

    //关注列表及主队
    public static String FOLLOW_HOME_LIST_URL = "/api/get_follow_home_list";
    //设置球队是否开启比赛提醒
    public static String TEAM_PUSH_SET_URL = "/api/set_team_is_push";
    //赛事及可关注球队列表
    public static String COMPETITION_LIST_URL = "/api/get_competition_team_list";
    //关注\取消关注球队
    public static String FOLLOW_STORE_URL = "/api/store_follow_team";
    //关注的球队
    public static String FOLLOW_LIST_URL = "/api/get_follow_team";
    //设置\取消主队
    public static String HIME_SET_URL = "/api/set_home_team";
    //反馈类型
    public static String FEEDBACK_TYPE_URL = "/api/get_feedback_type";
    //意见反馈
    public static String FEEDBACK_URL = "/api/put_feedback";
    //意见反馈列表
    public static String FEEDBACK_LIST_URL = "/api/get_feedback";
    //版本更新
    public static final String VERSION_CHECK_URL = "/api/version_check";
    //获取对应url
    public static final String HTML_URL = "/api/get_html_url";
    public static final String HTML_CONTENT_URL = "/api/get_html_content";

    //图片上传
    public static String UPLOAD_IMAGE_URL = "/api/upload_image";
    //图片url前缀
    public static String IMAGE_URL = "http://image.2048.com/";
    //七牛上传凭证接口
    public static final String QINIU_TOKEN_URL = "/api/upload_token";
    //政策隐私
//    public static String PRIVACY_URL = BASE_URL + "h5/copyright.html";
    public static String PRIVACY_URL = "file:///android_asset/copyright.html";
    //社区公约
    public static String PUBLIC_TREATY = BASE_URL + "h5/bbs.html";

    public static String ABOUT_URL = BASE_URL + "h5/about.html";
    //等级说明
    public static String LEVEL_URL = BASE_URL + "static/level.html?mode=light";

    //---------社区
    //社区广场列表接口（搜索共用）
    public static final String SQUARE_POST = "api/square_post";
    //帖子详情
    public static final String POST_DETAIL = "api/get_post";
    //帖子评论
    public static final String COMMENT_LIST = "/api/get_comment_list";
    //精选帖子
    public static final String GOOD_POST = "/api/good_post";
    //发布帖子
    public static final String SEND_POST = "/api/send_post";
    //发表评论
    public static final String SEND_COMMENT = "/api/send_post_comment";

    //举报帖子\评论\回复接口
    public static final String REPORT_POST = "/api/report_post";
    //删除帖子\评论\回复接口
    public static final String DEL_POST = "/api/del_post";
    //获取举报类型接口
    public static final String GET_REPORT_TYPE = "/api/get_report_type";

    //==================我的消息
    //我的消息消息未读数接口
    public static String POST_MSG_UN_READ = "api/msg_un_read";
    //一键已读接口
    public static String POST_SET_READ = "api/set_read";
    //获取我的消息下 评论详情接口
    public static final String POST_GET_COMMENT = "api/get_comment";
    //点赞（取消）帖子\评论\回复接口
    public static final String POST_ZAN_POST = "api/zan_post";
    //获取我的消息下 消息列表接口
    public static String POST_MSG_LIST = "api/msg_list";
    //获取与指定用户的私信列表接口
    public static String POST_MSG_WITH_USER = "api/msg_with_user";
    //获取我的消息下 评论消息列表接口
    public static String POST_PCMSG_LIST = "/api/pc_msg";
    //获取我的消息下 点赞消息列表接口
    public static String POST_ZANMSG_LIST = "/api/zan_msg";
    //评论下回复列表接口
    public static final String GET_REPLY_LIST = "/api/get_reply_list";

    //个人主页
    //个人主页基础信息接口
    public static final String BBS_USER_INFO = "/api/bbs_user_info";
    //个人主页动态列表接口
    public static final String BBS_USER_TRENDS = "/api/bbs_user_trends";
    //个人主页点赞列表接口
    public static final String BBS_USER_ZAN = "/api/bbs_user_zan";

    //v1.2.0 赛事 改版
    //获取球类列表接口
    public static final String GET_BALL_LIST = "/api/get_ball_list";
    //获取赛事筛选接口
    public static final String GET_FILTER_COM_LIST = "/api/get_filter_com_list";
    //获取比赛列表接口-日期
    public static final String MATCH_LIST = "/api/match_list";
    //获取比赛列表接口-关注
    public static final String FOLLOW_MATCH_LIST = "/api/follow_match_list";
    //比赛详情页面TAB列表接口
    public static final String MATCH_TAB = "/api/match_tab";
    //比赛详情接口
    public static final String MATCH_DETAIL = "/api/match_detail";
    //获取比赛各公司初盘\即盘指数列表接口
    public static final String MATCH_OBBS_LIST = "/api/match_obbs_list";
    //获取比赛盘口公司列表接口
    public static final String MATCH_COMP_LIST = "/api/match_comp_list";
    //获取比赛指定公司指数列表接口
    public static final String MATCH_COMP_OBBS = "/api/match_comp_obbs";
    //聊天室举报接口
    public static final String IM_REPORT = "/api/im_report";
    //比赛过滤列表
    public static final String COMPETITION_FILTER_LIST = "https://image.2048.com/competition_filter_list.json";
    //log收集
    public static final String ERROR_LOG = "/api/error_log";
    //篮球赛事相关接口
    //篮球赛事筛选接口
    public static final String BB_COMPETITION_FILTER_LIST = "https://image.2048.com/bb_competition_filter_list.json";
    //获取篮球比赛列表接口-日期
    public static final String BB_MATCH_LIST = "/api/bb_match_list";
    //获取篮球比赛列表接口-关注
    public static final String BB_FOLLOW_LIST = "/api/bb_follow_list";
    //开启\关闭篮球赛事提醒接口
    public static final String BB_MATCH_PUSH = "/api/bb_match_push";
    //篮球比赛详细信息接口
    public static final String BB_MATCH_DETAIL = "/api/bb_match_detail";
    //篮球比赛详情页面统计接口
    public static final String BB_MATCH_REALTIME = "/api/bb_match_realtime";
    //篮球比赛详情页面分析接口
    public static final String BB_MATCH_ANALYSIS = "/api/bb_match_analysis";
    //获取篮球比赛各公司初盘\即盘指数列表接口
    public static final String BB_MATCH_ODDS = "/api/bb_match_odds";
    //获取篮球比赛盘口公司列表接口
    public static final String BB_MATCH_COMP_LIST = "/api/bb_match_comp_list";
    //获取篮球比赛指定公司指数列表接口
    public static final String BB_MATCH_COMP_OBBS = "/api/bb_match_comp_obbs";
    //文字直播
    public static final String BB_MATCH_TLIVE = "/api/bb_match_tlive";
    //账户设置信息接口
    public static final String USER_SET = "/api/user_set";
    //更新账户设置信息接口
    public static final String UPD_USER_SET = "/api/upd_user_set";

    //解除第三方账号绑定（微信、QQ）
    public static final String UN_BIND_OPEN_ID = "/api/un_bind_openid";
    //解除手机号码绑定
    public static final String UN_BIND_MOBILE = "/api/un_bind_mobile";
    //手机号码绑定
    public static final String BIND_MOBILE = "/api/bind_mobile";
    //绑定第三方账号（微信、QQ）
    public static final String BIND_OPEN_ID = "/api/bind_openid";
    /**
     * 彩经版本
     */
    //获取用户个人信息接口（彩经版本）
    public static final String USER_NAV_INFO = "/api/user_nav_info";
    //获取个人中心导航菜单接口（彩经版本）
    public static final String CENTER_MENU = "/api/center_menu_n";
    //推荐方案模块
    /**
     * 足球-盈利
     */
    public static final String FOOTBALL_GAIN_LIST = "/api/football_gain_list";
    /**
     * 足球-连红
     */
    public static final String FOOTBALL_COMBO_LIST = "/api/football_combo_list";
    /**
     * 足球-命中
     */
    public static final String FOOTBALL_HIT_LIST = "/api/football_hit_list";
    /**
     * 足球-免费
     */
    public static final String FOOTBALL_FREE_LIST = "/api/football_free_list";
    /**
     * 篮球-连红
     */
    public static final String BASKETBALL_COMBO_LIST = "/api/basketball_combo_list";
    /**
     * 篮球-命中
     */
    public static final String BASKETBALL_HIT_LIST = "/api/basketball_hit_list";
    /**
     * 篮球-免费
     */
    public static final String BASKETBALL_FREE_LIST = "/api/basketball_free_list";
    //方案购买相关
    /**
     * 购买方案询问余额接口
     */
    public static final String BUY_PLAN_INQUIRE = "/api/buy_plan_inquire";
    /**
     * 购买方案接口
     */
    public static final String BUY_PLAN = "/api/buy_plan";
    /*充值购买协议*/
    public static final String BUY_PROTOCOL_URL = BASE_URL + "static/buy.html";
    //关注
    /**
     * 关注操作
     */
    public static final String RECOMMEND_FOLLOW = "/api/follow/follow";
    /**
     * 取消关注操作
     */
    public static final String RECOMMEND_FOLLOW_CANCEL = "/api/follow/cancel";
    /**
     * 关注 - 专家列表
     */
    public static final String RECOMMEND_FOLLOW_LIST = "/api/follow/follow_list";
    /**
     * 关注 - 方案列表 - 篮球
     */
    public static final String RECOMMEND_FOLLOW_BASKETBALL_PLAN_LIST = "/api/follow/basketball_plan_list";
    /**
     * 关注 - 方案列表 - 足球
     */
    public static final String RECOMMEND_FOLLOW_FOOTBALL_PLAN_LIST = "/api/follow/football_plan_list";
    //历史方案 - 足球
    public static final String RECOMMEND_SPECIALIST_FOOTBALL_PLAN_HISTORY = "/api/football_plan_history";
    //历史方案 - 篮球
    public static final String RECOMMEND_SPECIALIST_BASKETBALL_PLAN_HISTORY = "/api/basketball_plan_history";
    //专家主页
    public static final String RECOMMEND_SPECIALIST_PLAN_USER = "/api/plan_user_index";
    //比赛详情- 方案列表 - 足球
    public static final String RECOMMEND_FB_MATCH_PLAN_LIST = "/api/football_match_plan_list";
    //比赛详情- 方案列表 - 篮球
    public static final String RECOMMEND_BB_MATCH_PLAN_LIST = "/api/basketball_match_plan_list";
    //已购方案
    public static final String RECOMMEND_USER_BUY_PLAN = "/api/user_buy_plan";
    //方案详情接口
    public static final String RECOMMEND_PLAN_GOODS = "/api/plan_goods";
    /**
     * 充值
     */
    //红钻充值页面列表接口
    public static final String RECHARGE_LIST = "/api/recharge_list";
    //生成充值订单 - 安卓
    public static final String ANDROID_MARK_RECHARGE = "/api/android_make_recharge";
    //关注tab计数值
    public static final String FOLLOW_TAB_CNT = "/api/follow/tab_cnt";
    //聊天室热度记录接口
    public static final String CHATROOM_LOG = "/api/chatroom_log";
    /**
     * V1.7
     */
    //数据模块 - 赛事基础信息接口
    public static final String COMPETITION_BASE = "/api/competition_base";
    //赛事类型导航_v1.7.0
    public static final String MATCH_MENU = "/api/match_menu";

    /*资讯*/
    //发现 - banner列表
    public static final String BANNER_LIST = "/api/banner_list";
    //发现 - 赛事导航
    @Deprecated
    public static final String COMPETITION_MENU = "/api/competition_menu";
    //发现 - 频道列表
    public static final String NEWS_CHAN_LIST = "/api/news_chan_list";
    //发现 - 频道详情
    public static final String NEWS_CHAN_DETAIL = "/api/news_chan_detail";
    //发现 - 资讯列表
    public static final String NEWS_LIST = "/api/news_list_ad";
    /*原生 - 资讯详情*/
    //发现 - 资讯详情
    public static final String NEWS_DETAIL = "/api/news_detail";
    //资讯点赞
    public static final String NEWS_ZAN = "/api/news_zan";

    //金币中心
    public static final String COIN_INDEX = "/api/coin_index";
    //金币明细
    public static final String COIN_FLOW = "/api/coin_flow";
    //等级权益 - 每月金币领取
    public static final String LEVEL_MONTH_COIN_DRAW = "/api/level_month_coin_draw";
    //我的等级
    public static final String LEVEL_INDEX = "/api/level_index";
    //成长值明细
    public static final String LEVEL_FLOW = "/api/level_flow";
    //比赛赛况
    public static final String MATCH_LIVE = "/api/match_live";
    //分析
    public static final String GET_MATCH_ANALYSIS_WITH_GOAL = "/api/get_match_analysis_with_goal";

}


