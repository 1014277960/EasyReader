package com.wulinpeng.easyreader.homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.wulinpeng.easyreader.bookserver.model.Book
import com.wulinpeng.easyreader.detail.Loading
import com.wulinpeng.easyreader.homepage.viewmodel.HomePageViewModel
import com.wulinpeng.easyreader.search.viewmodel.SearchPageViewModel
import kotlinx.coroutines.launch

/**
 * author：wulinpeng
 * date：2022/3/30 22:26
 * desc:
 */
@Composable
fun SearchPage(vm: SearchPageViewModel) {
    Column(Modifier.background(Color.White)) {
        SearchView(onSearch = vm::search)
        if (vm.isSearching) {
            Text(text = "Searching", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        } else if (!vm.errMsg.isNullOrEmpty()) {
            Text(text = vm.errMsg!!, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        } else {
            LazyColumn(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()) {
                items(vm.books) { book ->
                    BookView(book, Modifier.clickable {
                        vm.openBook(book)
                    })
                }
                if (vm.books.isNotEmpty() && vm.hasMore) {
                    item {
                        LaunchedEffect(Unit) {
                            vm.loadMore()
                        }
                        Text(text = "正在加载更多", modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(), textAlign = TextAlign.Center, fontSize = 20.sp)
                    }
                }
            }
        }
    }
}


@Composable
fun SearchView(onSearch: (name: String) -> Unit) {
    var text by remember { mutableStateOf("") }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {
        TextField(
            value = text,
            onValueChange = {text = it},
            modifier = Modifier.weight(1f),
            placeholder = { Text("搜索书名/作者") },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearch(text)
            }),
        )
        Button(onClick = { onSearch(text)}) {
            Text(text = "搜索")
        }
    }
}

@Composable
fun BookView(book: Book, modifier: Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
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