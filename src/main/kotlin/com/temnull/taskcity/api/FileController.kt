package com.temnull.taskcity.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class FileController {

    @PostMapping("/file")
    fun uploadFile(@RequestParam file: MultipartFile): Int {
        var bytes = file.bytes
        return bytes.size;

    }

}