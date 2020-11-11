package com.mikolofton.satellite.satellitemonitor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mikolofton.satellite.satellitemonitor.interators.MakeSatelliteAlert;
import com.mikolofton.satellite.satellitemonitor.interators.MakeSatelliteStatus;
import com.mikolofton.satellite.satellitemonitor.interators.MakeSatelliteStatus.MakeSatelliteStatusException;
import com.mikolofton.satellite.satellitemonitor.models.SatelliteComponentAlert;
import com.mikolofton.satellite.satellitemonitor.models.SatelliteComponentStatus;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CLIDriver {

    public static void main(final String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java -jar target/launch-satellite-monitor.jar path/to/file");
            System.exit(0);
        }

        final String filePath = args[0];

        try (final Stream<String> fileStream = Files.lines(Paths.get(filePath))) {

            final Map<Integer, List<SatelliteComponentStatus>> statusesById = fileStream
                .flatMap(line -> {
                    try {
                        return Stream.of(new MakeSatelliteStatus("\\|").makeStatus(line));
                    } catch (MakeSatelliteStatusException e) {
                        // log.error(e);
                        return Stream.empty();
                    }
                })
                .collect(Collectors.groupingBy(s -> s.getSatelliteId()));

            final Set<SatelliteComponentAlert> alerts = new HashSet<>();

            statusesById.entrySet().forEach(statusEntry -> {
                final Set<SatelliteComponentAlert> alertsById = new MakeSatelliteAlert(3)
                    .makeAlerts(statusEntry.getKey(), statusEntry.getValue());

                alerts.addAll(alertsById);
            });

            final Gson gson = new GsonBuilder().setPrettyPrinting().create();
            final String alertsJson = gson.toJson(alerts);

            System.out.println(alertsJson);

        } catch (final Exception e) {
            System.out.println("There was an error processing the file: " + e);
        }
    }
}
