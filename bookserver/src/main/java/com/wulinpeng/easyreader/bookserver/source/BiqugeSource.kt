package com.wulinpeng.easyreader.bookserver.source

import androidx.annotation.Keep
import com.wulinpeng.easyreader.bookserver.model.Book
import com.wulinpeng.easyreader.bookserver.model.Category
import com.wulinpeng.easyreader.bookserver.model.Chapter
import com.wulinpeng.easyreader.bookserver.model.Update
import com.wulinpeng.easyreader.network.Network
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * author：wulinpeng
 * date：2021/7/18 22:11
 * desc:
 */

@Keep
private data class CategoryPram(val sortId: Int)
@Keep
private data class BiqugeBook(val url_list: String, val url_img: String, val articlename: String, val author: String, val intro: String)

class BiqugeSource: BookSource {

    private val BASE_URL = "https://www.biquge7.com"
    private val api = Network.createClient("$BASE_URL/").create(Api::class.java)

    override fun sourceName(): String {
        return "笔趣阁"
    }

    // TODO: page count
    override suspend fun searchBook(bookName: String, page: Int, count: Int): Pair<List<Book>, Boolean> {
        return withContext(Dispatchers.IO) {
            val response = api.searchBook(bookName)
            parseBook(response) to false
        }
    }

    private fun parseBook(content: String): List<Book> {
        val list = Jsoup.parse(content).getElementsByClass("so_list bookcase").first().getElementsByClass("box")
        return mutableListOf<Book>().apply {
            list.forEach {
                val bookName = (it.getElementsByClass("bookname").first().getElementsByTag("a").first().childNode(0) as TextNode).text()
                val category = it.getElementsByClass("cat").first()?.text()?.removePrefix("分类：") ?: ""
                val imageUrl = it.getElementsByTag("img").first().attr("src")
                val author = it.getElementsByClass("author").first().text().removePrefix("作者：")
                val desc = it.getElementsByClass("uptime").first().text()
                val bookUrl = "$BASE_URL${it.getElementsByClass("bookname").first().child(0).attr("href")}"
                add(Book(bookName, author, imageUrl, bookUrl, sourceName(), category = category, introduce = desc))
            }
        }
    }

    override suspend fun getBookDetail(book: Book): Book {
        return withContext(Dispatchers.Default) {
            val response = api.urlContent(book.url)
            parseBookDetail(book, response)
        }
    }

    private fun parseBookDetail(book: Book, content: String): Book {
        val element = Jsoup.parse(content)
        val desc = element.getElementsByClass("intro").first().child(0).child(1).text()

        val chapters = mutableListOf<Chapter>()

        val chapterElements = element.getElementsByClass("listmain").first().child(0).children().filter {
            it.tag().name == "dd" && it.className() != "more pc_none"
        }
        chapters.addAll(toChapters(chapterElements))
        element.getElementsByClass("dd_hide")?.first()?.children()?.apply {
            chapters.addAll(toChapters(this.toList()))
        }

        val sorted = chapters.sortedBy {
            it.url.split("/").last().removeSuffix(".html").toInt()
        }

        val lastUpdateTime = element.getElementsByAttributeValue("property", "og:novel:update_time").first().attr("content")
        val lastUpdateChapter = element.getElementsByAttributeValue("property", "og:novel:latest_chapter_name").first().attr("content")

        return book.copy(introduce = desc, chapterList = sorted, lastUpdate = Update(lastUpdateTime, lastUpdateChapter))
    }

    private fun toChapters(elements: List<Element>): List<Chapter> {
        return mutableListOf<Chapter>().apply {
            elements.forEach {
                val title = it.child(0).text()
                val url = "$BASE_URL${it.child(0).attr("href")}"
                add(Chapter(title, url, sourceName()))
            }
        }
    }

    override suspend fun fillChapterContent(chapter: Chapter) {
        withContext(Dispatchers.Default) {
            val response = api.urlContent(chapter.url)
            chapter.content = parseChapterContent(response)
        }
    }

    override suspend fun getCategorys(): List<Category> {
        return withContext(Dispatchers.IO) {
            val response = api.urlContent(BASE_URL)
            parseCategory(response)
        }
    }

    private fun parseCategory(content: String): List<Category> {
        val element = Jsoup.parse(content)
        val list = element.getElementsByClass("nav").first().getElementsByTag("a")
        val size = list.size
        return list.subList(1, size - 1).mapIndexed { index, element ->
            Category(element.text(), sourceName(), customParam = CategoryPram(index + 1))
        }
    }

    override suspend fun getCategoryBook(
        category: Category,
        count: Int,
        page: Int
    ): Pair<List<Book>, Boolean> {
        return withContext(Dispatchers.IO) {
            val list = api.getCategoryBooks((category.customParam as CategoryPram).sortId, page).map {
                Book(it.articlename, it.author, it.url_img, "$BASE_URL${it.url_list}", sourceName())
            }
            list to !list.isEmpty()
        }
    }

    override suspend fun getRankInfos(): List<Category> {
        TODO("Not yet implemented")
    }

    override suspend fun getRankInfoBook(
        category: Category,
        count: Int,
        page: Int
    ): Pair<List<Book>, Boolean> {
        TODO("Not yet implemented")
    }

    private fun parseChapterContent(response: String): List<String> {

        val element = Jsoup.parse(response).getElementById("chaptercontent")
        element.getElementsByClass("readinline")?.remove()
        return element.toString().removePrefix("<div id=\"chaptercontent\" class=\"Readarea ReadAjax_content\">")
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
        @GET("/s")
        suspend fun searchBook(@Query("q")bookName: String): String

        @GET("/json")
        suspend fun getCategoryBooks(@Query("sortid")id: Int, @Query("page")page: Int): List<BiqugeBook>

        @GET
        suspend fun urlContent(@Url url: String): String
    }
}