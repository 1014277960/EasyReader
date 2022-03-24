package com.wulinpeng.easyreader.readerview.manager

import com.wulinpeng.easyreader.bookserver.model.Chapter
import com.wulinpeng.easyreader.bookserver.model.fillChapterContent
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.suspendCoroutine

/**
 * author：wulinpeng
 * date：2022/3/23 00:23
 * desc:
 */
class ChapterLoadManger(val chapterList: List<Chapter>) {

    private val countDownLatchList = ConcurrentHashMap<Int, CountDownLatch>()

    suspend fun loadChapter(index: Int): Boolean {
        if (index < 0 || index > chapterList.size - 1) {
            return false
        }
        if (countDownLatchList.contains(index)) {
            suspendCoroutine<Unit> {
                countDownLatchList[index]!!.await()
            }
            if (chapterList[index].content != null) {
                return true
            }
        }
        countDownLatchList[index]?.countDown()
        val latch = CountDownLatch(1)
        countDownLatchList[index] = latch
        chapterList[index].fillChapterContent()
        latch.countDown()
        return true
    }
}