package kz.iitu.diploma_resource_server.register;

import kz.iitu.diploma_resource_server.model.FeedbackMessage;

public interface FeedbackRegister {

    void sendMessage(FeedbackMessage message);

}
