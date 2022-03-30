package com.wulinpeng.easyreader.bookserver.source

/**
 * author：wulinpeng
 * date：2021/10/14 16:58
 * desc:
 */
object BookSourceManager {

    private lateinit var currentSource: BookSource
    private val sources = mutableListOf<BookSource>()

    fun addBookSource(source: BookSource, setCurrent: Boolean = true): BookSourceManager {
        sources.firstOrNull {it.sourceName() == source.sourceName()}?.apply {
            removeBookSource(this)
        }
        sources.add(source)
        if (setCurrent) {
            setCurrentSource(source.sourceName())
        }
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

    fun setCurrentSource(name: String) {
        findSource(name)?.apply {
            currentSource = this
        }
    }

    fun currentSource(): BookSource {
        return currentSource
    }
}