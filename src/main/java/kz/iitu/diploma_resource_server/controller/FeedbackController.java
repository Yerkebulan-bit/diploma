package kz.iitu.diploma_resource_server.controller;

import kz.iitu.diploma_resource_server.model.FeedbackMessage;
import kz.iitu.diploma_resource_server.register.FeedbackRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/load-messages")
    public List<FeedbackMessage> loadMessages() {
        return feedbackRegister.loadMessages();
    }

}
