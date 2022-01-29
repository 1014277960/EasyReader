package com.wulinpeng.easyreader.bookserver.source

import com.wulinpeng.easyreader.bookserver.model.Book
import com.wulinpeng.easyreader.bookserver.model.Chapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * author：wulinpeng
 * date：2021/7/18 22:02
 * desc:
 */
interface BookSource: CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default
    fun sourceName(): String
    suspend fun searchBook(bookName: String): Deferred<List<Book>>

    suspend fun getBookDetail(book: Book): Book?
    suspend fun fillChapterContent(chapter: Chapter)
}