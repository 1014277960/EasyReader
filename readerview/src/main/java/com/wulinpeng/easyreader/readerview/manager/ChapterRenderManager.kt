package com.wulinpeng.easyreader.readerview.manager

import android.graphics.Canvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.wulinpeng.easyreader.bookserver.model.Book

/**
 * author：wulinpeng
 * date：2022/3/22 23:01
 * desc: 管理章节数据并负责章节页面UI的各项计算
 */

data class ChapterRenderConfig(val fontSize: TextUnit, val lineSpace: Dp)
data class Page(val lines: List<String>)

class ChapterRenderManager {

    private lateinit var config: ChapterRenderConfig
    private lateinit var chapterLoadManger: ChapterLoadManger
    private var currentChapter = 0
    private var currentPage = 0
    private val chapter2Page = mutableMapOf<Int, List<Page>>()

    suspend fun init(book: Book, config: ChapterRenderConfig, startChapter: Int) {
        this.config = config
        chapterLoadManger = ChapterLoadManger(book.chapterList!!)
        currentChapter = startChapter
    }

    fun draw(canvas: Canvas) {
    }

    suspend fun configChange(config: ChapterRenderConfig) {
        this.config = config
        TODO()
    }


}