package com.fradot.exercise.airline.seats.manager.reader;

import com.fradot.exercise.airline.seats.manager.model.Plane;
import com.fradot.exercise.airline.seats.manager.model.Traveler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class that handles flight data reading from file
 */
public class FlightDataReader {

    private static final int ROWS = 1;
    private static final int NUMBER_OF_SEATS = 0;

    private final String filePath;
    private List<List<Traveler>> groupBookingsList = new ArrayList<>();
    private Plane plane;

    public FlightDataReader(final String filePath) throws FileNotFoundException {
        this.filePath = filePath;
    }

    public void readData() throws FileNotFoundException {
        this.plane = readPlaneDimensions();
        this.groupBookingsList = readBookingsList();
    }

    Plane readPlaneDimensions() throws FileNotFoundException {
        final Scanner scanner = new Scanner(new File(filePath));
        final int[] data = new int[2];
        int count = 0;

        try {
            while (scanner.hasNext() && count < 2) {
                data[count] = scanner.nextInt();
                count++;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format("Error while reading Plane dimensions on file %s", filePath), e);
        }

        if (count != 2) {
            throw new IllegalArgumentException(
                    String.format("Error reading Plane dimensions on File %s is empty", filePath));
        }

        return new Plane(data[ROWS], data[NUMBER_OF_SEATS]);
    }

    List<List<Traveler>> readBookingsList() throws FileNotFoundException {
        final Scanner scanner = new Scanner(new File(filePath));
        final List<List<Traveler>> bookingsList = new ArrayList<>();
        int count = 0;
        try {
            while (scanner.hasNextLine()) {
                final String row = scanner.nextLine();

                // skip first line
                if (count >= 1) {
                    final String[] ids = row.split(" ");
                    final List<Traveler> group = new ArrayList<>(ids.length);
                    for (String travelerId : ids) {
                        group.add(new Traveler(travelerId));
                    }
                    bookingsList.add(group);
                }
                count++;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format("Error while reading bookings list on file %s", filePath), e);
        }

        return bookingsList;
    }

    public List<List<Traveler>> getGroupBookingsList() {
        return groupBookingsList;
    }

    public Plane getPlane() {
        return plane;
    }
}
