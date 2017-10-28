package com.teamnull.taskcity.api

import com.teamnull.taskcity.processor.CsvProcessor
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.File


@RestController
class FileController {
    private val LOGGER = LoggerFactory.getLogger(FileController::class.java);

    @PostMapping("/csv")
    fun downloadResult(@RequestParam("filename") fileName:String,
                       @RequestParam("street") street: String,
                       @RequestParam("city") city:String,
                       @RequestParam("houseNumber") houseNumber: String) : HttpEntity<ByteArray> {

        val startTime = System.currentTimeMillis();
        val addressHeaders: Map<String, String> = hashMapOf("city" to city,"street" to street,"number" to houseNumber)
        val csvProcessor = CsvProcessor(fileName, "wyjscie.csv");
        val outputFileName = csvProcessor.getOutputFile(addressHeaders);
        val endTime : Long =System.currentTimeMillis()
        println("Finished - time: ${endTime-startTime}")
        val documentBody = File(outputFileName).readBytes()
        val header = HttpHeaders()
        header.contentType = MediaType.APPLICATION_PDF
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + fileName.replace(" ", "_"))
        header.contentLength = documentBody.size.toLong();
        return HttpEntity(documentBody, header)
    }
}