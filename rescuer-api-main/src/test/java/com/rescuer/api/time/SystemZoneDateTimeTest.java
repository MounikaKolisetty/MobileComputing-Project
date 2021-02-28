package com.rescuer.api.time;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SystemZoneDateTimeTest {

    @Test
    public void shouldABleToGetZoneIdHolder() {
        // Act
        ZoneIdHolder zoneIdHolder = SystemZoneDateTime.getZoneIdHolder();

        // Prepare
        assertThat(zoneIdHolder.getZonedDateTime()).isNotNull();
    }
}