package kz.iitu.diploma_resource_server.sql;

public class CodeTable {
    public static final String TABLE_NAME = "auth_code";
    public static final String ID = "id";
    public static final String CODE = "code";
    public static final String USER_ID = "user_id";
    public static final String USERNAME = "username";
    public static final String EXPIRES_AT = "expires_at";
    public static final String ACTUAL = "actual";

    public static final String SELECT_CODE_BY_ID = "SELECT " +
            ID + " as id, " +
            CODE + " as code, " +
            USER_ID + " as userId, " +
            USERNAME + " as username, " +
            EXPIRES_AT + " as expiresAt " +
            " FROM " + TABLE_NAME +
            " WHERE " + ID + " = ? ";

}
