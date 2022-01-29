package com.wulinpeng.easyreader.bookserver.model

import com.wulinpeng.easyreader.bookserver.BookServer
import java.io.Serializable

/**
 * author：wulinpeng
 * date：2021/10/14 20:34
 * desc:
 */
data class Chapter(val title: String, val url: String, val source: String, var content: List<String>? = null): Serializable

suspend fun Chapter.fillChapterContent(forceUpdate: Boolean = false) {
    BookServer.fillChapterContent(this, forceUpdate)
}