package com.wulinpeng.easyreader.launcher.task

import android.app.Application
import com.wulinpeng.easyreader.bookserver.BookServer

/**
 * author：wulinpeng
 * date：2021/10/14 17:14
 * desc:
 */
class BookServerTask: InitTask {
    override fun init(application: Application) {
        BookServer.init(application)
    }
}