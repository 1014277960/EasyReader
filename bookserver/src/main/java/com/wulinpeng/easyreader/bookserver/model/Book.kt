package com.wulinpeng.easyreader.bookserver.model

import com.wulinpeng.easyreader.bookserver.BookServer
import java.io.Serializable


/**
 * author：wulinpeng
 * date：2021/7/18 22:03
 * desc:
 */
data class Book(val name: String,
                val author: String,
                val image: String,
                val url: String,
                val source: String,
                val lastUpdate: Update? = null,
                val introduce: String = "",
                val category: String = "",
                val chapterList: List<Chapter>? = null,
                // 用于业务自己使用
                val customParam: Map<String, Any>? = null): Serializable

data class Update(val time: String, val title: String): Serializable

suspend fun Book.getBookDetail(): Book {
    return BookServer.getBookDetail(this)
}