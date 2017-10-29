package com.teamnull.taskcity.dbclient

import com.teamnull.taskcity.api.CSVController
import com.teamnull.taskcity.geocodeclient.Coordinate
import com.teamnull.taskcity.geocodeclient.GeoCodeClient
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.sql.SQLException
import java.lang.reflect.Array.setInt
import java.sql.PreparedStatement


class DBController {

    fun InitTable() {

        val url = "jdbc:sqlite:F://dbry.db"
        Class.forName("org.sqlite.JDBC");

        val sql = "CREATE TABLE IF NOT EXISTS tempTable(\n"+
        "	city char(32),\n"+
        "	street char(32),\n"+
        "	number char(32),\n"+
                "	x_pos float,\n"+
                "	y_pos float\n"+
        ");"

        val data = CSVController("Słownik Adresów.csv")

        val addressHeaders: Map<String, String> = hashMapOf("city" to "\uFEFFULICA","street" to "\uFEFFULICA","number" to "ADR_NR" )
        data.LoadAddress(addressHeaders)
        val list = data.GetAddress()

        try {
            DriverManager.getConnection(url).use { conn ->
                conn.createStatement().use { stmt ->
                    stmt.execute(sql)

                    list!!.forEach (
                        action = {
                            stmt.execute("INSERT INTO tempTable  VALUES (\"Kraków\", \"${it.street}\", \"${it.number}\", \"${it.x}\", \"${it.y}\" )")
                        }
                    )
                }
            }
        } catch (e: SQLException) {
            println(e.message)
        }

    }

    fun GetCordinate(city:String, street:String, number:String): Coordinate? {
        val url = "jdbc:sqlite:F://dbry.db"

        Class.forName("org.sqlite.JDBC");

        var corResult: Coordinate? = null
        try {
            DriverManager.getConnection(url).use { conn ->
                conn.createStatement().use { stmt ->
                    val result  =stmt.executeQuery("select * from tempTable where street=\"$street\" and number=\"$number\" LIMIT 1")
                    val x =result.getString("x_pos").toFloat()
                    val y =result.getString("y_pos").toFloat()
                    corResult = Coordinate(x,y)
                }
            }
        } catch (e: SQLException) {
            println(e.message)
        }
        return corResult
    }

    fun InsertNewData (city:String, street:String, number:String, pos_x:Float, pos_y:Float){
        val url = "jdbc:sqlite:F://dbry.db"
        Class.forName("org.sqlite.JDBC");


        try {
            DriverManager.getConnection(url).use { conn ->
                conn.createStatement().use { stmt ->
                    stmt.execute("INSERT INTO tempTable  VALUES (\"Kraków\", \"${street}\", \"${number}\", \"${pos_x}\", \"${pos_y}\" )")
                }
            }
        } catch (e: SQLException) {
            println(e.message)
        }
    }
}