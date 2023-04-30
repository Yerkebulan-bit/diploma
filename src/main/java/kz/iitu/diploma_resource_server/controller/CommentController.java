package kz.iitu.diploma_resource_server.controller;

import kz.iitu.diploma_resource_server.model.Comment;
import kz.iitu.diploma_resource_server.register.CommentRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentRegister commentRegister;

    @Autowired
    public CommentController(CommentRegister commentRegister) {
        this.commentRegister = commentRegister;
    }

    @PostMapping("/save")
    public String saveComment(@RequestParam("userId") String userId,
                              @RequestParam("eventId") String eventId,
                              @RequestParam("text") String text) {
        return commentRegister.saveComment(userId, eventId, text);
    }

    @GetMapping("/load-event-comments")
    public List<Comment> loadEventComments(@RequestParam("eventId") String eventId) {
        return commentRegister.loadCommentsByEvent(eventId);
    }

}
