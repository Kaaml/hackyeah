package com.temnull.taskcity.api


import java.io.File
import com.temnull.taskcity.csvclient.AddressData
//import org.jetbrains.ktor.cio.readChannel


class CSVController (val fileNameInput:String,val flieNameOuput:String){

    private var addressData: List<AddressData>? = null
    private var header:String? = null

    fun GetAddress(): List<AddressData>? {
        return addressData
    }

    fun LoadHeaders() : List<String> {
        val headersLine = File(fileNameInput).readLines().first()
        return headersLine.split(';')
    }

    private fun GetAddressIndexes(addressHeaders: Map<String, String>  ):Map<String, Int>  {
        val headers = LoadHeaders()

        val result: MutableMap<String, Int> =HashMap()
        result.put("city",headers.indexOf(addressHeaders["city"]) )
        result.put("street",headers.indexOf(addressHeaders["street"] ))
        result.put("number",headers.indexOf(addressHeaders["number"] ))

        return result
    }

    fun LoadAddress( addressHeaders: Map<String, String> ){
        val result : MutableList<AddressData> = ArrayList<AddressData>()
        if(!addressHeaders.isEmpty()){
            val headerIndexes = GetAddressIndexes(addressHeaders)

            val lineStream = File(fileNameInput).readLines()
            header = lineStream.first()

            var isFirstLine = true

            lineStream.forEach(action = {

                if (!isFirstLine){
                    val columns = it.split(';')

                    val indexCity = headerIndexes["city"]
                    val indexStreet = headerIndexes["city"]
                    val indexNumber = headerIndexes["city"]

                    result.add (AddressData(columns[indexCity!!],columns[indexStreet!!],columns[indexNumber!!],it,-1f,-1f))
                }else{
                    isFirstLine=false
                }
            })
        }
        addressData =result
    }

    fun SaveAddress(){

        File(flieNameOuput).printWriter().use { out ->
            out.println("${header};x_pos;y_pos")
            addressData!!.forEach {
                out.println("${it.rowData};${it.x};${it.y}")
            }
        }
    }

}




