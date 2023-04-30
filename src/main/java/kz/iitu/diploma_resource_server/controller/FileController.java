package kz.iitu.diploma_resource_server.controller;

import kz.iitu.diploma_resource_server.model.File;
import kz.iitu.diploma_resource_server.register.FileRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileRegister fileRegister;

    @Autowired
    public FileController(FileRegister fileRegister) {
        this.fileRegister = fileRegister;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestBody MultipartFile file) {
        return fileRegister.saveFile(file);
    }

    @PostMapping("/load/{id}")
    public File loadFile(@PathVariable("id") String fileId) {
        return fileRegister.loadFile(fileId);
    }

}
