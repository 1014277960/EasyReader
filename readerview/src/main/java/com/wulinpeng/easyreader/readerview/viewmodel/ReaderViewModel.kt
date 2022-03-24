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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * author：wulinpeng
 * date：2022/3/22 11:52
 * desc:
 */
class ReaderViewModel: ViewModel() {

    private val errorHandler = ErrorHandler()

    lateinit var book: Book
    var startChapter: Int = 0
    var currentChapter by mutableStateOf<Chapter?>(null)
    var errorMsg by mutableStateOf<String?>(null)

    private fun loadChapter(index: Int) {
        errorMsg = null
        val chapter = book.chapterList!![index]!!
        if (chapter.content != null) {
            currentChapter = chapter
            return
        } else {
            currentChapter = null
        }
        viewModelScope.launch(errorHandler) {
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

    private inner class ErrorHandler(override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler) : CoroutineExceptionHandler {
        override fun handleException(context: CoroutineContext, exception: Throwable) {
            errorMsg = exception.message
        }

    }
}