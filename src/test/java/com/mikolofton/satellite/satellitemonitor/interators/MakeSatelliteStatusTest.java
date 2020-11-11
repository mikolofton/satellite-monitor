package com.mikolofton.satellite.satellitemonitor.interators;

import com.mikolofton.satellite.satellitemonitor.models.SatelliteComponent;
import com.mikolofton.satellite.satellitemonitor.models.SatelliteComponentStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit Tests for {@link MakeSatelliteStatus}.
 */
public class MakeSatelliteStatusTest {

    @Test
    public void testMakeStatus() throws Exception {
        final String testInput = "20180101 23:01:05.001|1001|101|98|25|20|99.9|TSTAT";

        final SatelliteComponentStatus expected = SatelliteComponentStatus.newBuilder()
            .setSatelliteId(1001)
            .setComponent(SatelliteComponent.newBuilder()
                .setComponentType(SatelliteComponent.ComponentType.TSTAT)
                .setRedHighLimit(101)
                .setRedLowLimit(20)
                .setYellowHighLimit(98)
                .setYellowLowLimit(25)
                .build())
            .setValue(99.9)
            .setTimestamp(Instant.parse("2018-01-01T23:01:05.001Z"))
            .build();

        final SatelliteComponentStatus result = new MakeSatelliteStatus("\\|").makeStatus(testInput);

        assertEquals(expected, result);
    }

    public void testMakeStatus_invalid() throws Exception {
        final String testInput = "20180101 23:01:05.001|1001|101|25|20|99.9|TSTAT";

        assertThrows(MakeSatelliteStatus.MakeSatelliteStatusException.class, () ->
            new MakeSatelliteStatus("|").makeStatus(testInput));
    }
}
