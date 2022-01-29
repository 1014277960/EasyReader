package com.wulinpeng.easyreader.homepage.viewmodel

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
class HomePageViewModel: ViewModel() {

    val books = mutableStateListOf<Book>()

    var isSearching by mutableStateOf(false)

    var errMsg by mutableStateOf<String?>(null)

    val bookClickFlow = MutableSharedFlow<Book>(extraBufferCapacity = Int.MAX_VALUE)
}