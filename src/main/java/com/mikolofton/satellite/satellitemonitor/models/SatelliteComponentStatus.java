package com.mikolofton.satellite.satellitemonitor.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Instant;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * The status data for a {@link SatelliteComponent} extracted from telemetry logs.
 */
public class SatelliteComponentStatus {

    private final int satelliteId;
    private final SatelliteComponent component;
    private final double value;
    private final Instant timestamp;

    /**
     * Private to prevent instantiation. Use {@link Builder} to create instances.
     *
     * @param satelliteId The identifier of the satellite.
     * @param component The {@link SatelliteComponent} this status belongs to. (not null)
     * @param value The value recorded for the {@link SatelliteComponent} at a specific time.
     * @param timestamp The timestamp when the value was recorded. (not null)
     */
    private SatelliteComponentStatus(
            final int satelliteId,
            final SatelliteComponent component,
            final double value,
            final Instant timestamp) {
        checkArgument(satelliteId > 0, "The satellite id must be greater than 0.");
        requireNonNull(component);
        requireNonNull(satelliteId);
        checkArgument(value > 0, "The satellite status value must be greater than 0.");
        requireNonNull(timestamp);

        this.satelliteId = satelliteId;
        this.component = component;
        this.value = value;
        this.timestamp = timestamp;
    }

    /**
     * @return The identifier of the satellite.
     */
    public int getSatelliteId() {
        return satelliteId;
    }

    /**
     * @return The {@link SatelliteComponent} this status belongs to.
     */
    public SatelliteComponent getComponent() {
        return component;
    }

    /**
     * @return The value recorded for the {@link SatelliteComponent} at a specific time.
     */
    public double getValue() {
        return value;
    }

    /**
     * @return The timestamp when the value was recorded.
     */
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final SatelliteComponentStatus other = (SatelliteComponentStatus) o;

        return satelliteId == other.satelliteId &&
                Objects.equals(component, other.component) &&
                Double.compare(value, other.value) == 0 &&
                Objects.equals(timestamp, other.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(satelliteId, component, value, timestamp);
    }

    @Override
    public String toString() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    /**
     * @return Creates a new instance of {@link Builder}.
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Creates instances of {@link SatelliteComponentStatus}.
     */
    public static class Builder {
        private int satelliteId;
        private SatelliteComponent component;
        private double value;
        private Instant timestamp;

        /**
         * Private constructor to prevent instantiation.
         */
        private Builder() { }

        /**
         * @param satelliteId The identifier of the satellite.
         * @return The current instance of {@link Builder} to chain methods.
         */
        public Builder setSatelliteId(final int satelliteId) {
            this.satelliteId = satelliteId;
            return this;
        }

        /**
         * @param component The {@link SatelliteComponent} to use for this status.
         * @return The current instance of {@link Builder} to chain methods.
         */
        public Builder setComponent(final SatelliteComponent component) {
            this.component = component;
            return this;
        }

        /**
         * @param value The value recorded for the {@link SatelliteComponent}.
         * @return The current instance of {@link Builder} to chain methods.
         */
        public Builder setValue(final double value) {
            this.value = value;
            return this;
        }

        /**
         * @param timestamp The timestamp when the value was recorded.
         * @return The current instance of {@link Builder} to chain methods.
         */
        public Builder setTimestamp(final Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * @return An instance of {@link SatelliteComponentStatus} using this {@link Builder}'s values.
         */
        public SatelliteComponentStatus build() {
            return new SatelliteComponentStatus(satelliteId, component, value, timestamp);
        }
    }
}
