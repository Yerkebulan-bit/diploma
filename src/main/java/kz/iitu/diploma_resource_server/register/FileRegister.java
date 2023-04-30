package kz.iitu.diploma_resource_server.register;

import kz.iitu.diploma_resource_server.model.File;
import org.springframework.web.multipart.MultipartFile;

public interface FileRegister {

    String saveFile(MultipartFile file);

    File loadFile(String fileId);

}
