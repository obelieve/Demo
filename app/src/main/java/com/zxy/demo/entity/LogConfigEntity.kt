package com.zxy.demo.entity

data class LogConfigEntity(
    val accessKey: String,
    val accessKeyId: String,
    val endpoint: String,
    val event_id: List<String>,
    val logstore: String,
    val project: String,
    val token: String
)