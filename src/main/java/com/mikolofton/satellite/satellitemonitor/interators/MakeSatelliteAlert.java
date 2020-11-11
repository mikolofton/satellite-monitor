package com.mikolofton.satellite.satellitemonitor.interators;

import com.mikolofton.satellite.satellitemonitor.models.SatelliteComponentAlert;
import com.mikolofton.satellite.satellitemonitor.models.SatelliteComponent;
import com.mikolofton.satellite.satellitemonitor.models.SatelliteComponentStatus;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Creates {@link SatelliteComponentAlert} based on whether there are the threshold amount of recorded
 * {@link SatelliteComponentStatus}s that have values that are above the red high limit or below the red low limit
 * within 5 minutes of each other.
 */
public class MakeSatelliteAlert {

    private final int threshold;

    /**
     * Creates an instance of {@link MakeSatelliteAlert}.
     *
     * @param threshold The amount of {@link SatelliteComponentStatus}s with values that are above the red high limit
     *     or below the red low limit that will create a {@link SatelliteComponentAlert}.
     */
    public MakeSatelliteAlert(int threshold) {
        this.threshold = threshold;
    }

    /**
     * Makes a {@link SatelliteComponentAlert} for a specific satelliteId by aggregating
     * {@link SatelliteComponentStatus}s by component over five minute intervals and determining whether there are
     * enough {@link SatelliteComponentStatus}s with values that are above the red high limit or below the red low
     * limit.
     *
     * @param satelliteId The identifier of the satellite whose {@link SatelliteComponentStatus}s to use. (not null)
     * @param statuses The recorded {@link SatelliteComponentStatus} for the satellite. (not null)
     * @return The {@link SatelliteComponentAlert} made based on the {@link SatelliteComponentStatus}s.
     */
    public Set<SatelliteComponentAlert> makeAlerts(final int satelliteId, final List<SatelliteComponentStatus> statuses) {
        requireNonNull(satelliteId);
        requireNonNull(statuses);

        // Get all statuses for a component and group by five minute intervals.
        final Map<Long, List<SatelliteComponentStatus>> batteryStatuses = statuses.stream()
                .filter(s -> s.getComponent().getComponentType() == SatelliteComponent.ComponentType.BATT)
                .collect(Collectors.groupingBy(s -> getInterval(s.getTimestamp().getEpochSecond())));

        final Map<Long, List<SatelliteComponentStatus>> thermostatStatuses = statuses.stream()
                .filter(s -> s.getComponent().getComponentType() == SatelliteComponent.ComponentType.TSTAT)
                .collect(Collectors.groupingBy(s -> getInterval(s.getTimestamp().getEpochSecond())));

        final Set<SatelliteComponentAlert> alerts = new HashSet<>();

        // Handle all battery component statuses.
        batteryStatuses.values().forEach(bStat -> {
            final SatelliteComponentAlert.Builder alert = SatelliteComponentAlert.newBuilder();
            final Optional<Instant> alertTimestamp = getAlertTimestamp(SatelliteComponent.ComponentType.BATT, bStat);

            // If a timestamp is present, then an alert must be created for the timestamp.
            alertTimestamp.ifPresent(ts -> alerts.add(alert.setSatelliteId(satelliteId)
                    .setComponent(SatelliteComponent.ComponentType.BATT.name())
                    .setSeverity(SatelliteComponentAlert.Severity.RED_LOW.toString())
                    .setTimestamp(ts.toString())
                    .build()));
        });

        // Handle all thermostat component statues.
        thermostatStatuses.values().forEach(tStat -> {
            final SatelliteComponentAlert.Builder alert = SatelliteComponentAlert.newBuilder();
            final Optional<Instant> alertTimestamp = getAlertTimestamp(SatelliteComponent.ComponentType.TSTAT, tStat);

            // If a timestamp is present, then an alert must be created for the timestamp.
            alertTimestamp.ifPresent(ts -> alerts.add(alert.setSatelliteId(satelliteId)
                    .setComponent(SatelliteComponent.ComponentType.TSTAT.name())
                    .setSeverity(SatelliteComponentAlert.Severity.RED_HIGH.toString())
                    .setTimestamp(ts.toString())
                    .build()));
        });

        return alerts;
    }

    /**
     * Computes the five minute interval of a given time in seconds.
     *
     * @param timeSec The time to compute the interval for in seconds.
     * @return The time of the five minute interval in seconds.
     */
    public long getInterval(final long timeSec) {
        return timeSec - (timeSec % TimeUnit.MINUTES.toSeconds(5));
    }

    /**
     * Determines whether an {@link SatelliteComponentAlert} must be made for a satellite component by counting the
     * amount of {@link SatelliteComponentAlert}s with values that are above the red high limit or below the red low
     * limit based on the component type. If an alert must be made, the timestamp of the first
     * {@link SatelliteComponentStatus} counted is returned to create the alert for.
     *
     * @param componentType The {@link SatelliteComponent.ComponentType} whose statuses are provided. (not null)
     * @param satelliteStatuses A list of {@link SatelliteComponentStatus}s within a five minute interval. (not null)
     * @return If a {@link SatelliteComponentAlert} must be created, the timestamp to create the alert for or an
     *     empty optional is no alert is to be made.
     */
    public Optional<Instant> getAlertTimestamp(
            final SatelliteComponent.ComponentType componentType,
            final List<SatelliteComponentStatus> satelliteStatuses) {
        long alertCount = 0;
        Optional<Instant> firstTimestamp = Optional.empty();

        for (final SatelliteComponentStatus status : satelliteStatuses) {
            final boolean battCondition = componentType == SatelliteComponent.ComponentType.BATT &&
                    status.getValue() < status.getComponent().getRedLowLimit();
            final boolean tstatCondition = componentType == SatelliteComponent.ComponentType.TSTAT &&
                    status.getValue() > status.getComponent().getRedHighLimit();

            if (battCondition || tstatCondition) {
                alertCount++;
                if (!firstTimestamp.isPresent()) {
                    firstTimestamp = Optional.of(status.getTimestamp());
                }
            }
        }

        if (alertCount >= threshold && firstTimestamp.isPresent()) {
            return firstTimestamp;
        } else {
            return Optional.empty();
        }
    }
}
