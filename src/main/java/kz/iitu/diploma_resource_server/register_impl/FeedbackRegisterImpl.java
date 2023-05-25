package kz.iitu.diploma_resource_server.register_impl;

import kz.iitu.diploma_resource_server.model.FeedbackMessage;
import kz.iitu.diploma_resource_server.register.FeedbackRegister;
import kz.iitu.diploma_resource_server.sql.FeedbackTable;
import kz.iitu.diploma_resource_server.util.sql.SqlUpsert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.UUID;

@Service
public class FeedbackRegisterImpl implements FeedbackRegister {

    private final DataSource dataSource;

    @Autowired
    public FeedbackRegisterImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void sendMessage(FeedbackMessage message) {
        SqlUpsert.into(FeedbackTable.TABLE_NAME)
                .key(FeedbackTable.ID, UUID.randomUUID().toString())
                .field(FeedbackTable.NAME, message.name)
                .field(FeedbackTable.EMAIL, message.email)
                .field(FeedbackTable.HEAR, message.hearFrom)
                .field(FeedbackTable.MESSAGE, message.message)
                .toUpdate().ifPresent(u -> u.applyTo(dataSource));
    }

}
