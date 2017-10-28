package com.teamnull.taskcity.processor

import com.teamnull.taskcity.api.CSVController
import com.teamnull.taskcity.csvclient.AddressData
import com.teamnull.taskcity.geocodeclient.GeoCodeClient
import com.teamnull.taskcity.util.LatToxy
import java.util.concurrent.Executors

class CsvProcessor(fileInput: String, fileOutput: String) {
    private val geoCodeClient = GeoCodeClient()
    private val csvController: CSVController = CSVController(fileInput, fileOutput)
    private val executorService = Executors.newFixedThreadPool(10)

    fun getOutputFile(csvHeaders: Map<String, String>): String {
        csvController.LoadAddress(csvHeaders)
        return processAddresses(csvController.GetAddress()!!)
    }

    private fun processAddresses(addresses: List<AddressData>): String {
        addresses
                .forEach { address ->
                    executorService.execute {
                        val coordinates = geoCodeClient.getCordsFromAddress(address.GetAddress())
                        if (!coordinates.isEmpty()) {
                            val tmp = LatToxy.convert(coordinates[0].x.toDouble(), coordinates[0].y.toDouble())
                            address.x = tmp.x.toFloat()
                            address.y = tmp.y.toFloat()
                        }
                    }
                }
        executorService.shutdown()
        while (!executorService.isTerminated) {
        }

        return csvController.SaveAddress()
    }
}
