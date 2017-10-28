package com.teamnull.taskcity.api

import com.teamnull.taskcity.util.CsvUtils
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
        val fnam = "tmpFile"+System.currentTimeMillis()
        Files.write(File(fnam).toPath(), file.bytes )
        val retHead = CsvUtils.loadHeaders(fnam);

        model.addAttribute("columnNames", retHead)
        model.addAttribute("fileName", fnam)

        return "form"
    }
}