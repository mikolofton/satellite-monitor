package com.mikolofton.satellite.satellitemonitor.interators;

import com.mikolofton.satellite.satellitemonitor.models.SatelliteComponent;
import com.mikolofton.satellite.satellitemonitor.models.SatelliteComponentStatus;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TimeZone;

import static java.util.Objects.requireNonNull;

/**
 * Creates a {@link SatelliteComponentStatus} for a satellite component from data extracted from telemetry logs.
 */
public class MakeSatelliteStatus {

    private final String delimiter;

    /**
     * Creates an instance of {@link MakeSatelliteStatus}.
     *
     * @param delimiter The character used by the satellite's telemetry logs to separate data. (not null)
     */
    public MakeSatelliteStatus(final String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Makes a {@link SatelliteComponentStatus} for a satellite component by extracting and parsing values from
     * a single line of data of the telemetry logs that represents a single {@link SatelliteComponentStatus}.
     *
     * @param input The data containing values to populate a single {@link SatelliteComponentStatus} with. (not null)
     * @return The {@link SatelliteComponentStatus} extracted from the provided data.
     * @throws MakeSatelliteStatusException Thrown if the input cannot be parsed.
     */
    public SatelliteComponentStatus makeStatus(final String input) throws MakeSatelliteStatusException {
        requireNonNull(input);

        final Iterator<String> statusLine = Arrays.stream(input.split(delimiter)).iterator();

        final SatelliteComponentStatus.Builder satelliteStatus = SatelliteComponentStatus.newBuilder();
        final SatelliteComponent.Builder component = SatelliteComponent.newBuilder();

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Timestamp
        if (statusLine.hasNext()) {
            try {
                final Instant timestamp = dateFormat.parse(statusLine.next()).toInstant();

                satelliteStatus.setTimestamp(timestamp);
            } catch (final Exception e) {
                throw new MakeSatelliteStatusException("Unable to create Satellite Status due to the following " +
                    "error: " + e.getMessage(), e);
            }
        }

        // Satellite ID
        if (statusLine.hasNext()) {
            satelliteStatus.setSatelliteId(Integer.parseInt(statusLine.next().trim()));
        }

        // Red High Limit
        if (statusLine.hasNext()) {
            component.setRedHighLimit(Integer.parseInt(statusLine.next().trim()));
        }

        // Yellow High Limit
        if (statusLine.hasNext()) {
            component.setYellowHighLimit(Integer.parseInt(statusLine.next().trim()));
        }

        // Yellow Low Limit
        if (statusLine.hasNext()) {
            component.setYellowLowLimit(Integer.parseInt(statusLine.next().trim()));
        }

        // Red Low Limit
        if (statusLine.hasNext()) {
            component.setRedLowLimit(Integer.parseInt(statusLine.next().trim()));
        }

        // Value
        if (statusLine.hasNext()) {
            satelliteStatus.setValue(Double.parseDouble(statusLine.next().trim()));
        }

        // Component Type
        if (statusLine.hasNext()) {
            component.setComponentType(SatelliteComponent.ComponentType.valueOf(statusLine.next()));
        }

        try {
            return satelliteStatus
                    .setComponent(component.build())
                    .build();
        } catch (final Exception e) {
            throw new MakeSatelliteStatusException("Unable to create Satellite Status due to the following " +
                "error: " + e.getMessage(), e);
        }
    }

    /**
     * An {@link Exception} that indicates when there was an error in {@link MakeSatelliteStatus}.
     */
    public static class MakeSatelliteStatusException extends Exception {
        public MakeSatelliteStatusException(final String msg, final Exception e) {
            super(msg, e);
        }
    }
}
