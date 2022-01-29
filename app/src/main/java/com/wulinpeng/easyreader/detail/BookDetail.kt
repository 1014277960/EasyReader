package com.wulinpeng.easyreader.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.wulinpeng.easyreader.bookserver.model.Book
import com.wulinpeng.easyreader.detail.viewmodel.BookDetailViewModel

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
fun Loading() {
    Text(text = "书籍信息加载中", modifier = Modifier.fillMaxWidth().fillMaxHeight(), textAlign = TextAlign.Center, fontSize = 20.sp)
}