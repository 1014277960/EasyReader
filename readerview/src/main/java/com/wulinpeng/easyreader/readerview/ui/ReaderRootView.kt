package com.wulinpeng.easyreader.readerview.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.wulinpeng.easyreader.readerview.viewmodel.ReaderViewModel

/**
 * author：wulinpeng
 * date：2022/3/22 11:54
 * desc:
 */
@Composable
fun ReaderRootView(vm: ReaderViewModel) {
    val chapter = vm.book.chapterList!![vm.startChapter]
    Column {
        Text("${vm.startChapter}")
        Text("${chapter.title}")
    }
}