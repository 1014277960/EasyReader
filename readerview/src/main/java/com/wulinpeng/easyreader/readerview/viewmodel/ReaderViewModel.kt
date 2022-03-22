package com.wulinpeng.easyreader.readerview.viewmodel

import androidx.lifecycle.ViewModel
import com.wulinpeng.easyreader.bookserver.model.Book

/**
 * author：wulinpeng
 * date：2022/3/22 11:52
 * desc:
 */
class ReaderViewModel: ViewModel() {
    lateinit var book: Book
    var startChapter: Int = 0
}