package kz.iitu.diploma_resource_server.sql;

public class CommentTable {

    public static final String TABLE_NAME = "comment";
    public static final String ID = "id";
    public static final String USER_ID = "user_id";
    public static final String EVENT_ID = "event_id";
    public static final String TEXT = "text";
    public static final String DATE = "date";

    public static final String SELECT_COMMENTS_BY_ID = "SELECT " +
            "\"user\".first_name||' '||\"user\".last_name as fullName," +
            "c.text as text," +
            "c.date as date " +
            "FROM \"user\" INNER JOIN comment c " +
            "ON c.user_id = \"user\".id WHERE " + EVENT_ID + " = ?";

}
