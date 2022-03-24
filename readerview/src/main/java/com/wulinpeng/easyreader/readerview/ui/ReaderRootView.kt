package com.wulinpeng.easyreader.readerview.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.wulinpeng.easyreader.readerview.manager.ChapterRenderManager
import com.wulinpeng.easyreader.readerview.viewmodel.ReaderViewModel
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * author：wulinpeng
 * date：2022/3/22 11:54
 * desc:
 */
@Composable
fun ReaderRootView(vm: ReaderViewModel, renderManager: ChapterRenderManager) {
    if (vm.currentChapter == null) {
        Text(text = "Loading")
    } else {
        // TODO: 多线程？？
//        LazyColumn {
//            item {
//                Button(onClick = { vm.loadPreChapter() }) {
//                    Text(text = "上一章", textAlign = TextAlign.Center, fontSize = 20.sp, color = Color.Black)
//                }
//            }
//            item {
//                Text(text = "${vm.currentChapter!!.title}", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
//            }
//            try {
//                items(vm.currentChapter!!.content!!) { item ->
//                    Text(text = item)
//                }
//            } catch (t: Throwable) {
//                t.printStackTrace()
//            }
//            item {
//                Button(onClick = { vm.loadNextChapter() }) {
//                    Text(text = "下一章", textAlign = TextAlign.Center, fontSize = 20.sp, color = Color.Black)
//                }
//            }
//        }
        val scrollState = ScrollState(0)
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            Button(onClick = { vm.loadPreChapter() }) {
                Text(text = "上一章", textAlign = TextAlign.Center, fontSize = 20.sp, color = Color.Black)
            }
            Text(text = "${vm.currentChapter!!.title}", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            vm.currentChapter!!.content!!.forEach {
                Text(text = it)
            }
            Button(onClick = { vm.loadNextChapter() }) {
                Text(text = "下一章", textAlign = TextAlign.Center, fontSize = 20.sp, color = Color.Black)
            }
        }
    }
}