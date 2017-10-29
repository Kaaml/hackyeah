package com.teamnull.taskcity.api

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.io.File

@RestController
class ReportsController {
    @GetMapping("/reports/{filename}")
    fun getFile(@PathVariable("filename") filename: String): HttpEntity<ByteArray> {
        var fileNameWithExtension = filename
        if (filename.startsWith("proccessingreport")) {
            fileNameWithExtension += ".txt"
        } else {
            fileNameWithExtension += ".csv"
        }
        val documentBody = File(fileNameWithExtension).readBytes()
        val header = HttpHeaders()
        header.contentType = MediaType.APPLICATION_PDF
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + filename.replace(" ", "_"))
        header.contentLength = documentBody.size.toLong()
        return HttpEntity(documentBody, header)
    }

}