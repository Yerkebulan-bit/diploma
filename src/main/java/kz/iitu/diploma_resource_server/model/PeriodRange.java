package kz.iitu.diploma_resource_server.model;

import java.time.LocalDate;

public class PeriodRange {
    public LocalDate firstDate;
    public LocalDate lastDate;

    public static PeriodRange of(LocalDate firstDate, LocalDate lastDate) {
        var period = new PeriodRange();

        period.firstDate = firstDate;
        period.lastDate = lastDate;

        return period;
    }
}
