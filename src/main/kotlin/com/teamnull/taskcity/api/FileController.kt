package com.teamnull.taskcity.api

import com.teamnull.taskcity.processor.CsvProcessor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import java.io.File
import java.util.*


@Controller
class FileController {
    private val LOGGER = LoggerFactory.getLogger(FileController::class.java);

    @PostMapping("/csv")
    fun downloadResult(@RequestParam("filename") fileName: String,
                       @RequestParam("street") street: String,
                       @RequestParam("city") city: String,
                       @RequestParam("houseNumber") houseNumber: String, model: Model): String {

        val startTime = Date()
        val addressHeaders: Map<String, String> = hashMapOf("city" to city, "street" to street, "number" to houseNumber)
        val csvProcessor = CsvProcessor(fileName);
        val outputFileName = csvProcessor.getOutputFile(addressHeaders);
        val unprocessedFileName = csvProcessor.getUnprocessedAdressesFilename();
        val processedNumber = csvProcessor.getProcessedNumber();
        val unprocessedNumber = csvProcessor.getUnprocessedNumber();
        val endTime = Date()
        val processingTime = (endTime.time - startTime.time) / 1000
        LOGGER.info("Processing complete !. Processing time: ${processingTime}")
        val reportFileName = buildProcessingReport(startTime, endTime, unprocessedNumber, processedNumber, processingTime.toInt())
        val basePath = "reports/"
        model.addAttribute("resolved", basePath + outputFileName)
        model.addAttribute("unresolved", basePath + unprocessedFileName)
        model.addAttribute("report", basePath + reportFileName)
        return "results"
    }

    fun buildProcessingReport(processingStartDate: Date, processingFinishDate: Date, unprocessedNumber: Int,
                              processedNumber: Int, processingTime: Int): String {
        val filename = "proccessingreport" + System.currentTimeMillis() + ".txt"
        File(filename).printWriter().use { out ->
            out.println("Poczatek przetwarzania:  " + processingStartDate)
            out.println("Koniec przetwarzania: " + processingFinishDate)
            out.println("Czas dekodowania: " + processingTime)
            out.println("Dopasowane rekordy: " + processedNumber)
            out.println("Niedopasowane rekordy: " + unprocessedNumber)
        }
        return filename

    }
}