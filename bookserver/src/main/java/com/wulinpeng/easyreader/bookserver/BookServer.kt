package com.wulinpeng.easyreader.bookserver

import android.app.Application
import com.wulinpeng.easyreader.bookserver.model.Book
import com.wulinpeng.easyreader.bookserver.model.Chapter
import com.wulinpeng.easyreader.bookserver.source.BiqugeBizSource
import com.wulinpeng.easyreader.bookserver.source.BiqugeSource
import com.wulinpeng.easyreader.bookserver.source.BookSourceManager
import kotlinx.coroutines.awaitAll

/**
 * author：wulinpeng
 * date：2021/7/18 21:16
 * desc:
 */
object BookServer {

    fun init(application: Application) {
        BookSourceManager
            .addBookSource(BiqugeSource())
        // TODO: search error
//            .addBookSource(BiqugeBizSource())
    }

    suspend fun searchBook(bookName: String): List<Book> {
        val deferreds = BookSourceManager.getAllSources().map {
            it.searchBook(bookName)
        }
        return deferreds.awaitAll().flatMap {
            it
        }
    }

    /**
     * 查询数据详细信息，包括章节、介绍等
     */
    suspend fun getBookDetail(book: Book): Book {
        return BookSourceManager.findSource(book.source)?.getBookDetail(book) ?: book
    }

    /**
     * 查询章节内容Content填充到Chapter中
     */
    suspend fun fillChapterContent(chapter: Chapter, forceUpdate: Boolean = false) {
        if (!forceUpdate && chapter.content?.isNotEmpty() == true) {
            return
        }
        BookSourceManager.findSource(chapter.source)?.fillChapterContent(chapter)
    }
}