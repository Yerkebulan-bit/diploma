package kz.iitu.diploma_resource_server.controller;

import kz.iitu.diploma_resource_server.model.FeedbackMessage;
import kz.iitu.diploma_resource_server.register.FeedbackRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackRegister feedbackRegister;

    @Autowired
    public FeedbackController(FeedbackRegister feedbackRegister) {
        this.feedbackRegister = feedbackRegister;
    }

    @PostMapping("/send-message")
    public void sendMessage(@RequestBody FeedbackMessage message) {
        feedbackRegister.sendMessage(message);
    }

}
