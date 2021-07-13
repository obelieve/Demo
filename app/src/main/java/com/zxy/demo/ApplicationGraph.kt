package com.zxy.demo

import dagger.Component

@MyCustomScope
@Component(modules = [UserModule::class])
interface ApplicationGraph {
    fun repository(): UserRepository
}