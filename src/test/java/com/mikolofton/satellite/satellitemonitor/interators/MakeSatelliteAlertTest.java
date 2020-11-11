package com.mikolofton.satellite.satellitemonitor.interators;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mikolofton.satellite.satellitemonitor.models.SatelliteComponent;
import com.mikolofton.satellite.satellitemonitor.models.SatelliteComponentAlert;
import com.mikolofton.satellite.satellitemonitor.models.SatelliteComponentStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link MakeSatelliteAlert}.
 */
public class MakeSatelliteAlertTest {

    private final SatelliteComponent tstatComponent = SatelliteComponent.newBuilder()
        .setComponentType(SatelliteComponent.ComponentType.TSTAT)
        .setRedHighLimit(101)
        .setRedLowLimit(20)
        .setYellowHighLimit(98)
        .setYellowLowLimit(25)
        .build();

    private final SatelliteComponent battComponent = SatelliteComponent.newBuilder()
        .setComponentType(SatelliteComponent.ComponentType.BATT)
        .setRedHighLimit(17)
        .setRedLowLimit(8)
        .setYellowHighLimit(15)
        .setYellowLowLimit(9)
        .build();

    @Test
    public void testMakeAlerts() throws Exception {
        final List<SatelliteComponentStatus> statuses = ImmutableList.of(
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(battComponent)
                .setValue(7.8)
                .setTimestamp(Instant.parse("2018-01-01T23:01:09.521Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(tstatComponent)
                .setValue(102.9)
                .setTimestamp(Instant.parse("2018-01-01T23:01:38.001Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(tstatComponent)
                .setValue(87.9)
                .setTimestamp(Instant.parse("2018-01-01T23:01:49.021Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(battComponent)
                .setValue(7.7)
                .setTimestamp(Instant.parse("2018-01-01T23:02:11.302Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(tstatComponent)
                .setValue(102.7)
                .setTimestamp(Instant.parse("2018-01-01T23:03:03.008Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(tstatComponent)
                .setValue(101.2)
                .setTimestamp(Instant.parse("2018-01-01T23:03:05.009Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(battComponent)
                .setValue(7.9)
                .setTimestamp(Instant.parse("2018-01-01T23:04:11.531Z"))
                .build());

        final Set<SatelliteComponentAlert> expected = Sets.newHashSet(
            SatelliteComponentAlert.newBuilder()
                .setSatelliteId(1000)
                .setComponent(SatelliteComponent.ComponentType.TSTAT.name())
                .setSeverity(SatelliteComponentAlert.Severity.RED_HIGH.toString())
                .setTimestamp("2018-01-01T23:01:38.001Z")
                .build(),

            SatelliteComponentAlert.newBuilder()
                .setSatelliteId(1000)
                .setComponent(SatelliteComponent.ComponentType.BATT.name())
                .setSeverity(SatelliteComponentAlert.Severity.RED_LOW.toString())
                .setTimestamp("2018-01-01T23:01:09.521Z")
                .build());

        final Set<SatelliteComponentAlert> result = new MakeSatelliteAlert(3)
            .makeAlerts(1000, statuses);

        assertEquals(expected, result);
    }

    @Test
    public void testMakeAlerts_none() throws Exception {
        final List<SatelliteComponentStatus> statuses = ImmutableList.of(
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1001)
                .setComponent(tstatComponent)
                .setValue(99.9)
                .setTimestamp(Instant.parse("2018-01-01T23:01:05.001Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1001)
                .setComponent(tstatComponent)
                .setValue(99.8)
                .setTimestamp(Instant.parse("2018-01-01T23:01:26.011Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1001)
                .setComponent(tstatComponent)
                .setValue(89.3)
                .setTimestamp(Instant.parse("2018-01-01T23:02:09.014Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1001)
                .setComponent(tstatComponent)
                .setValue(89.4)
                .setTimestamp(Instant.parse("2018-01-01T23:02:10.021Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1001)
                .setComponent(tstatComponent)
                .setValue(102.7)
                .setTimestamp(Instant.parse("2018-01-01T23:03:03.008Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1001)
                .setComponent(tstatComponent)
                .setValue(101.2)
                .setTimestamp(Instant.parse("2018-01-01T23:03:05.009Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1001)
                .setComponent(tstatComponent)
                .setValue(89.9)
                .setTimestamp(Instant.parse("2018-01-01T23:04:06.017Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1001)
                .setComponent(tstatComponent)
                .setValue(89.9)
                .setTimestamp(Instant.parse("2018-01-01T23:05:05.021Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1001)
                .setComponent(battComponent)
                .setValue(7.9)
                .setTimestamp(Instant.parse("2018-01-01T23:05:07.421Z"))
                .build());

        final Set<SatelliteComponentAlert> result = new MakeSatelliteAlert(3)
            .makeAlerts(1000, statuses);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetInterval() {
        assertEquals(1604843400, new MakeSatelliteAlert(3).getInterval(1604843540));
    }

    @Test
    public void testGetAlertTimestamp_thermostat() {
        final List<SatelliteComponentStatus> statuses = ImmutableList.of(
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(tstatComponent)
                .setValue(102.9)
                .setTimestamp(Instant.parse("2018-01-01T23:01:38.001Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(tstatComponent)
                .setValue(87.9)
                .setTimestamp(Instant.parse("2018-01-01T23:01:49.021Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(tstatComponent)
                .setValue(102.7)
                .setTimestamp(Instant.parse("2018-01-01T23:03:03.008Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(tstatComponent)
                .setValue(101.2)
                .setTimestamp(Instant.parse("2018-01-01T23:03:05.009Z"))
                .build());

        final Optional<Instant> expected = Optional.of(Instant.parse("2018-01-01T23:01:38.001Z"));

        final Optional<Instant> result = new MakeSatelliteAlert(3)
            .getAlertTimestamp(SatelliteComponent.ComponentType.TSTAT, statuses);

        assertEquals(expected, result);
    }

    @Test
    public void testGetAlertTimestamp_thermostat_none() {
        final List<SatelliteComponentStatus> statuses = ImmutableList.of(
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(tstatComponent)
                .setValue(102.9)
                .setTimestamp(Instant.parse("2018-01-01T23:01:38.001Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(tstatComponent)
                .setValue(87.9)
                .setTimestamp(Instant.parse("2018-01-01T23:01:49.021Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(tstatComponent)
                .setValue(72.7)
                .setTimestamp(Instant.parse("2018-01-01T23:03:03.008Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(tstatComponent)
                .setValue(71.2)
                .setTimestamp(Instant.parse("2018-01-01T23:03:05.009Z"))
                .build());

        final Optional<Instant> result = new MakeSatelliteAlert(3)
            .getAlertTimestamp(SatelliteComponent.ComponentType.TSTAT, statuses);

        assertFalse(result.isPresent());
    }

    @Test
    public void testGetAlertTimestamp_battery() {
        final List<SatelliteComponentStatus> statuses = ImmutableList.of(
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(battComponent)
                .setValue(7.8)
                .setTimestamp(Instant.parse("2018-01-01T23:01:09.521Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(battComponent)
                .setValue(7.7)
                .setTimestamp(Instant.parse("2018-01-01T23:02:11.302Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(battComponent)
                .setValue(7.9)
                .setTimestamp(Instant.parse("2018-01-01T23:04:11.531Z"))
                .build());

        final Optional<Instant> expected = Optional.of(Instant.parse("2018-01-01T23:01:09.521Z"));

        final Optional<Instant> result = new MakeSatelliteAlert(3)
            .getAlertTimestamp(SatelliteComponent.ComponentType.BATT, statuses);

        assertEquals(expected, result);
    }

    @Test
    public void testGetAlertTimestamp_battery_none() {
        final List<SatelliteComponentStatus> statuses = ImmutableList.of(
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(battComponent)
                .setValue(7.8)
                .setTimestamp(Instant.parse("2018-01-01T23:01:09.521Z"))
                .build(),
            SatelliteComponentStatus.newBuilder()
                .setSatelliteId(1000)
                .setComponent(battComponent)
                .setValue(7.7)
                .setTimestamp(Instant.parse("2018-01-01T23:02:11.302Z"))
                .build());

        final Optional<Instant> result = new MakeSatelliteAlert(3)
            .getAlertTimestamp(SatelliteComponent.ComponentType.BATT, statuses);

        assertFalse(result.isPresent());
    }
}
