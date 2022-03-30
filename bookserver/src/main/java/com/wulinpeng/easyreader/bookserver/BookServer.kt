package com.wulinpeng.easyreader.bookserver

import android.app.Application
import com.wulinpeng.easyreader.bookserver.model.Book
import com.wulinpeng.easyreader.bookserver.model.Category
import com.wulinpeng.easyreader.bookserver.model.Chapter
import com.wulinpeng.easyreader.bookserver.source.BiqugeSource
import com.wulinpeng.easyreader.bookserver.source.BookSource
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
    }

    suspend fun searchBook(bookName: String, page: Int, count: Int): Pair<List<Book>, Boolean> {
        return BookSourceManager.currentSource().searchBook(bookName, count, page)
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

    suspend fun getCategorys(): List<Category> {
        return BookSourceManager.currentSource().getCategorys()
    }

    suspend fun getCategoryBook(category: Category, count: Int, page: Int): Pair<List<Book>, Boolean> {
        return BookSourceManager.findSource(category.sourceName)!!.getCategoryBook(category, count, page)
    }
}