package com.teamnull.taskcity.processor

import com.teamnull.taskcity.api.CSVController
import com.teamnull.taskcity.csvclient.AddressData
import com.teamnull.taskcity.geocodeclient.GeoCodeClient
import java.util.concurrent.CopyOnWriteArrayList
import com.teamnull.taskcity.util.LatToxy
import java.util.concurrent.Executors

class CsvProcessor(fileInput: String) {
    private val geoCodeClient = GeoCodeClient()
    private val csvController: CSVController = CSVController(fileInput);
    private val executorService = Executors.newFixedThreadPool(10)
    private val unprocessedAddressData: MutableList<AddressData> = CopyOnWriteArrayList()

    fun getOutputFile(csvHeaders: Map<String, String>): String {
        csvController.LoadAddress(csvHeaders)
        return processAddresses(csvController.GetAddress()!!)
    }

    fun getUnprocessedAdressesFilename() : String {
        return csvController.saveUnprocessedAddresses(unprocessedAddressData);
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
                        } else {
                            unprocessedAddressData.add(address);

                        }
                    }
                }
        executorService.shutdown()
        while (!executorService.isTerminated) {
        }

        return csvController.SaveAddress()
    }
}
