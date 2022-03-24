package com.wulinpeng.easyreader.readerview.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.wulinpeng.easyreader.bookserver.model.Book
import com.wulinpeng.easyreader.bookserver.model.Chapter
import com.wulinpeng.easyreader.bookserver.model.fillChapterContent
import kotlinx.coroutines.launch

/**
 * author：wulinpeng
 * date：2022/3/22 11:52
 * desc:
 */
class ReaderViewModel: ViewModel() {
    lateinit var book: Book
    var startChapter: Int = 0
    var currentChapter by mutableStateOf<Chapter?>(null)

    private fun loadChapter(index: Int) {
        val chapter = book.chapterList!![index]!!
        if (chapter.content != null) {
            currentChapter = chapter
            return
        } else {
            currentChapter = null
        }
        viewModelScope.launch {
            chapter.fillChapterContent()
            currentChapter = chapter
        }
    }

    fun loadCurrentChapter() {
        loadChapter(startChapter)
    }

    fun loadNextChapter() {
        if (startChapter + 1 > book.chapterList!!.size - 1) {
            return
        }
        startChapter++
        loadCurrentChapter()
    }

    fun loadPreChapter() {
        if (startChapter - 1 < 0) {
            return
        }
        startChapter--
        loadCurrentChapter()
    }

}