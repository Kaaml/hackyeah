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

        val url = "jdbc:sqlite:dbry.db"
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
                    conn.autoCommit = false
                    list!!.forEach (
                        action = {

                            val sql = "INSERT INTO tempTable  VALUES (\"Kraków\", ?,?,?,? )"
                            val pstmt = conn.prepareStatement(sql)
                            pstmt.setString(1, it.street)
                            pstmt.setString(2, it.number)
                            pstmt.setFloat(3, it.x)
                            pstmt.setFloat(4, it.y)
                            pstmt.execute()
                        }
                    )
                    conn.commit()
                }
            }
        } catch (e: SQLException) {
            println(e.message)
        }










    }

    fun GetCordinate(city:String, street:String, number:String): Coordinate? {
        val url = "jdbc:sqlite:dbry.db"

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
        val url = "jdbc:sqlite:dbry.db"
        Class.forName("org.sqlite.JDBC");

        try {
            DriverManager.getConnection(url).use { conn ->
                conn.createStatement().use { stmt ->

                    val sql = "INSERT INTO tempTable  VALUES (\"Kraków\", ?,?,?,? )"
                    val pstmt = conn.prepareStatement(sql)
                    pstmt.setString(1, street)
                    pstmt.setString(2, number)
                    pstmt.setFloat(3, pos_x)
                    pstmt.setFloat(4, pos_y)
                    pstmt.execute()
                }
            }
        } catch (e: SQLException) {
            println(e.message)
        }
    }
}