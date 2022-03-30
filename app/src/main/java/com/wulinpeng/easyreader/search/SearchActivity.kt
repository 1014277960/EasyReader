package com.wulinpeng.easyreader.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.wulinpeng.easyreader.bookserver.BookServer
import com.wulinpeng.easyreader.bookserver.model.Book
import com.wulinpeng.easyreader.detail.BookDetailActivity
import com.wulinpeng.easyreader.homepage.SearchPage
import com.wulinpeng.easyreader.search.viewmodel.SearchPageViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * author：wulinpeng
 * date：2022/3/30 23:35
 * desc:
 */
class SearchActivity: AppCompatActivity(), CoroutineScope {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SearchActivity::class.java))
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.immediate

    private val vm by lazy { ViewModelProvider(this).get(SearchPageViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchPage(vm)
        }

        launch {
            vm.bookClickFlow.collect {
                BookDetailActivity.start(this@SearchActivity, it)
            }
        }
    }
}