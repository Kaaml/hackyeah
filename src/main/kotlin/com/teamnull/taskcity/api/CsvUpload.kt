package com.teamnull.taskcity.api

import com.teamnull.taskcity.model.HeaderModel
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files

@Controller
class CsvUpload {
    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile, model: Model): String {
        val fnam = "dupa"+System.currentTimeMillis()
        Files.write(File(fnam).toPath(), file.bytes )
        val data = CSVController(fnam, fnam+".out")
        val retHead = data.LoadHeaders()


        model.addAttribute("prods", retHead)
        model.addAttribute("pupa", fnam)


        return "form"
    }
}