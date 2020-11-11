package com.mikolofton.satellite.satellitemonitor.models;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link SatelliteComponentStatus}.
 */
public class SatelliteComponentStatusTest {

    private final SatelliteComponent satelliteComponent = SatelliteComponent.newBuilder()
        .setComponentType(SatelliteComponent.ComponentType.TSTAT)
        .setRedHighLimit(101)
        .setRedLowLimit(20)
        .setYellowHighLimit(98)
        .setYellowLowLimit(25)
        .build();

    @Test
    public void builderValid() {
        SatelliteComponentStatus.newBuilder()
            .setSatelliteId(1001)
            .setComponent(satelliteComponent)
            .setValue(99.9)
            .setTimestamp(Instant.parse("2018-01-01T23:01:05.001Z"))
            .build();
    }

    @Test
    public void builderInvalid() {
        assertThrows(IllegalArgumentException.class, () -> SatelliteComponentStatus.newBuilder()
            .setSatelliteId(-1001)
            .setComponent(satelliteComponent)
            .setValue(99.9)
            .setTimestamp(Instant.parse("2018-01-01T23:01:05.001Z"))
            .build());

        assertThrows(NullPointerException.class, () -> SatelliteComponentStatus.newBuilder()
            .setSatelliteId(1001)
            .setValue(99.9)
            .setTimestamp(Instant.parse("2018-01-01T23:01:05.001Z"))
            .build());

        assertThrows(IllegalArgumentException.class, () -> SatelliteComponentStatus.newBuilder()
            .setSatelliteId(1001)
            .setComponent(satelliteComponent)
            .setValue(-99.9)
            .setTimestamp(Instant.parse("2018-01-01T23:01:05.001Z"))
            .build());

        assertThrows(NullPointerException.class, () -> SatelliteComponentStatus.newBuilder()
            .setSatelliteId(1001)
            .setComponent(satelliteComponent)
            .setValue(99.9)
            .build());
    }

    @Test
    public void equalsAndHashCode() {
        final SatelliteComponentStatus original = SatelliteComponentStatus.newBuilder()
            .setSatelliteId(1001)
            .setComponent(satelliteComponent)
            .setValue(99.9)
            .setTimestamp(Instant.parse("2018-01-01T23:01:05.001Z"))
            .build();

        final SatelliteComponentStatus same = SatelliteComponentStatus.newBuilder()
            .setSatelliteId(1001)
            .setComponent(satelliteComponent)
            .setValue(99.9)
            .setTimestamp(Instant.parse("2018-01-01T23:01:05.001Z"))
            .build();

        final SatelliteComponentStatus different = SatelliteComponentStatus.newBuilder()
            .setSatelliteId(1000)
            .setComponent(satelliteComponent)
            .setValue(102.9)
            .setTimestamp(Instant.parse("2018-01-01T23:01:38.001Z"))
            .build();

        assertEquals(original.hashCode(), same.hashCode());
        assertNotEquals(original.hashCode(), different.hashCode());

        assertEquals(original, same);
        assertNotEquals(original, different);
    }

    @Test
    public void testString() {
        final String expected = "{\n" +
            "  \"satelliteId\": 1001,\n" +
            "  \"component\": {\n" +
            "    \"componentType\": \"TSTAT\",\n" +
            "    \"redHighLimit\": 101,\n" +
            "    \"redLowLimit\": 20,\n" +
            "    \"yellowHighLimit\": 98,\n" +
            "    \"yellowLowLimit\": 25\n" +
            "  },\n" +
            "  \"value\": 99.9,\n" +
            "  \"timestamp\": {\n" +
            "    \"seconds\": 1514847665,\n" +
            "    \"nanos\": 1000000\n" +
            "  }\n" +
            "}";

        final SatelliteComponentStatus satelliteComponentStatus = SatelliteComponentStatus.newBuilder()
            .setSatelliteId(1001)
            .setComponent(satelliteComponent)
            .setValue(99.9)
            .setTimestamp(Instant.parse("2018-01-01T23:01:05.001Z"))
            .build();

        assertEquals(expected, satelliteComponentStatus.toString());
    }
}
