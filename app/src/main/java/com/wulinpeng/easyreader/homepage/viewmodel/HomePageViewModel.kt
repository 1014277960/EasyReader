package com.wulinpeng.easyreader.homepage.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wulinpeng.easyreader.bookserver.BookServer
import com.wulinpeng.easyreader.bookserver.model.Book
import com.wulinpeng.easyreader.bookserver.model.Category
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * author：wulinpeng
 * date：2021/10/15 13:53
 * desc:
 */
class HomePageViewModel: ViewModel() {

    private val _searchClickFlow = MutableSharedFlow<Unit>(extraBufferCapacity = Int.MAX_VALUE)
    val searchClickFlow = _searchClickFlow.asSharedFlow()

    fun openSearch() {
        viewModelScope.launch {
            _searchClickFlow.emit(Unit)
        }
    }
}