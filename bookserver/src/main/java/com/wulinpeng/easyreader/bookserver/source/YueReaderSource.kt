package com.wulinpeng.easyreader.bookserver.source

import androidx.annotation.Keep
import com.wulinpeng.easyreader.bookserver.model.Book
import com.wulinpeng.easyreader.bookserver.model.Chapter
import com.wulinpeng.easyreader.bookserver.model.Update
import com.wulinpeng.easyreader.network.Network
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.http.*
import java.lang.RuntimeException
import java.util.*

/**
 * author：wulinpeng
 * date：2022/3/24 22:48
 * desc:
 */
class YueReaderSource: BookSource {

    companion object {
        private const val KEY_BOOK_ID = "KEY_BOOK_ID"
        private const val KEY_CHAPTER_ID = "KEY_CHAPTER_ID"
        private const val KEY_CHAPTER_V = "KEY_CHAPTER_V"
    }

    private val BASE_URL = "http://yuenov.com:15555"
    private val api = Network.createClient("$BASE_URL/").create(Api::class.java)

    override fun sourceName() = "yue-reader"

    override suspend fun searchBook(bookName: String): Deferred<List<Book>> {
        return async {
            val response = api.searchBook(bookName, 1, Int.MAX_VALUE).dealError()
            try {
                response.data.list.map {
                    Book(
                        it.title,
                        it.author,
                        "$BASE_URL${it.coverImg}",
                        "",
                        sourceName(),
                        introduce = it.desc,
                        customParam = mutableMapOf(KEY_BOOK_ID to it.bookId)
                    )
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                emptyList()
            }
        }
    }

    override suspend fun getBookDetail(book: Book): Book? {
        return withContext(Dispatchers.Default) {
            val bookId = book.customParam!![KEY_BOOK_ID] as Int
//            val update = api.bookDetail(bookId).dealError().data.update
            val chapterList = api.bookChapterList(bookId).dealError().data.chapters.map {
                Chapter(title = it.name, url = "", source = sourceName(),
                    customParam = mutableMapOf(KEY_CHAPTER_ID to it.id, KEY_CHAPTER_V to it.v, KEY_BOOK_ID to bookId))
            }
//            book.copy(chapterList = chapterList,
//                lastUpdate = Update(Date(update.time).toLocaleString(), update.chapterName))
            book.copy(chapterList = chapterList)
        }
    }

    override suspend fun fillChapterContent(chapter: Chapter) {
        withContext(Dispatchers.Default) {
            val bookId = chapter.customParam!![KEY_BOOK_ID] as Int
            val chapterId = chapter.customParam!![KEY_CHAPTER_ID] as Long
            val v = chapter.customParam!![KEY_CHAPTER_V] as Int
            val yueBookDetail = api.chapterDetail(bookId, listOf(chapterId), v).dealError().data.list.first()
            // TODO: split
            chapter.content = listOf(yueBookDetail.content)
        }
    }

    private interface Api {
        @GET("/app/open/api/book/search")
        suspend fun searchBook(@Query("keyWord")keyWord: String,
                               @Query("pageNum")pageNum: Int,
                               @Query("pageSize")pageSize: Int): Response<SearchResponse>

        @GET("/app/open/api/book/getDetail")
        suspend fun bookDetail(@Query("bookId")id: Int): Response<YueBookDetail>

        @GET("/app/open/api/chapter/getByBookId")
        suspend fun bookChapterList(@Query("bookId")id: Int): Response<YueBookChapterList>

        @POST("/app/open/api/chapter/get")
        @Headers("Content-Type: application/json")
        @FormUrlEncoded
        suspend fun chapterDetail(@Field("bookId")id: Int, @Field("chapterIdList")chapterIdList: List<Long>, @Field("v")v: Int): Response<YueBookChapterDetailResponse>
    }
}

@Keep
private data class ResultCode(val code: Int, val msg: String)
@Keep
private data class Response<T>(val result: ResultCode, val data: T)
@Keep
private data class YueBook(val author: String,
                           val bookId: Int,
                           val categoryName: String,
                           val chapterStatus: String,
                           val coverImg: String,
                           val desc: String,
                           val title: String,
                           val word: String)
@Keep
private data class YueUpdate(val chapterId: Long, val chapterName: String, val chapterStatus: String, val time: Long)
@Keep
private data class YueBookDetail(val update: YueUpdate)
@Keep
private data class SearchResponse(val list: List<YueBook>)
@Keep
private data class YueBookChapter(val id: Long, val name: String, val v: Int)
@Keep
private data class YueBookChapterList(val chapters: List<YueBookChapter>)
@Keep
private data class YueBookChapterDetail(val id: Long, val name: String, val content: String)
@Keep
private data class YueBookChapterDetailResponse(val list: List<YueBookChapterDetail>)

private fun <T> Response<T>.dealError(): Response<T> {
    if (result.code != 0) {
        throw RuntimeException(result.msg)
    } else {
        return this
    }
}