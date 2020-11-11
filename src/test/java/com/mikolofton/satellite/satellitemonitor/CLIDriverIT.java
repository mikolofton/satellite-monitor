package com.mikolofton.satellite.satellitemonitor;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for satellite monitor's {@link CLIDriver}.
 */
public class CLIDriverIT {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream sysOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(sysOut);
    }

    @Test
    public void testSatelliteMonitor() {
        String filePath = Objects.requireNonNull(getClass().getClassLoader().getResource("satellite.log")).getPath();
        String[] arguments = new String[] {filePath};

        CLIDriver.main(arguments);

        final String expected = "[\n" +
            "  {\n" +
            "    \"satelliteId\": 1000,\n" +
            "    \"severity\": \"RED HIGH\",\n" +
            "    \"component\": \"TSTAT\",\n" +
            "    \"timestamp\": \"2018-01-01T23:01:38.001Z\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"satelliteId\": 1000,\n" +
            "    \"severity\": \"RED LOW\",\n" +
            "    \"component\": \"BATT\",\n" +
            "    \"timestamp\": \"2018-01-01T23:01:09.521Z\"\n" +
            "  }\n" +
            "]";

        assertEquals(expected, outContent.toString().trim());
    }
}