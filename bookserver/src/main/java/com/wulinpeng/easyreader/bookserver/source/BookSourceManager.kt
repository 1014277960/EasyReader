package com.wulinpeng.easyreader.bookserver.source

/**
 * author：wulinpeng
 * date：2021/10/14 16:58
 * desc:
 */
object BookSourceManager {

    private val sources = mutableListOf<BookSource>()

    fun addBookSource(source: BookSource): BookSourceManager {
        sources.firstOrNull {it.sourceName() == source.sourceName()}?.apply {
            removeBookSource(this)
        }
        sources.add(source)
        return this
    }

    fun removeBookSource(source: BookSource): BookSourceManager {
        sources.remove(source)
        return this
    }

    fun findSource(sourceName: String): BookSource? {
        return sources.firstOrNull { it.sourceName() == sourceName }
    }

    fun getAllSources(): List<BookSource> {
        return sources
    }
}