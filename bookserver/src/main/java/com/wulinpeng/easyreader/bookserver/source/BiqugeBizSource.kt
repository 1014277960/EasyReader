package com.wulinpeng.easyreader.bookserver.source

import com.wulinpeng.easyreader.bookserver.model.Book
import com.wulinpeng.easyreader.bookserver.model.Chapter
import com.wulinpeng.easyreader.bookserver.model.Update
import com.wulinpeng.easyreader.network.Network
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * author：wulinpeng
 * date：2021/7/18 22:11
 * desc:
 */
class BiqugeBizSource: BookSource {

    private val BASE_URL = "https://www.biquge.biz"
    private val api = Network.createClient("$BASE_URL/").create(Api::class.java)

    override fun sourceName(): String {
        return "笔趣阁.Biz"
    }

    override suspend fun searchBook(bookName: String): Deferred<List<Book>> {
        return async {
            val response = api.searchBook(bookName)
            parseBook(response)
        }
    }

    private fun parseBook(content: String): List<Book> {
        val list = Jsoup.parse(content).getElementsByClass("result-item result-game-item")
        return mutableListOf<Book>().apply {
            list.forEach {
                val infoList = it.getElementsByClass("result-game-item-info").first().children()
                val bookName = it.getElementsByAttribute("title").first().text()
                val category = infoList[1].child(1).text()
                val imageUrl = it.getElementsByTag("img").first().attr("src")
                val author = infoList.first().child(1).text()
                val lastUpdateTime = infoList[2].child(1).text()
                val lastUpdateTitle = infoList[3].child(1).text()
                val bookUrl = "$BASE_URL${it.getElementsByClass("result-item-title result-game-item-title").first().child(0).attr("href")}"
                val desc = it.getElementsByClass("result-game-item-desc").first().text()
                add(Book(bookName, author, imageUrl, bookUrl, sourceName(),
                    category = category, introduce = desc, lastUpdate = Update(lastUpdateTime, lastUpdateTitle)))
            }
        }
    }

    override suspend fun getBookDetail(book: Book): Book? {
        return withContext(Dispatchers.Default) {
            val response = api.urlContent(book.url)
            parseBookDetail(book, response)
        }
    }

    private fun parseBookDetail(book: Book, content: String): Book {
        val chapters = mutableListOf<Chapter>()
        Jsoup.parse(content).getElementById("list").child(0).children().filter { it.tag().name == "dd" }.forEach {
            val title = it.child(0).text()
            val url = "$BASE_URL${it.child(0).attr("href")}"
            chapters.add(Chapter(title, url, sourceName()))
        }
        return book.copy(chapterList = chapters)
    }

    override suspend fun fillChapterContent(chapter: Chapter) {
        withContext(Dispatchers.Default) {
            val response = api.urlContent(chapter.url)
            chapter.content = parseChapterContent(response)
        }
    }

    private fun parseChapterContent(response: String): List<String> {
        return Jsoup.parse(response).getElementById("content").toString()
            .removePrefix("<div id=\"content\">")
            .removeSuffix("</div>")
            .split(Regex("<br>|<br/>|<br />")).map {
                formatLine(it)
            }.filter {
                it.isNotEmpty()
            }
    }

    private fun formatLine(line: String): String {
        if (line.isNullOrEmpty()) {
            return line
        }
        return line.replace(Regex("\n|\t|\r|&nbsp;|<br>|<br/>|<br />|p&gt;|&gt;|&hellip;"), "").trim()
    }

    private interface Api {
        @GET("/search.php")
        suspend fun searchBook(@Query("q")bookName: String): String

        @GET
        suspend fun urlContent(@Url url: String): String
    }
}