package org.redex.backend.zelper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Laboratory {

    public static void main(String[] args) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        LocalDateTime time = LocalDateTime.parse("2018-12-12 23:02", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        time = time.plus(-5, ChronoUnit.HOURS);
        System.out.println(ZonedDateTime.of(time, ZoneId.of("UTC")));
    }
}
