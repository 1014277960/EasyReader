package com.wulinpeng.easyreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.wulinpeng.easyreader.bookserver.BookServer
import com.wulinpeng.easyreader.detail.BookDetailActivity
import com.wulinpeng.easyreader.homepage.HomePage
import com.wulinpeng.easyreader.homepage.viewmodel.HomePageViewModel
import com.wulinpeng.easyreader.search.SearchActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.immediate + ErrorHandler()

    private val vm by lazy { ViewModelProvider(this).get(HomePageViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomePage(vm)
        }

        launch {
            vm.searchClickFlow.collectLatest {
                SearchActivity.start(this@MainActivity)
            }
        }
    }

    inner class ErrorHandler: CoroutineExceptionHandler {
        override val key = CoroutineExceptionHandler

        override fun handleException(context: CoroutineContext, exception: Throwable) {
            exception.printStackTrace()
        }
    }
}