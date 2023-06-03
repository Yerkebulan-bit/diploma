package kz.iitu.diploma_resource_server.sql;

import kz.iitu.diploma_resource_server.model.PeriodRange;
import kz.iitu.diploma_resource_server.model.SearchFilter;
import kz.iitu.diploma_resource_server.model.event.Day;
import kz.iitu.diploma_resource_server.util.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class SqlBuilder {

    public static String eventSelectByFilter(SearchFilter filter, Day day) {

        if (filter == null) {
            throw new RuntimeException();
        }

        var sql = new StringBuilder();
        sql.append(EventTable.BASE_SELECT);
        sql.append(" ");

        appendSearch(sql, filter);

        if (filter.onlyMain) {
            appendMainEventCondition(sql);
        }

        if (filter.currentWeek) {
            appendCurrentWeekCondition(sql);
        }

        if (filter.soon) {
            appendSoonCondition(sql);
        }

        if (StringUtils.isNotNullOrEmpty(filter.sortColumn)) {
            appendSortOrder(sql, filter);
        }

        if (day != null && day != Day.ALL) {
            appendSearchByDay(sql, day);
        }

        appendTimeLimitation(sql);

        appendOffsetLimit(sql, filter);

        System.out.println(sql);
        return sql.toString();
    }

    private static void appendSearchByDay(StringBuilder sb, Day day) {
        sb.append("AND ");
        sb.append(EventTable.DAY);
        sb.append(" = '");
        sb.append(day);
        sb.append("' ");
    }

    private static void appendCurrentWeekCondition(StringBuilder sb) {
        var currentWeekPeriod = currentWeek();

        sb.append("AND ");
        sb.append(EventTable.STARTED_AT);
        sb.append(" > '");
        sb.append(currentWeekPeriod.firstDate);
        sb.append("' AND ");
        sb.append(EventTable.STARTED_AT);
        sb.append(" < '");
        sb.append(currentWeekPeriod.lastDate);
        sb.append("' ");

    }

    private static void appendSoonCondition(StringBuilder sb) {
        sb.append("AND ");
        sb.append(EventTable.STARTED_AT);
        sb.append(" > '");
        sb.append(LocalDate.now().plusMonths(1));
        sb.append("' ");
    }

    private static PeriodRange currentWeek() {
        var firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        var lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);

        var firstDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(firstDayOfWeek)); // first day
        var lastDate = LocalDate.now().with(TemporalAdjusters.nextOrSame(lastDayOfWeek));      // last day

        return PeriodRange.of(firstDate, lastDate);
    }

    private static void appendMainEventCondition(StringBuilder sb) {
        sb.append("AND ");
        sb.append(EventTable.IS_MAIN);
        sb.append(" = true ");
    }

    private static void appendSearch(StringBuilder sb, SearchFilter filter) {
        sb.append("WHERE ");
        sb.append(filter.searchColumn);
        sb.append(" LIKE '%");
        sb.append(filter.search);
        sb.append("%' ");
    }

    private static void appendSortOrder(StringBuilder sb, SearchFilter filter) {
        sb.append("ORDER BY ");
        sb.append(filter.sortColumn);
        sb.append(" ");
        sb.append(filter.sortOrder);
        sb.append(" ");

    }

    private static void appendTimeLimitation(StringBuilder sb) {
        sb.append(" AND ");
        sb.append(EventTable.STARTED_AT);
        sb.append(" >= '");
        sb.append(LocalDate.now());
        sb.append("' ");
    }

    private static void appendOffsetLimit(StringBuilder sb, SearchFilter filter) {
        sb.append("LIMIT ");
        sb.append(filter.limit);
        sb.append(" ");

        sb.append("OFFSET ");
        sb.append(filter.offset);
        sb.append(" ");
    }

}
