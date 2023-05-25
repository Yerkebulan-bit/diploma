package kz.iitu.diploma_resource_server.sql;

public class NewsTable {

    public static final String TABLE_NAME = "news";
    public static final String ID = "id";
    public static final String CATEGORY = "category";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String IMAGE_ID = "image_id";

    public static final String SELECT_ALL_NEWS = "SELECT " +
            ID + " as id, " +
            CATEGORY + " as category, " +
            TITLE + " as title, " +
            IMAGE_ID + " as imageId, " +
            CONTENT + " as content " +
            "FROM " + TABLE_NAME;

}
