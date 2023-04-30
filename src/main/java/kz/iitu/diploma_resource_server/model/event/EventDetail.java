package kz.iitu.diploma_resource_server.model.event;

import kz.iitu.diploma_resource_server.model.Organization;

import java.util.Date;

public class EventDetail extends Event {

    public String description;
    public String location;
    public Date endedAt;
    public String organizationId;
    public Organization organization;

}
