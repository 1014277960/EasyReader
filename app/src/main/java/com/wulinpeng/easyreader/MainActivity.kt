package com.wulinpeng.easyreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.wulinpeng.easyreader.bookserver.BookServer
import com.wulinpeng.easyreader.detail.BookDetailActivity
import com.wulinpeng.easyreader.homepage.HomePage
import com.wulinpeng.easyreader.homepage.viewmodel.HomePageViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.immediate + ErrorHandler()

    private val vm by lazy { ViewModelProvider(this).get(HomePageViewModel::class.java) }

    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomePage(vm) {
                vm.isSearching = true
                vm.errMsg = null
                searchJob?.cancel()
                searchJob = launch {
                    vm.books.clear()
                    vm.books.addAll(BookServer.searchBook(it))
                    vm.isSearching = false
                }
            }
        }

        launch {
            vm.bookClickFlow.collect {
                BookDetailActivity.start(this@MainActivity, it)
            }
        }
    }

    inner class ErrorHandler: CoroutineExceptionHandler {
        override val key = CoroutineExceptionHandler

        override fun handleException(context: CoroutineContext, exception: Throwable) {
            exception.printStackTrace()
            vm.isSearching = false
            vm.errMsg = exception.message
        }
    }
}