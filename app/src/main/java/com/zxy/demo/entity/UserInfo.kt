package com.zxy.demo.entity


data class UserInfo(
    val avatar: String,
    val coins: Int,
    val email: String,
    val global_roaming: String,
    val locale: List<Locale>,
    val mobile: String,
    val nickname: String ,
    val diamonds : String ,
    val join_day : String ,
    val team_badge_url:String
){
    data class Locale(
        val text: String,
        val value: String
    )
}


