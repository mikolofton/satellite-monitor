package com.mikolofton.satellite.satellitemonitor.models;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link SatelliteComponentAlert}.
 */
public class SatelliteComponentAlertTest {

    @Test
    public void builderValid() {
        SatelliteComponentAlert.newBuilder()
            .setSatelliteId(1000)
            .setSeverity(SatelliteComponentAlert.Severity.RED_HIGH.toString())
            .setComponent(SatelliteComponent.ComponentType.TSTAT.name())
            .setTimestamp("2018-01-01T23:01:38.001Z")
            .build();
    }

    @Test
    public void builderInvalid() {
        assertThrows(IllegalArgumentException.class, () -> SatelliteComponentAlert.newBuilder()
            .setSatelliteId(-1000)
            .setSeverity(SatelliteComponentAlert.Severity.RED_HIGH.toString())
            .setComponent(SatelliteComponent.ComponentType.TSTAT.name())
            .setTimestamp("2018-01-01T23:01:38.001Z")
            .build());

        assertThrows(NullPointerException.class, () -> SatelliteComponentAlert.newBuilder()
            .setSatelliteId(1000)
            .setSeverity(SatelliteComponentAlert.Severity.RED_HIGH.toString())
            .setTimestamp("2018-01-01T23:01:38.001Z")
            .build());

        assertThrows(NullPointerException.class, () -> SatelliteComponentAlert.newBuilder()
            .setSatelliteId(1000)
            .setComponent(SatelliteComponent.ComponentType.TSTAT.name())
            .setTimestamp("2018-01-01T23:01:38.001Z")
            .build());

        assertThrows(NullPointerException.class, () -> SatelliteComponentAlert.newBuilder()
            .setSatelliteId(1000)
            .setSeverity(SatelliteComponentAlert.Severity.RED_HIGH.toString())
            .setComponent(SatelliteComponent.ComponentType.TSTAT.name())
            .build());
    }

    @Test
    public void equalsAndHashCode() {
        final SatelliteComponentAlert original = SatelliteComponentAlert.newBuilder()
            .setSatelliteId(1000)
            .setSeverity(SatelliteComponentAlert.Severity.RED_HIGH.toString())
            .setComponent(SatelliteComponent.ComponentType.TSTAT.name())
            .setTimestamp("2018-01-01T23:01:38.001Z")
            .build();

        final SatelliteComponentAlert same = SatelliteComponentAlert.newBuilder()
            .setSatelliteId(1000)
            .setSeverity(SatelliteComponentAlert.Severity.RED_HIGH.toString())
            .setComponent(SatelliteComponent.ComponentType.TSTAT.name())
            .setTimestamp("2018-01-01T23:01:38.001Z")
            .build();

        final SatelliteComponentAlert different = SatelliteComponentAlert.newBuilder()
            .setSatelliteId(1000)
            .setSeverity(SatelliteComponentAlert.Severity.RED_LOW.toString())
            .setComponent(SatelliteComponent.ComponentType.BATT.name())
            .setTimestamp("2018-01-01T23:01:09.521Z")
            .build();

        assertEquals(original.hashCode(), same.hashCode());
        assertNotEquals(original.hashCode(), different.hashCode());

        assertEquals(original, same);
        assertNotEquals(original, different);
    }

    @Test
    public void testString() {
        final String expected = "{\n" +
            "  \"satelliteId\": 1000,\n" +
            "  \"severity\": \"RED HIGH\",\n" +
            "  \"component\": \"TSTAT\",\n" +
            "  \"timestamp\": \"2018-01-01T23:01:38.001Z\"\n" +
            "}";

        final SatelliteComponentAlert satelliteComponentAlert = SatelliteComponentAlert.newBuilder()
            .setSatelliteId(1000)
            .setSeverity(SatelliteComponentAlert.Severity.RED_HIGH.toString())
            .setComponent(SatelliteComponent.ComponentType.TSTAT.name())
            .setTimestamp("2018-01-01T23:01:38.001Z")
            .build();

        assertEquals(expected, satelliteComponentAlert.toString());
    }
}
