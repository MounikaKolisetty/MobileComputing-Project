package com.rescuer.api.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class LocalDateTimeParser {

    // defaults if app doesn't pass
    private static final FormatStyle defaultFormatStyle = FormatStyle.MEDIUM;
    private static final DateTimeFormatter defaultDateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(defaultFormatStyle);

    public static String toUserFriendlyLocalDateTime(LocalDateTime localDateTime) {
        return toUserFriendlyLocalDateTime(localDateTime, defaultDateTimeFormatter);
    }

    public static String toUserFriendlyLocalDateTime(LocalDateTime localDateTime, FormatStyle formatStyle) {
        return toUserFriendlyLocalDateTime(localDateTime, DateTimeFormatter.ofLocalizedDateTime(formatStyle));
    }

    public static String toUserFriendlyLocalDateTime(LocalDateTime localDateTime,
                                                            DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.format(localDateTime);
    }
}