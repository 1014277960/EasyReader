package com.wulinpeng.easyreader.readerview

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.wulinpeng.easyreader.bookserver.model.Book
import com.wulinpeng.easyreader.bookserver.model.fillChapterContent
import com.wulinpeng.easyreader.readerview.manager.ChapterRenderManager
import com.wulinpeng.easyreader.readerview.ui.ReaderRootView
import com.wulinpeng.easyreader.readerview.viewmodel.ReaderViewModel
import kotlinx.coroutines.launch

class ReaderActivity : AppCompatActivity() {

    companion object {

        private const val KEY_BOOK = "key_book"
        private const val KEY_START_CHAPTER = "key_start_chapter"

        fun start(context: Context, book: Book, startChapter: Int = 0) {
            context.startActivity(Intent(context, ReaderActivity::class.java).apply {
                putExtra(KEY_BOOK, book)
                putExtra(KEY_START_CHAPTER, startChapter)
            })
        }
    }

    private val readerViewModel: ReaderViewModel by lazy {
        ViewModelProvider(this).get(ReaderViewModel::class.java)
    }

    private val renderManager = ChapterRenderManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val book = intent.getSerializableExtra(KEY_BOOK) as? Book
        val startChapter = intent.getIntExtra(KEY_START_CHAPTER, 0)
        if (book == null) {
            finish()
            return
        }
        readerViewModel.book = book
        readerViewModel.startChapter = startChapter
        setContent {
            ReaderRootView(readerViewModel, renderManager)
        }
        readerViewModel.loadCurrentChapter()
    }
}