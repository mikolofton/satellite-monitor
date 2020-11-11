package com.mikolofton.satellite.satellitemonitor.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Instant;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * An alert created for a {@link SatelliteComponent} when the {@link SatelliteComponentStatus} meets the
 * required alert conditions.
 */
public class SatelliteComponentAlert {

    private final int satelliteId;
    private final String severity;
    private final String component;
    private final String timestamp;

    /**
     * Represents the possible severity values for an alert.
     */
    public enum Severity {
        RED_LOW("RED LOW"),
        RED_HIGH("RED HIGH");

        private final String name;

        Severity(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    /**
     * Private to prevent instantiation. Use {@link Builder} to create instances.
     *
     * @param satelliteId The identifier of the satellite the {@link SatelliteComponent} belongs to.
     * @param severity The string representation of {@link Severity} that triggered the alert. (not null)
     * @param component The string representation of the {@link SatelliteComponent.ComponentType} the alert is
     *    for. (not null)
     * @param timestamp The timestamp of the first {@link SatelliteComponentStatus} alert trigger. (not null)
     */
    private SatelliteComponentAlert(
            final int satelliteId,
            final String severity,
            final String component,
            final String timestamp) {
        checkArgument(satelliteId > 0, "The satellite id must be greater than 0.");
        requireNonNull(component);
        requireNonNull(severity);
        requireNonNull(timestamp);

        this.satelliteId = satelliteId;
        this.component = component;
        this.severity = severity;
        this.timestamp = timestamp;
    }

    /**
     * @return The identifier of the satellite the {@link SatelliteComponent} belongs to.
     */
    public int getSatelliteId() {
        return satelliteId;
    }

    /**
     * @return The string representation of {@link Severity} that triggered the alert.
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * @return The string representation of the {@link SatelliteComponent.ComponentType} the alert is for.
     */
    public String getComponent() {
        return component;
    }

    /**
     * @return The timestamp of the first {@link SatelliteComponentStatus} alert trigger.
     */
    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final SatelliteComponentAlert other = (SatelliteComponentAlert) o;

        return satelliteId == other.satelliteId &&
                Objects.equals(severity, other.severity) &&
                Objects.equals(component, other.component) &&
                Objects.equals(timestamp, other.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(satelliteId, severity, component, timestamp);
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
     * Creates instances of {@link SatelliteComponentAlert}.
     */
    public static class Builder {
        private int satelliteId;
        private String severity;
        private String component;
        private String timestamp;

        /**
         * Private constructor to prevent instantiation.
         */
        private Builder() { }

        /**
         * @param satelliteId The identifier of the satellite the {@link SatelliteComponent} belongs to.
         * @return The current instance of {@link SatelliteComponentStatus.Builder} to chain methods.
         */
        public Builder setSatelliteId(final int satelliteId) {
            this.satelliteId = satelliteId;
            return this;
        }

        /**
         * @param severity The string representation of {@link Severity} that triggered the alert.
         * @return The current instance of {@link SatelliteComponentStatus.Builder} to chain methods.
         */
        public Builder setSeverity(final String severity) {
            this.severity = severity;
            return this;
        }

        /**
         * @param component The string representation of the {@link SatelliteComponent.ComponentType} the alert is
         *    for.
         * @return The current instance of {@link SatelliteComponentStatus.Builder} to chain methods.
         */
        public Builder setComponent(final String component) {
            this.component = component;
            return this;
        }

        /**
         * @param timestamp The timestamp of the first {@link SatelliteComponentStatus} alert trigger.
         * @return The current instance of {@link SatelliteComponentStatus.Builder} to chain methods.
         */
        public Builder setTimestamp(final String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * @return An instance of {@link SatelliteComponentAlert} using this {@link Builder}'s values.
         */
        public SatelliteComponentAlert build() {
            return new SatelliteComponentAlert(satelliteId, severity, component, timestamp);
        }
    }
}
