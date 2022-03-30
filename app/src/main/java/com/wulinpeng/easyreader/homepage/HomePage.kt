package com.wulinpeng.easyreader.homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.wulinpeng.easyreader.R
import com.wulinpeng.easyreader.homepage.viewmodel.HomePageViewModel
import kotlinx.coroutines.launch

/**
 * author：wulinpeng
 * date：2021/10/15 13:38
 * desc:
 */

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomePage(vm: HomePageViewModel) {
    val pagerState = rememberPagerState()
    val screens = listOf(HomeScreen.Shelf, HomeScreen.Category, HomeScreen.Rank)
    Column {
        Row(modifier = Modifier
            .padding(10.dp, 0.dp, 10.dp, 0.dp)) {
            Image(painter = rememberVectorPainter(image = Icons.Outlined.Person),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(25.dp)
                    .height(25.dp))
            TabRow(selectedTabIndex = pagerState.currentPage,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .padding(80.dp, 0.dp, 80.dp, 0.dp),
                backgroundColor = Color.Transparent, divider = {}) {
                screens.forEachIndexed { index, homeScreen ->
                    Tab(selected = pagerState.currentPage == index, onClick = {
                        // TODO:
                        vm.viewModelScope.launch {
                            pagerState.scrollToPage(index)
                        }
                    }, modifier = Modifier.wrapContentSize()) {
                        Text(text = homeScreen.title,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(4.dp, 15.dp, 4.dp, 15.dp),
                            color = Color.Black)
                    }
                }
            }
            Image(painter = rememberVectorPainter(image = Icons.Outlined.Search),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(25.dp)
                    .height(25.dp)
                    .clickable {
                        vm.openSearch()
                    })
        }
        Divider(color = colorResource(R.color.common_gray_bg))
        HorizontalPager(count = screens.size,
            state = pagerState,
            modifier = Modifier.weight(1f)) { page ->
            screens[page].Screen(vm = vm)
        }
    }
}