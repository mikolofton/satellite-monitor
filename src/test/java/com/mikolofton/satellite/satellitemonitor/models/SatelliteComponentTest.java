package com.mikolofton.satellite.satellitemonitor.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link SatelliteComponent}.
 */
public class SatelliteComponentTest {

    @Test
    public void builderValid() {
        SatelliteComponent.newBuilder()
            .setComponentType(SatelliteComponent.ComponentType.TSTAT)
            .setRedHighLimit(101)
            .setRedLowLimit(20)
            .setYellowHighLimit(98)
            .setYellowLowLimit(25)
            .build();
    }

    @Test
    public void builderInvalid() {
        assertThrows(NullPointerException.class, () -> SatelliteComponent.newBuilder()
            .setRedHighLimit(101)
            .setRedLowLimit(20)
            .setYellowHighLimit(98)
            .setYellowLowLimit(25)
            .build());

        assertThrows(IllegalArgumentException.class, () -> SatelliteComponent.newBuilder()
            .setComponentType(SatelliteComponent.ComponentType.TSTAT)
            .setRedHighLimit(-101)
            .setRedLowLimit(20)
            .setYellowHighLimit(98)
            .setYellowLowLimit(25)
            .build());

        assertThrows(IllegalArgumentException.class, () -> SatelliteComponent.newBuilder()
            .setComponentType(SatelliteComponent.ComponentType.TSTAT)
            .setRedHighLimit(101)
            .setRedLowLimit(-25)
            .setYellowHighLimit(98)
            .setYellowLowLimit(25)
            .build());

        assertThrows(IllegalArgumentException.class, () -> SatelliteComponent.newBuilder()
            .setComponentType(SatelliteComponent.ComponentType.TSTAT)
            .setRedHighLimit(101)
            .setRedLowLimit(20)
            .setYellowHighLimit(-98)
            .setYellowLowLimit(25)
            .build());

        assertThrows(IllegalArgumentException.class, () -> SatelliteComponent.newBuilder()
            .setComponentType(SatelliteComponent.ComponentType.TSTAT)
            .setRedHighLimit(101)
            .setRedLowLimit(20)
            .setYellowHighLimit(98)
            .setYellowLowLimit(-20)
            .build());
    }

    @Test
    public void equalsAndHashCode() {
        final SatelliteComponent original = SatelliteComponent.newBuilder()
            .setComponentType(SatelliteComponent.ComponentType.TSTAT)
            .setRedHighLimit(101)
            .setRedLowLimit(20)
            .setYellowHighLimit(98)
            .setYellowLowLimit(25)
            .build();

        final SatelliteComponent same = SatelliteComponent.newBuilder()
            .setComponentType(SatelliteComponent.ComponentType.TSTAT)
            .setRedHighLimit(101)
            .setRedLowLimit(20)
            .setYellowHighLimit(98)
            .setYellowLowLimit(25)
            .build();

        final SatelliteComponent different = SatelliteComponent.newBuilder()
            .setComponentType(SatelliteComponent.ComponentType.BATT)
            .setRedHighLimit(17)
            .setRedLowLimit(15)
            .setYellowHighLimit(9)
            .setYellowLowLimit(8)
            .build();

        assertEquals(original.hashCode(), same.hashCode());
        assertNotEquals(original.hashCode(), different.hashCode());

        assertEquals(original, same);
        assertNotEquals(original, different);
    }

    @Test
    public void testString() {
        final String expected = "{\n" +
            "  \"componentType\": \"TSTAT\",\n" +
            "  \"redHighLimit\": 101,\n" +
            "  \"redLowLimit\": 20,\n" +
            "  \"yellowHighLimit\": 98,\n" +
            "  \"yellowLowLimit\": 25\n" +
            "}";
        final SatelliteComponent satelliteComponent = SatelliteComponent.newBuilder()
            .setComponentType(SatelliteComponent.ComponentType.TSTAT)
            .setRedHighLimit(101)
            .setRedLowLimit(20)
            .setYellowHighLimit(98)
            .setYellowLowLimit(25)
            .build();

        assertEquals(expected, satelliteComponent.toString());
    }
}
