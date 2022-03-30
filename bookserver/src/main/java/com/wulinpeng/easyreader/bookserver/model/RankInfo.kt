package com.wulinpeng.easyreader.bookserver.model

/**
 * author：wulinpeng
 * date：2022/3/28 03:38
 * desc:
 */
data class RankInfo(val name: String,
                    val imgUrl: String,
                    // 用于业务自己使用
                    val customParam: Any? = null)