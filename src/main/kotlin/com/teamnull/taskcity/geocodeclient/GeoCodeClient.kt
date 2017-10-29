package com.teamnull.taskcity.geocodeclient

import khttp.get
import org.apache.coyote.Response
import org.json.JSONArray
import java.net.URLEncoder
import java.util.ArrayList

class GeoCodeClient {

    fun getCordsFromAddress(addressLine: String) : List<Coordinate> {
        return callGeoCode(addressLine);
    }

    private fun callGeoCode(addressLine: String): List<Coordinate> {
        var errorFlad = false
        var jsonArray:JSONArray? = null
        var getObject:khttp.responses.Response? = null
        do{
            getObject = get("http://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer/findAddressCandidates" +
                    "?SingleLine=" + URLEncoder.encode(addressLine,"UTF-8") +
                    "&forStorage=false" +
                    "&f=pjson")
        }while (errorFlad)
        println(getObject!!.statusCode)
        if(getObject!!.statusCode == 503){
            println("2")
        }
        jsonArray =getObject!!.jsonObject.getJSONArray("candidates")

        return parseResponse(jsonArray!!)
    }

    private fun parseResponse(jsonArray: JSONArray): MutableList<Coordinate> {
        val candidates: MutableList<Coordinate> = ArrayList()

        for (i in 0 until jsonArray.length()) {
            val coords = jsonArray.getJSONObject(i);
            val location = coords.getJSONObject("location");
            val x: String = location.get("x").toString();
            val y: String = location.get("y").toString();

            val score = coords.get("score").toString();
            if( score.toFloat() > 75.0 ){
                candidates.add(Coordinate(x.toFloat(), y.toFloat()));
            }

        }

        return candidates;
    }
}