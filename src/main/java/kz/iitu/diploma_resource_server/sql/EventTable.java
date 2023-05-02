package kz.iitu.diploma_resource_server.sql;

public class EventTable {
    public static final String TABLE_NAME = "event";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String DESCRIPTION = "description";
    public static final String SHORT_DESC = "short_description";
    public static final String TIME = "time";
    public static final String RUNNING_TIME = "running_time";
    public static final String LOCATION = "location";
    public static final String STARTED_AT = "started_at";
    public static final String ENDED_AT = "ended_at";
    public static final String CONSTRAINTS = "constraints";
    public static final String ORG = "organization";
    public static final String IS_MAIN = "is_main";
    public static final String DAY = "day";
    public static final String LIMIT = "limit_participant";
    public static final String CURRENT = "current_participants";
    public static final String IMAGE_ID = "image_id";

    public static final String BASE_SELECT = "SELECT " +
            ID + " as id, " +
            NAME + " as name, " +
            TYPE + " as type, " +
            SHORT_DESC + " as shortDescription, " +
            TIME + " as time, " +
            RUNNING_TIME + " as runningTime, " +
            STARTED_AT + " as startedAt, " +
            CONSTRAINTS + " as constraints, " +
            IMAGE_ID + " as imageId, " +
            LIMIT + " as limit, " +
            CURRENT + " as current, " +
            IS_MAIN + " as isMain " +
            "FROM " + TABLE_NAME;

    public static final String BASE_SELECT_DETAILS_BY_ID = "SELECT " +
            NAME + " as name, " +
            TYPE + " as type, " +
            DESCRIPTION + " as description, " +
            LOCATION + " as location, " +
            ENDED_AT + " as endedAt, " +
            ORG + " as organizationId, " +
            SHORT_DESC + " as shortDescription, " +
            TIME + " as time, " +
            RUNNING_TIME + " as runningTime, " +
            STARTED_AT + " as startedAt, " +
            CONSTRAINTS + " as constraints, " +
            LIMIT + " as limit, " +
            CURRENT + " as current, " +
            IMAGE_ID + " as imageId, " +
            IS_MAIN + " as isMain " +
            "FROM " + TABLE_NAME
            + " WHERE " + ID + " = ?";

    public static final String SELECT_EVENTS_BY_ORG_ID = BASE_SELECT + " WHERE " +
            ORG + " = ?";

    public static final String SELECT_EVENT_BY_ID = BASE_SELECT + " WHERE " +
            ID + " = ?";

}

