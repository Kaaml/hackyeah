package com.teamnull.taskcity.util

import java.io.File

object CsvUtils {
    fun loadHeaders(fileName: String) : List<String> {
        val headersLine = File(fileName).readLines().first()
        return headersLine.split(';')
    }
}