package kz.iitu.diploma_resource_server.model.event;

import kz.iitu.diploma_resource_server.model.Organization;

import java.util.Date;

public class EventDetail extends Event {

    public String description;
    public String location;
    public Date endedAt;
    public String organizationId;
    public Organization organization;
    public String ytUrl;
    public boolean isFollowed;
    public int userRating;
    public boolean canFollow;

}
