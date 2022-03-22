package com.wulinpeng.easyreader.detail.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.wulinpeng.easyreader.bookserver.model.Book
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * author：wulinpeng
 * date：2021/10/15 13:53
 * desc:
 */
class BookDetailViewModel: ViewModel() {

    val book = mutableStateOf<Book?>(null)
    val chapterClickFlow = MutableSharedFlow<Int>(extraBufferCapacity = Int.MAX_VALUE)
    val startReadFlow = MutableSharedFlow<Unit>(extraBufferCapacity = Int.MAX_VALUE)
}