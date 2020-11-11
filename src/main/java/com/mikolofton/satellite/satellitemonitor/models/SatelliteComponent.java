package com.mikolofton.satellite.satellitemonitor.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * A component of a satellite and its limit configurations.
 */
public class SatelliteComponent {
    private final ComponentType componentType;
    private final int redHighLimit;
    private final int redLowLimit;
    private final int yellowHighLimit;
    private final int yellowLowLimit;

    /**
     * Private to prevent instantiation. Use {@link Builder} to create instances.
     *
     * @param componentType The type of component of a satellite. (not null)
     * @param redHighLimit The upper limit of when this component is considered to be red.
     * @param redLowLimit The lower limit of when this component is considered to be red.
     * @param yellowHighLimit The upper limit of when this component is considered to be yellow.
     * @param yellowLowLimit The lower limit of when this component is considered to be yellow.
     */
    private SatelliteComponent(
            final ComponentType componentType,
            final int redHighLimit,
            final int redLowLimit,
            final int yellowHighLimit,
            final int yellowLowLimit) {
        requireNonNull(componentType);
        checkArgument(redHighLimit > 0, "The red high limit for the satellite component must be greater than 0.");
        checkArgument(redLowLimit > 0, "The red high limit for the satellite component must be greater than 0.");
        checkArgument(yellowHighLimit > 0, "The red high limit for the satellite component must be greater than 0.");
        checkArgument(yellowLowLimit > 0, "The red high limit for the satellite component must be greater than 0.");
        checkArgument(redHighLimit > redLowLimit, ("The red high limit for the satellite component must be greater " +
                "than the red low limit"));
        checkArgument(yellowHighLimit > yellowLowLimit, ("The yellow high limit for the satellite component must be greater " +
                "than the yellow low limit"));

        this.componentType = componentType;
        this.redHighLimit = redHighLimit;
        this.redLowLimit = redLowLimit;
        this.yellowHighLimit = yellowHighLimit;
        this.yellowLowLimit = yellowLowLimit;
    }

    /**
     * Represents the different component types of a satellite.
     *
     * BATT - Battery
     * TSTAT - Thermostat
     */
    public enum ComponentType {
        BATT, TSTAT
    }

    /**
     * @return The type of component of a satellite.
     */
    public ComponentType getComponentType() {
        return componentType;
    }

    /**
     * @return The upper limit of when this component is considered to be red.
     */
    public int getRedHighLimit() {
        return redHighLimit;
    }

    /**
     * @return The lower limit of when this component is considered to be red.
     */
    public int getRedLowLimit() {
        return redLowLimit;
    }

    /**
     * @return The upper limit of when this component is considered to be yellow.
     */
    public int getYellowHighLimit() {
        return yellowHighLimit;
    }

    /**
     * @return The lower limit of when this component is considered to be yellow.
     */
    public int getYellowLowLimit() {
        return yellowLowLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final SatelliteComponent other = (SatelliteComponent) o;

        return redHighLimit == other.redHighLimit &&
                redLowLimit == other.redLowLimit &&
                yellowHighLimit == other.yellowHighLimit &&
                yellowLowLimit == other.yellowLowLimit &&
                componentType == other.componentType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(componentType, redHighLimit, redLowLimit, yellowHighLimit, yellowLowLimit);
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
     * Creates instances of {@link SatelliteComponent}.
     */
    public static class Builder {
        private ComponentType componentType;
        private int redHighLimit;
        private int redLowLimit;
        private int yellowHighLimit;
        private int yellowLowLimit;

        /**
         * Private constructor to prevent instantiation.
         */
        private Builder() { }

        /**
         * @param componentType The type of component of a satellite.
         * @return The current instance of {@link Builder} to chain methods.
         */
        public Builder setComponentType(final ComponentType componentType) {
            this.componentType = componentType;
            return this;
        }

        /**
         * @param redHighLimit The upper limit of when this component is considered to be red.
         * @return The current instance of {@link Builder} to chain methods.
         */
        public Builder setRedHighLimit(final int redHighLimit) {
            this.redHighLimit = redHighLimit;
            return this;
        }

        /**
         * @param redLowLimit The lower limit of when this component is considered to be red.
         * @return The current instance of {@link Builder} to chain methods.
         */
        public Builder setRedLowLimit(final int redLowLimit) {
            this.redLowLimit = redLowLimit;
            return this;
        }

        /**
         * @param yellowHighLimit The upper limit of when this component is considered to be yellow.
         * @return The current instance of {@link Builder} to chain methods.
         */
        public Builder setYellowHighLimit(final int yellowHighLimit) {
            this.yellowHighLimit = yellowHighLimit;
            return this;
        }

        /**
         * @param yellowLowLimit The lower limit of when this component is considered to be yellow.
         * @return The current instance of {@link Builder} to chain methods.
         */
        public Builder setYellowLowLimit(final int yellowLowLimit) {
            this.yellowLowLimit = yellowLowLimit;
            return this;
        }

        /**
         * @return An instance of {@link SatelliteComponent} using this {@link Builder}'s values.
         */
        public SatelliteComponent build() {
            return new SatelliteComponent(componentType, redHighLimit, redLowLimit, yellowHighLimit, yellowLowLimit);
        }
    }
}

