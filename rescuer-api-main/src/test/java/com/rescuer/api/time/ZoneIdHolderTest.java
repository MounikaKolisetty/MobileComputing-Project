package com.rescuer.api.time;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ZoneIdHolderTest {

    @Test
    public void shouldAbleToCreateInstanceWithGiveZoneId() {
        // Act
        ZoneIdHolder zoneIdHolder = new ZoneIdHolder(ZoneIdConstant.CENTRAL_EUROPE);

        // Prepare
        assertThat(zoneIdHolder.getZonedDateTime().toLocalDate())
                .isEqualTo(ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Europe/Paris")).toLocalDate());
    }

    @Test
    public void shouldAbleToGetLocalDateTime() {
        // Prepare
        ZoneIdHolder zoneIdHolder = new ZoneIdHolder(ZoneIdConstant.CENTRAL_EUROPE);

        // Act
        LocalDateTime localDateTime = zoneIdHolder.getLocalDateTime();

        // Assert
        assertThat(localDateTime).isNotNull();
    }

    @Test
    public void shouldAbleToGetLocalDateTimeBasedOnInstant() {
        // Prepare
        ZoneIdHolder zoneIdHolder = new ZoneIdHolder(ZoneIdConstant.CENTRAL_EUROPE);

        // Act
        LocalDateTime localDateTime = zoneIdHolder.getLocalDateTime(Instant.now());

        // Assert
        assertThat(localDateTime).isNotNull();
    }
}