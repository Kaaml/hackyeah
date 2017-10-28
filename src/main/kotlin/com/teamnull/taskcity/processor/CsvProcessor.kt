package com.teamnull.taskcity.processor

import com.teamnull.taskcity.api.CSVController
import com.teamnull.taskcity.csvclient.AddressData
import com.teamnull.taskcity.geocodeclient.GeoCodeClient
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
                            address.x = coordinates[1].x
                            address.y = coordinates[1].y
                        }
                    }
                }
        executorService.shutdown()
        while (!executorService.isTerminated) {
        }

        return csvController.SaveAddress()
    }
}
