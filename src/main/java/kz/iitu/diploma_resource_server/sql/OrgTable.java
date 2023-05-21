package kz.iitu.diploma_resource_server.sql;

public class OrgTable {
    public static final String TABLE_NAME = "organization";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String EMAIL = "email";
    public static final String SHORT_DESCRIPTION = "short_desc";
    public static final String DESCRIPTION = "description";
    public static final String PHONE = "phone";
    public static final String SITE = "site";
    public static final String IMAGE_ID = "image_id";
    public static final String USERNAME = "username";

    public static final String SELECT_ORG_BY_USERNAME = "SELECT " +
            NAME + "," +
            ID + "," +
            ADDRESS + "," +
            EMAIL + "," +
            SHORT_DESCRIPTION + " as shortDescription," +
            DESCRIPTION + "," +
            PHONE + "," +
            SITE + "," +
            IMAGE_ID + " as imageId " +
            "FROM " + TABLE_NAME + " " +
            "WHERE " + USERNAME + " = ?";

    public static final String SELECT_ORG_BY_ID = "SELECT " +
            NAME + "," +
            ID + "," +
            ADDRESS + "," +
            EMAIL + "," +
            SHORT_DESCRIPTION + " as shortDescription," +
            DESCRIPTION + "," +
            PHONE + "," +
            SITE + "," +
            IMAGE_ID + " as imageId " +
            "FROM " + TABLE_NAME + " " +
            "WHERE " + ID + " = ?";
}
