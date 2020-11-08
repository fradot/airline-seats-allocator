package com.fradot.exercise.airline.seats.manager;

import com.fradot.exercise.airline.seats.manager.model.Plane;
import com.fradot.exercise.airline.seats.manager.reader.FlightDataReader;
import com.fradot.exercise.airline.seats.manager.service.SeatsAllocatorException;
import com.fradot.exercise.airline.seats.manager.service.SeatsAllocatorService;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.net.URL;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class AirlineSeatsManagerIT {

    @Test
    public void itShouldAllocateASeatsMatrixBasedOnTheInputFile()
            throws FileNotFoundException, SeatsAllocatorException {
        final String fileName = "flight_data_4rows_4columns.txt";
        final ClassLoader classLoader = getClass().getClassLoader();
        final URL resource = classLoader.getResource(fileName);
        final FlightDataReader flightDataReader = new FlightDataReader(resource.getPath());
        flightDataReader.readData();

        final SeatsAllocatorService seatsAllocatorService =
                new SeatsAllocatorService(flightDataReader.getGroupBookingsList(), flightDataReader.getPlane());

        seatsAllocatorService.allocateFlight();
        final Long[][] output = seatsAllocatorService.generateAllocatedFlightMatrix();
        final double travellersSatisfaction = seatsAllocatorService.getTravellersSatisfaction();

        assertEquals(100, travellersSatisfaction, 0.01);
        assertArrayEquals(
                new Long[][] {
                    {1L, 2L, 3L, 8L},
                    {4L, 5L, 6L, 7L},
                    {11L, 10L, 9L, 12L},
                    {13L, 14L, 15L, 16L}
                },
                output);
    }

    @Test(expected = SeatsAllocatorException.class)
    public void itShouldThrowAnExceptionWhenGeneratingOutputMatrixIfNoSeatsWereAllocated()
            throws FileNotFoundException, SeatsAllocatorException {
        final String fileName = "flight_no_seats_generated.txt";
        final ClassLoader classLoader = getClass().getClassLoader();
        final URL resource = classLoader.getResource(fileName);
        final FlightDataReader flightDataReader = new FlightDataReader(resource.getPath());
        flightDataReader.readData();

        final SeatsAllocatorService seatsAllocatorService =
                new SeatsAllocatorService(flightDataReader.getGroupBookingsList(), flightDataReader.getPlane());

        seatsAllocatorService.allocateFlight();
        final Long[][] output = seatsAllocatorService.generateAllocatedFlightMatrix();
    }
}
