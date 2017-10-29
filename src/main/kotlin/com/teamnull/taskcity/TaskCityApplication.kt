package com.teamnull.taskcity


import com.teamnull.taskcity.api.FileController
import com.teamnull.taskcity.api.CsvUpload
import com.teamnull.taskcity.api.ReportsController
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class TaskCityApplication

@Bean
fun controller() = FileController()

@Bean
fun ffffff() = CsvUpload()

@Bean
fun ff() = ReportsController()


fun main(args: Array<String>) {
    SpringApplication.run(TaskCityApplication::class.java, *args)
}
