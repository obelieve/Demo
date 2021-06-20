package com.zxy.demo

/**
 * Created by zxy
 * on 2020/11/20
 */
interface AppPageRouter {
    companion object {
        const val WEB_VIEW = "/common/webview"
        const val SPLASH_AD = "/main/splashAd"
        const val MAIN = "/main/main"
        const val SMS = "/auth/sms"
        const val LOGIN = "/auth/login"
        const val REGISTER = "/auth/register"

        const val ABOUT = "/more/about"
        const val SETTING = "/more/settings"
        const val PUSH_SETTINGS = "/more/push_settings"
        const val PUSH_SETTINGS_LIST = "/more/push_settings/list"
        const val PERSONAL_INFOMATION = "/more/personal_information"
        const val EDIT_NICK = "/more/edit_nick"
        const val EDIT_MAILBOX = "/more/edit_mailbox"
        const val MY_TAK = "/more/my_task"
        const val LANGUAGE_SETTING = "/more/lan_setting"
        const val FEEDBACK = "/more/feed_back"
        const val COIN_DETAIL_LIST = "/more/coin_list_detail"
        const val SHOPPING_ADDRESS = "/more/shopping_address"
        const val MY_ORDERS = "/more/my_orders"
        const val MY_ADDRESS = "/more/address"
        const val ORDER_INFORMATION = "/more/order_information"
        const val EXCHANGE_SUCC = "/more/exchange_succ"

        const val MESSAGE = "/home/message"
        const val MESSAGE_DETAIL = "/home/message/detail"


        const val MATCHES_FILTER = "/matches/filter"
        //比赛详情
        const val MATCHES_DETAIL = "/matches/detail"
        const val MATCHES_DETAIL_ODDS_LIST = "/matches/detail/odds_list"

        const val NEWS_DETAIL = "/news/detail"
        const val NEWS_SETTINGS = "/news/settings"
        const val TOPIC_DETAIL = "/news/topicDetail"
        const val TOPIC_LIST = "/news/topicList"

        const val FAVORITE_ADD = "/favorite/add"
        const val FAVORITE_SEARCH = "/favorite/search"
        const val FAVORITE_TEAM_DETAIL = "/favorite/team/detail"
        const val FAVORITE_PLAYER_DETAIL = "/favorite/player/detail"
        const val FAVORITE_COMPETITION_DETAIL = "/favorite/competition/detail"

        const val PREDICT_RECORD = "/predict/record"
        const val PREDICT_LEADER_BOARD = "/predict/leaderboard"
        /**1.2.1**/
        const val VIDEO_PUBLISHER = "/video/videoPublisher"
        const val VIDEO_LIST = "/video/list"
        /**1.3.0**/
        const val COINS_STORE_LIST = "/coins_store/list"
        const val COINS_STORE_DETAIL = "/coins_store/detail"
    }
}
