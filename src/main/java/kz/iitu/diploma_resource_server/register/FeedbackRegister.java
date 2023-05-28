package kz.iitu.diploma_resource_server.register;

import kz.iitu.diploma_resource_server.model.FeedbackMessage;

import java.util.List;

public interface FeedbackRegister {

    void sendMessage(FeedbackMessage message);

    List<FeedbackMessage> loadMessages();

}
