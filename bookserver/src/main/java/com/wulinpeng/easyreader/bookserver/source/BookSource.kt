package com.wulinpeng.easyreader.bookserver.source

import com.wulinpeng.easyreader.bookserver.model.Book
import com.wulinpeng.easyreader.bookserver.model.Category
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

    /**
     * 搜索书籍
     * List<Book> -> hasMore
     */
    suspend fun searchBook(bookName: String, page: Int, count: Int): Pair<List<Book>, Boolean>

    /**
     * 获取书籍详情
     */
    suspend fun getBookDetail(book: Book): Book

    /**
     * 获取章节内容
     */
    suspend fun fillChapterContent(chapter: Chapter)

    /**
     * 获取所有分类
     */
    suspend fun getCategorys(): List<Category>

    /**
     * 获取分类书籍
     * List<Book> -> hasMore
     */
    suspend fun getCategoryBook(category: Category, count: Int, page: Int): Pair<List<Book>, Boolean>

    /**
     * 获取所有榜单
     */
    suspend fun getRankInfos(): List<Category>

    /**
     * 获取榜单书籍
     * List<Book> -> hasMore
     */
    suspend fun getRankInfoBook(category: Category, count: Int, page: Int): Pair<List<Book>, Boolean>
}