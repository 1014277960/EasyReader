package com.wulinpeng.easyreader.launcher

import android.app.Application
import android.content.Context
import com.wulinpeng.easyreader.appcontext.AppContextManager
import com.wulinpeng.easyreader.launcher.task.BookServerTask
import com.wulinpeng.easyreader.launcher.task.InitNetworkTask

/**
 * author：wulinpeng
 * date：2021/7/18 21:01
 * desc:
 */
class HostApplication: Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        AppContextManager.init(this)
    }

    override fun onCreate() {
        super.onCreate()
        InitNetworkTask().init(this)
        BookServerTask().init(this)
    }
}