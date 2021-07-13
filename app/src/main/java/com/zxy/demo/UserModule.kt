package com.zxy.demo

import com.obelieve.frame.utils.log.LogUtil
import dagger.Module
import dagger.Provides

@Module
class UserModule {

    @Provides //@Inject
    fun getUserRepository():UserRepository{
        LogUtil.e("Module->getUserRepository()")
        return UserRepository(UserLocalDataSource(),UserRemoteDataSource())
    }
}