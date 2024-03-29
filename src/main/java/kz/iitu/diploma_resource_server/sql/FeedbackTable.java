package kz.iitu.diploma_resource_server.sql;

public class FeedbackTable {

    public static final String TABLE_NAME = "feedback_messages";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String HEAR = "hear_about";
    public static final String MESSAGE = "message";

    public static final String SELECT_MESSAGES = "SELECT " +
            ID + " as id," +
            NAME + " as name," +
            EMAIL + " as email," +
            HEAR + " as hearFrom," +
            MESSAGE + " as message " +
            "FROM " + TABLE_NAME;

}