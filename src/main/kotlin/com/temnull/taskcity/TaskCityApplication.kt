package com.teamnull.taskcity


import com.teamnull.taskcity.api.FileController
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.concurrent.atomic.AtomicLong

@SpringBootApplication
class TaskCityApplication

@Bean
fun controller() = FileController()


fun main(args: Array<String>) {
    SpringApplication.run(TaskCityApplication::class.java, *args)
}
