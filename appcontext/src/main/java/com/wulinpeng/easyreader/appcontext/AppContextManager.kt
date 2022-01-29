package com.wulinpeng.easyreader.appcontext

import android.app.Application

/**
 * author：wulinpeng
 * date：2021/7/18 21:18
 * desc:
 */
object AppContextManager {

    lateinit var context: Application

    fun init(application: Application) {
        context = application
    }
}