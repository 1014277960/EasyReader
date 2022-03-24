package com.wulinpeng.easyreader.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Divider
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.wulinpeng.easyreader.bookserver.model.Book
import com.wulinpeng.easyreader.bookserver.model.Chapter
import com.wulinpeng.easyreader.detail.viewmodel.BookDetailViewModel
import kotlinx.coroutines.launch

/**
 * author：wulinpeng
 * date：2021/10/15 17:01
 * desc:
 */
@Composable
fun BookDetailView(vm: BookDetailViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val book = vm.book.value
        if (book != null) {
            Header(book!!)
            Desc(book!!)
            ChapterList(
                Modifier
                    .weight(1f)
                    .fillMaxWidth(), book!!, vm)
            BottomView(vm)
        } else if (vm.errorMsg.value != null) {
            Button(onClick = { vm.refresh() }) {
                Text(text = "重试")
            }
            Text(text = vm.errorMsg.value!!)
        } else {
            Loading()
        }
    }
}

@Composable
private fun Header(book: Book) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(painter = rememberImagePainter(book.image,
            builder = { crossfade(true) }), contentDescription = "",
            modifier = Modifier.size(128.dp))
        Column(
            Modifier
                .weight(1f)
                .padding(10.dp)) {
            Text(text = book.name, fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth())
            book.category.takeIf {
                it.isNotEmpty()
            }?.apply {
                Text(text = "分类：${book.category}", fontSize = 15.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 5.dp, 0.dp, 0.dp))
            }

            Text(text = "作者：${book.author}", fontSize = 15.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 5.dp, 0.dp, 0.dp))

            book.lastUpdate?.apply {
                Text(text = "更新时间：${time}", fontSize = 15.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 5.dp, 0.dp, 0.dp))

                Text(text = "最新章节：${title}", fontSize = 15.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 5.dp, 0.dp, 0.dp))
            }

        }
    }
    Divider(color = Color.Black, modifier = Modifier.fillMaxWidth())
}

@Composable
private fun Desc(book: Book) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)) {
        Text(text = "简介", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = "${book.introduce}", fontSize = 15.sp)
    }
}

@Composable
fun ChapterList(modifier: Modifier, book: Book, vm: BookDetailViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val chapters = book.chapterList!!
    LazyColumn(modifier = modifier) {
        items(chapters) { chapter ->
            ChapterView(chapter) {
                coroutineScope.launch {
                    vm.chapterClickFlow.emit(chapters.indexOf(chapter))
                }
            }
        }
    }
}

@Composable
fun ChapterView(chapter: Chapter, onClick: () -> Unit) {
    Button(colors = buttonColors(backgroundColor = Color.White), onClick = { onClick() }) {
        Text(text = chapter.title, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun BottomView(vm: BookDetailViewModel) {
    val coroutineScope = rememberCoroutineScope()
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(Color.Transparent)
        .padding(0.dp, 0.dp, 0.dp, 12.dp)) {
        Button(colors = buttonColors(backgroundColor = Color.White),
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
                .padding(10.dp, 0.dp, 5.dp, 0.dp), onClick = { /*TODO*/ }) {
            Text(text = "加入书架", textAlign = TextAlign.Center, fontSize = 20.sp, color = Color.Black)
        }

        Button(colors = buttonColors(backgroundColor = Color.Red),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
                .padding(5.dp, 0.dp, 10.dp, 0.dp), onClick = {
                    coroutineScope.launch {
                        vm.startReadFlow.emit(Unit)
                    }
                }) {
            Text(text = "立即阅读", textAlign = TextAlign.Center, fontSize = 20.sp, color = Color.White)
        }
    }
}

@Composable
fun Loading() {
    Text(text = "书籍信息加载中", modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(), textAlign = TextAlign.Center, fontSize = 20.sp)
}