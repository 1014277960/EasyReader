package com.wulinpeng.easyreader.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.wulinpeng.easyreader.bookserver.model.Book
import com.wulinpeng.easyreader.bookserver.model.getBookDetail
import com.wulinpeng.easyreader.detail.viewmodel.BookDetailViewModel
import com.wulinpeng.easyreader.readerview.ReaderActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BookDetailActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.immediate + ErrorHandler()

    companion object {

        private const val KEY_BOOK = "key_book"

        fun start(context: Context, book: Book) {
            context.startActivity(Intent(context, BookDetailActivity::class.java).apply {
                putExtra(KEY_BOOK, book)
            })
        }
    }

    private val vm by lazy { ViewModelProvider(this).get(BookDetailViewModel::class.java)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val book = intent.getSerializableExtra(KEY_BOOK) as? Book
        if (book == null) {
            finish()
            return
        }

        setContent {
            BookDetailView(vm)
        }

        launch {
            vm.book.value = book.getBookDetail()
            vm.errorMsg.value = null
        }

        launch {
            vm.chapterClickFlow.collect {
                ReaderActivity.start(this@BookDetailActivity, vm.book.value!!, it)
            }
        }
        launch {
            vm.startReadFlow.collect {
                ReaderActivity.start(this@BookDetailActivity, vm.book.value!!)
            }
        }
        launch(Dispatchers.Main) {
            vm.refreshFlow.collect {
                try {
                    vm.book.value = book.getBookDetail()
                } catch (t: Throwable) {
                    vm.errorMsg.value = t.message
                }
            }
        }

    }

    inner class ErrorHandler: CoroutineExceptionHandler {
        override val key = CoroutineExceptionHandler

        override fun handleException(context: CoroutineContext, exception: Throwable) {
            exception.printStackTrace()
            vm.errorMsg.value = exception.message
        }
    }
}