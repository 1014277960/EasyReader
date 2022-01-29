package com.wulinpeng.easyreader.launcher.task

import android.app.Application
import com.wulinpeng.easyreader.launcher.task.InitTask
import com.wulinpeng.easyreader.network.Network

/**
 * author：wulinpeng
 * date：2021/7/18 21:04
 * desc:
 */
class InitNetworkTask: InitTask {
    override fun init(application: Application) {
        Network.init()
    }
}