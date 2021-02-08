package com.zxy.demo

import kotlinx.coroutines.*
import java.lang.Exception
import java.lang.NullPointerException

/**
 * launcher 和 async 抛出异常区别：
 * 1.launcher可以直接CoroutineExceptionHandler捕获
 * 2.async需要调用 #await()时才会抛到上一级协程并捕获处理
 */
object CoroutinesTest {

    @JvmStatic
    fun main(arrays: Array<String>) {
        val start = System.currentTimeMillis()
        println("Main")
        runBlocking {
            val job = GlobalScope.launch(handler) {
                val list = listOf(req1(), req2())
                val size = list.size
                val value = list.awaitAll()
                println("time=${System.currentTimeMillis()-start} \nsize=$size, 0=${value[0]}, 1=${value[1]}")
            }
            job.join()
        }
    }

    val handler = CoroutineExceptionHandler { _, exception -> //runBlocking协程没有异常处理
        println("CoroutineExceptionHandler got $exception") //协程未处理异常时，抛出CoroutineExceptionHandler。全局异常处理者就如同 Thread.defaultUncaughtExceptionHandler 一样
    }


    suspend fun req1(): Deferred<EntityA> {
        return GlobalScope.async {
            delay(1000)
            println("req1=${Thread.currentThread()}")
            throw NullPointerException("HH")
            EntityA("AA",21)
        }
    }

    suspend fun req2(): Deferred<EntityB>{
        return GlobalScope.async {
            delay(1000)
            println("req2=${Thread.currentThread()}")
            EntityB("BB")
        }
    }

    data class EntityA(val nameA: String, val age: Int)
    data class EntityB(val nameB: String)
}