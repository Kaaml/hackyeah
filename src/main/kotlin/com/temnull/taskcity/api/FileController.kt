package com.teamnull.taskcity.api

import com.temnull.taskcity.api.CSVController
import com.temnull.taskcity.geocodeclient.GeoCodeClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.concurrent.Executors

@RestController
class FileController {

    @PostMapping("/file")
    fun uploadFile(@RequestParam("file") file: MultipartFile): Int {
        var bytes = file.bytes
        return bytes.size
    }
    @GetMapping("/csv")
    fun downloadResult() {
        val data = CSVController("dane wejściowe.csv","wyjscie.csv")
        val geo = GeoCodeClient()

        data.LoadHeaders()


        val addressHeaders: Map<String, String> = hashMapOf("city" to "miejsce","street" to "UL","number" to "NR_BUD" )
        data.LoadAddress(addressHeaders)

        val list = data.GetAddress()
        val threadsNumber = 10
        val threadDataSize = list!!.size/threadsNumber

        var startTime : Long =System.currentTimeMillis()

        val executor = Executors.newFixedThreadPool(threadsNumber)
        for (i in 0..threadsNumber-1) {
            val worker = Runnable {
                val startData = i*threadDataSize
                var endData: Int = (i+1)*threadDataSize-1
                if(i == threadsNumber-1)
                    endData =list!!.size

                for (k:Int in startData..endData ){
                    val position = geo.getCordsFromAddress(list[k].GetAddress())
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

        return data.SaveAddress()
    }
}