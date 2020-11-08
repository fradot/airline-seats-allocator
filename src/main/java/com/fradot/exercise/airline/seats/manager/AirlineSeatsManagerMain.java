package com.fradot.exercise.airline.seats.manager;

import com.fradot.exercise.airline.seats.manager.reader.FlightDataReader;
import com.fradot.exercise.airline.seats.manager.service.SeatsAllocatorException;
import com.fradot.exercise.airline.seats.manager.service.SeatsAllocatorService;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AirlineSeatsManagerMain {

    public static void main(String... args) {
        if (args != null && args.length == 1) {
            final String filepath = args[0];
            try {
                final FlightDataReader flightDataReader = new FlightDataReader(filepath);
                flightDataReader.readData();

                final SeatsAllocatorService seatsAllocatorService =
                        new SeatsAllocatorService(flightDataReader.getGroupBookingsList(), flightDataReader.getPlane());

                seatsAllocatorService.allocateFlight();
                final Long[][] output = seatsAllocatorService.generateAllocatedFlightMatrix();
                final double travellersSatisfaction = seatsAllocatorService.getTravellersSatisfaction();
                printOutputMatrix(output, travellersSatisfaction);
            } catch (FileNotFoundException e) {
                System.err.println("ERROR: file not found!");
            } catch (SeatsAllocatorException e) {
                System.err.println(String.format("ERROR: allocating seats: %s", e.getMessage()));
            } catch (Exception e) {
                System.err.println(String.format("ERROR: %s", e.getMessage()));
            }
        } else {
            System.err.println("ERROR: Please enter a file path as argument.");
        }
    }

    private static void printOutputMatrix(final Long[][] output, final double travellersSatisfaction) {
        for (int i = 0; i < output.length; i++) {
            List<Long> ids = Arrays.asList(output[i]);
            System.out.println(
                    String.join(" ", ids.stream().map(String::valueOf).collect(Collectors.toList())));
        }
        System.out.println(round(travellersSatisfaction ,2) + "%");
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
