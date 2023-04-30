package kz.iitu.diploma_resource_server.register_impl;

import kz.iitu.diploma_resource_server.model.File;
import kz.iitu.diploma_resource_server.register.FileRegister;
import kz.iitu.diploma_resource_server.sql.FileTable;
import kz.iitu.diploma_resource_server.util.sql.SqlSelectTo;
import kz.iitu.diploma_resource_server.util.sql.SqlUpsert;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.util.UUID;

@Component
public class FileRegisterImpl implements FileRegister {

    private DataSource dataSource;

    @Autowired
    public FileRegisterImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    @SneakyThrows
    public String saveFile(MultipartFile file) {
        var id = UUID.randomUUID().toString();

        SqlUpsert.into(FileTable.TABLE_NAME)
                .key(FileTable.ID, id)
                .field(FileTable.NAME, file.getName())
                .field(FileTable.CONTENT, file.getBytes())
                .field(FileTable.TYPE, file.getContentType())
                .toUpdate().ifPresent(u -> u.applyTo(dataSource));

        return id;
    }

    @Override
    public File loadFile(String fileId) {
        return SqlSelectTo.theClass(File.class)
                .sql(FileTable.SELECT_FILE_BY_ID)
                .param(fileId)
                .applyTo(dataSource)
                .stream().findFirst().orElseThrow();
    }

}
