package com.wulinpeng.easyreader.search.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wulinpeng.easyreader.bookserver.BookServer
import com.wulinpeng.easyreader.bookserver.model.Book
import com.wulinpeng.easyreader.bookserver.model.Category
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * author：wulinpeng
 * date：2021/10/15 13:53
 * desc:
 */
class SearchPageViewModel: ViewModel() {

    private var searchJob: Job? = null

    val books = mutableStateListOf<Book>()

    var _isSearching = mutableStateOf(false)
    val isSearching by _isSearching

    var _errMsg = mutableStateOf<String?>(null)
    val errMsg by _errMsg

    val _bookClickFlow = MutableSharedFlow<Book>(extraBufferCapacity = Int.MAX_VALUE)
    val bookClickFlow = _bookClickFlow.asSharedFlow()

    fun openBook(book: Book) {
        viewModelScope.launch {
            _bookClickFlow.emit(book)
        }
    }

    fun search(content: String) {
        _isSearching.value = true
        _errMsg.value = null
        searchJob?.cancel()
        searchJob = viewModelScope.launch(ErrorHandler()) {
            books.clear()
            // TODO:
            books.addAll(BookServer.searchBook(content, 0, 100).first)
            _isSearching.value = false
        }
    }

    inner class ErrorHandler: CoroutineExceptionHandler {
        override val key = CoroutineExceptionHandler

        override fun handleException(context: CoroutineContext, exception: Throwable) {
            exception.printStackTrace()
            _isSearching.value = false
            _errMsg.value = exception.message
        }
    }
}