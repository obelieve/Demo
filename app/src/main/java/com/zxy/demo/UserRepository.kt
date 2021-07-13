package com.zxy.demo

@MyCustomScope//@Singleton
class UserRepository  constructor( //@Inject
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
){}