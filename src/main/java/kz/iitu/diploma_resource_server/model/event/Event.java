package kz.iitu.diploma_resource_server.model.event;

import java.util.Date;

public class Event {

    public String id;
    public String name;
    public String type;
    public String shortDescription;
    public String time;
    public int runningTime;
    public String constraints;
    public String imageId;
    public Date startedAt;
    public boolean isMain;
    public int limit;
    public int current;

}
