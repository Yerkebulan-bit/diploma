package kz.iitu.diploma_resource_server.sql;

public class FileTable {

    public static final String TABLE_NAME = "images";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String CONTENT = "img";
    public static final String TYPE = "type";

    public static final String SELECT_FILE_BY_ID = "SELECT " +
            NAME + " as name, " +
            TYPE + " as type, " +
            CONTENT + " as img " +
            "FROM " + TABLE_NAME + " " +
            "WHERE " + ID + " = ?";
}
