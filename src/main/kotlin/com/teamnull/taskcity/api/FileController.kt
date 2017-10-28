package com.teamnull.taskcity.api

import com.teamnull.taskcity.geocodeclient.GeoCodeClient
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.util.concurrent.Executors


@RestController
class FileController {
    private val LOGGER = LoggerFactory.getLogger(FileController::class.java);

    @PostMapping("/csv")
    fun downloadResult(@RequestParam("filename") fileName:String,
                       @RequestParam("street") street: String,
                       @RequestParam("city") city:String,
                       @RequestParam("houseNumber") houseNumber: String) : HttpEntity<ByteArray> {

        val csvController = CSVController(fileName, "wyjscie.csv")
        val geoCodeClient = GeoCodeClient()

        csvController.LoadHeaders()


        val addressHeaders: Map<String, String> = hashMapOf("city" to city,"street" to street,"number" to houseNumber)
        csvController.LoadAddress(addressHeaders)

        val list = csvController.GetAddress()
        val threadsNumber = 10
        var threadDataSize = list!!.size/threadsNumber

        var startTime : Long =System.currentTimeMillis()

        if(threadDataSize<= 0) {
            LOGGER.warn("List of record suprisingly small");
            threadDataSize = list.size;
        }

        LOGGER.info("Processing started. Chunk size: " + threadDataSize);

        val executor = Executors.newFixedThreadPool(threadsNumber)


        for (i in 0..threadsNumber-1) {
            val worker = Runnable {
                val startData = i*threadDataSize
                var endData: Int = (i+1)*threadDataSize-1
                if(i == threadsNumber-1)
                    endData =list!!.size

                for (k:Int in startData..endData ){
                    val position = geoCodeClient.getCordsFromAddress(list[k].GetAddress())
                    if(!position.isEmpty()){
                        list[k].x = position.first().x
                        list[k].y = position.first().y
                    }
                }
            }
            executor.execute(worker)
        }
        executor.shutdown()
        while (!executor.isTerminated) {
        }

        var endTime : Long =System.currentTimeMillis()
        println("Finished - time: ${endTime-startTime}")

        val outputFileName = csvController.SaveAddress()

        val documentBody = File(outputFileName).readBytes();
        val header = HttpHeaders()
        header.contentType = MediaType.APPLICATION_PDF
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + fileName.replace(" ", "_"))
        header.contentLength = documentBody.size.toLong();
        return HttpEntity(documentBody, header)
    }
}