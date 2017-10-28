package com.teamnull.taskcity.api

import com.temnull.taskcity.api.CSVController
import com.temnull.taskcity.geocodeclient.GeoCodeClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController
class FileController {

    @PostMapping("/file")
    fun uploadFile(@RequestParam("file") file: MultipartFile): Int {
        var bytes = file.bytes
        return bytes.size
    }
    @PostMapping("/csv")
    fun downloadResult() {
        val data = CSVController("dane wejściowe.csv","wyjscie.csv")
        val geo = GeoCodeClient()

        data.LoadHeaders()


        val addressHeaders: Map<String, String> = hashMapOf("city" to "miejsce","street" to "UL","number" to "NR_BUD" )
        data.LoadAddress(addressHeaders)

        val list = data.GetAddress()
        list!!.forEach(action = {
            val position = geo.getCordsFromAddress(it.GetAddress())
            it.x = position.first().x
            it.y = position.first().y
        })

        return data.SaveAddress()
    }
}