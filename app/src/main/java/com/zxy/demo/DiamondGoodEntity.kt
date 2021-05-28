package com.zxy.demo


data class DiamondGoodEntity(
    val diamond_goods_list: List<DiamondGoods>,
    val instructions: Instructions,
    val user_diamond: String
){

    data class DiamondGoods(
        val diamond_number: String,
        val giving_coins: String,
        val iap_id: String,
        val icon: String,
        val price: String
    ) : DiamondRechargeTag

    data class Instructions(
        val content: String,
        val file_name: String,
        val file_url: String
    ) : DiamondRechargeTag

}

