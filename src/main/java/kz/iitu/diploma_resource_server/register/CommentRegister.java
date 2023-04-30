package kz.iitu.diploma_resource_server.register;

import kz.iitu.diploma_resource_server.model.Comment;

import java.util.List;

public interface CommentRegister {

    String saveComment(String userId, String eventId, String text);

    List<Comment> loadCommentsByEvent(String eventId);

}
