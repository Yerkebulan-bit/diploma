package kz.iitu.diploma_resource_server.model;

import kz.iitu.diploma_resource_server.model.event.Day;
import kz.iitu.diploma_resource_server.sql.EventTable;

public class SearchFilter {
    public int offset;
    public int limit;

    public String search = "";
    public String searchColumn = EventTable.NAME;

    public String sortColumn;
    public SortOrder sortOrder = SortOrder.ASC;

    public boolean onlyMain = false;
    public boolean currentWeek = false;
    public boolean soon = false;

    public Day day = Day.ALL;

    public static SearchFilter onlyMain() {
        var filter = new SearchFilter();

        filter.offset = 0;
        filter.limit = 20;
        filter.search = "";
        filter.sortOrder = SortOrder.ASC;
        filter.onlyMain = true;

        return filter;
    }

    public static SearchFilter currentWeak() {
        var filter = new SearchFilter();

        filter.offset = 0;
        filter.limit = 20;
        filter.search = "";
        filter.sortOrder = SortOrder.ASC;
        filter.currentWeek = true;

        return filter;
    }

    public static SearchFilter soon() {
        var filter = new SearchFilter();

        filter.offset = 0;
        filter.limit = 20;
        filter.search = "";
        filter.sortOrder = SortOrder.ASC;
        filter.soon = true;

        return filter;
    }

    @Override
    public String toString() {
        return "SearchFilter{" +
                "offset=" + offset +
                ", limit=" + limit +
                ", search='" + search + '\'' +
                ", searchColumn='" + searchColumn + '\'' +
                ", sortColumn='" + sortColumn + '\'' +
                ", sortOrder=" + sortOrder +
                ", onlyMain=" + onlyMain +
                ", currentWeek=" + currentWeek +
                '}';
    }
}
