package com.teamnull.taskcity


import com.teamnull.taskcity.api.FileController
import com.teamnull.taskcity.api.CsvUpload
import com.teamnull.taskcity.api.ReportsController
import com.teamnull.taskcity.dbclient.DBController
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class TaskCityApplication

@Bean
fun controller() = FileController()

@Bean
fun ffffff() = CsvUpload()

val isFirstStart = false
@Bean
fun ff() = ReportsController()


fun main(args: Array<String>) {
        val k :DBController = DBController()
        k.InitTable()

    SpringApplication.run(TaskCityApplication::class.java, *args)
}
