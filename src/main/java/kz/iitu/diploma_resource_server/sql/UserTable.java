package kz.iitu.diploma_resource_server.sql;

public class UserTable {

    public static final String TABLE_NAME = "user_info";
    public static final String ID = "id";
    public static final String NAME = "first_name";
    public static final String SURNAME = "last_name";
    public static final String BIRTH = "birth_date";
    public static final String EMAIL = "email";
    public static final String ABOUT = "about";
    public static final String PHONE = "phone_numbers";
    public static final String IMAGE_ID = "image_id";
    public static final String USERNAME = "username";

    public static final String SELECT_USERS_BY_EVENT = "SELECT u." +
            ID + " as id, " +
            NAME + " as name, " +
            SURNAME + " as surname, " +
            BIRTH + " as birth, " +
            EMAIL + " as email, " +
            ABOUT + " as about, " +
            PHONE + " as phone, " +
            "u." + IMAGE_ID + " as imageId " +
            "FROM event e " +
            "JOIN user_event ue ON e.id = ue.event_id " +
            "JOIN users u ON ue.user_id = u.id " +
            "WHERE e.id = ?";

    public static final String SELECT_USER_BY_NICKNAME = "SELECT " +
            NAME + " as name, " +
            ID + " as id, " +
            SURNAME + " as surname, " +
            BIRTH + " as birth, " +
            EMAIL + " as email, " +
            ABOUT + " as about, " +
            PHONE + " as phone, " +
            IMAGE_ID + " as imageId FROM diploma." + TABLE_NAME +
            " WHERE username = ?";

    public static final String SELECT_EVENT_IDS_BY_USER_ID = "SELECT " +
            "event_id as id " +
            "FROM user_event " +
            "WHERE user_id = ?";

    public static final String SELECT_EVENT_BY_USER_ID = "SELECT " +
            "id " +
            "FROM user_event " +
            "WHERE user_id = ? AND event_id = ?";

    public static final String SELECT_FAVORITE_EVENT_IDS_BY_USER_ID = "SELECT " +
            "event_id as id " +
            "FROM favorite_event " +
            "WHERE user_id = ?";
}
