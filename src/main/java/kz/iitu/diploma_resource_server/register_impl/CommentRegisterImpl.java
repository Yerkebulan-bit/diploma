package kz.iitu.diploma_resource_server.register_impl;

import kz.iitu.diploma_resource_server.model.Comment;
import kz.iitu.diploma_resource_server.register.CommentRegister;
import kz.iitu.diploma_resource_server.sql.CommentTable;
import kz.iitu.diploma_resource_server.util.Strings;
import kz.iitu.diploma_resource_server.util.sql.SqlSelectTo;
import kz.iitu.diploma_resource_server.util.sql.SqlUpsert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

@Component
public class CommentRegisterImpl implements CommentRegister {

    private final DataSource source;

    @Autowired
    public CommentRegisterImpl(DataSource source) {
        this.source = source;
    }

    @Override
    public String saveComment(String userId, String eventId, String text) {
        Strings.requiresNotNullOrEmpty(text);
        Strings.requiresNotNullOrEmpty(userId);
        Strings.requiresNotNullOrEmpty(eventId);

        var id = UUID.randomUUID().toString();

        SqlUpsert.into(CommentTable.TABLE_NAME)
                .key(CommentTable.ID, id)
                .field(CommentTable.USER_ID, userId)
                .field(CommentTable.EVENT_ID, eventId)
                .field(CommentTable.TEXT, text)
                .toUpdate().ifPresent(u -> u.applyTo(source));

        return id;
    }

    @Override
    public List<Comment> loadCommentsByEvent(String eventId) {

        Strings.requiresNotNullOrEmpty(eventId);

        return SqlSelectTo.theClass(Comment.class)
                .sql(CommentTable.SELECT_COMMENTS_BY_ID)
                .param(eventId)
                .applyTo(source);
    }

}
