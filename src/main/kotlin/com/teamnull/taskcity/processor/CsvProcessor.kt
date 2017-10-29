package com.teamnull.taskcity.processor

import com.teamnull.taskcity.api.CSVController
import com.teamnull.taskcity.csvclient.AddressData
import com.teamnull.taskcity.dbclient.DBController
import com.teamnull.taskcity.geocodeclient.GeoCodeClient
import java.util.concurrent.CopyOnWriteArrayList
import com.teamnull.taskcity.util.LatToxy
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class CsvProcessor(fileInput: String) {
    private val geoCodeClient = GeoCodeClient()
    private val csvController: CSVController = CSVController(fileInput);
    private val executorService = Executors.newFixedThreadPool(10)
    private val unprocessedAddressData: MutableList<AddressData> = CopyOnWriteArrayList()
    private var processedAddresses: AtomicInteger = AtomicInteger()
    private var unprocessedAddresses: AtomicInteger = AtomicInteger()

    fun getOutputFile(csvHeaders: Map<String, String>): String {
        csvController.LoadAddress(csvHeaders)
        return processAddresses(csvController.GetAddress()!!)
    }

    fun getUnprocessedAdressesFilename() : String {
        return csvController.saveUnprocessedAddresses(unprocessedAddressData);
    }

    fun getProcessedNumber() : Int {
        return processedAddresses.get();
    }

    fun getUnprocessedNumber() : Int {
        return unprocessedAddresses.get();
    }

    private fun processAddresses(addresses: List<AddressData>): String {
        addresses
                .forEach { address ->
                    executorService.execute {

                        val db :DBController =DBController()
                        val positionDB = db.GetCordinate("Kraków",address.street,address.number)
                        if(positionDB == null){

                            val coordinates = geoCodeClient.getCordsFromAddress(address.GetAddress())
                            if (!coordinates.isEmpty()) {
                                val tmp = LatToxy.convert(coordinates[0].x.toDouble(), coordinates[0].y.toDouble())
                                address.x = tmp.x.toFloat()
                                address.y = tmp.y.toFloat()
                                processedAddresses.getAndIncrement()
                            } else {
                                unprocessedAddressData.add(address);
                                unprocessedAddresses.getAndIncrement();

                            }
                            db.InsertNewData(address.city,address.street,address.number,address.x,address.y)
                        }else{
                            val tmp = LatToxy.convert(positionDB.x.toDouble(), positionDB.y.toDouble())
                            address.x = tmp.x.toFloat()
                            address.y = tmp.y.toFloat()
                            processedAddresses.getAndIncrement()
                        }

                        val coordinates = geoCodeClient.getCordsFromAddress(address.GetAddress())
                        if (!coordinates.isEmpty()) {
                            val tmp = LatToxy.convert(coordinates[0].x.toDouble(), coordinates[0].y.toDouble())
                            address.x = tmp.x.toFloat()
                            address.y = tmp.y.toFloat()
                            processedAddresses.getAndIncrement()
                        } else {
                            unprocessedAddressData.add(address);
                            unprocessedAddresses.getAndIncrement();

                        }
                    }
                }
        executorService.shutdown()
        while (!executorService.isTerminated) {
        }

        return csvController.SaveAddress()
    }
}
