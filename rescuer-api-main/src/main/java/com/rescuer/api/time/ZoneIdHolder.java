package com.rescuer.api.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ZoneIdHolder {

    private final ZoneId zoneId;

    public ZoneIdHolder() {
        this(ZoneIdConstant.UTC);
    }


    public ZoneIdHolder(ZoneIdConstant zoneIdConstant) {
        this.zoneId = ZoneId.of(zoneIdConstant.value());
    }

    public Instant getInstant() {
        return Instant.now();
    }

    public ZonedDateTime getZonedDateTime() {
        return this.getZonedDateTime(Instant.now());
    }

    public ZonedDateTime getZonedDateTime(Instant instant) {
        return ZonedDateTime.ofInstant(instant, this.zoneId);
    }

    public LocalDateTime getLocalDateTime() {
        return this.getZonedDateTime().toLocalDateTime();
    }

    public LocalDateTime getLocalDateTime(Instant instant) {
        return this.getZonedDateTime(instant).toLocalDateTime();
    }

}
