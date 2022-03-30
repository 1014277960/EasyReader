package com.wulinpeng.easyreader.homepage

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.wulinpeng.easyreader.homepage.viewmodel.HomePageViewModel

/**
 * author：wulinpeng
 * date：2022/3/30 22:33
 * desc:
 */
sealed class HomeScreen(val title: String) {

    @Composable
    open fun Screen(vm: HomePageViewModel) {
        Text(text = title, modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(), textAlign = TextAlign.Center)
    }

    object Shelf: HomeScreen("书架") {


    }
    object Category: HomeScreen("分类") {

    }
    object Rank: HomeScreen("榜单") {

    }
}