import khttp.get
import org.json.JSONArray
import java.util.ArrayList

class GeoCodeClient {

    fun getCordsFromAddress(addressLine: String) : List<Coordinate> {
        return callGeoCode(addressLine);
    }

    private fun callGeoCode(addressLine: String): List<Coordinate> {
        val jsonArray = get("http://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer/findAddressCandidates" +
                "?SingleLine=" + addressLine +
                "&category=&outFields=*" +
                "&forStorage=false" +
                "&f=pjson").jsonObject.getJSONArray("candidates")

        return parseResponse(jsonArray)
    }

    private fun parseResponse(jsonArray: JSONArray): MutableList<Coordinate> {
        val candidates: MutableList<Coordinate> = ArrayList();

        for (i in 0 until jsonArray.length()) {
            val coords = jsonArray.getJSONObject(i);
            val location = coords.getJSONObject("location");
            val x: String = location.get("x").toString();
            val y: String = location.get("y").toString();
            candidates.add(Coordinate(x.toFloat(), y.toFloat()));
        }

        return candidates;
    }
}