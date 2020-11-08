package com.fradot.exercise.airline.seats.manager.reader;

import com.fradot.exercise.airline.seats.manager.model.Plane;
import com.fradot.exercise.airline.seats.manager.model.Traveler;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;

public class FlightDataReaderTest {

    @Test
    public void readPlaneDimensions_itShouldReadPlaneDataCorrectly() throws FileNotFoundException {
        final String fileName = "flight_data.txt";
        final ClassLoader classLoader = getClass().getClassLoader();
        final URL resource = classLoader.getResource(fileName);
        final FlightDataReader flightDataReader = new FlightDataReader(resource.getPath());
        final Plane plane = flightDataReader.readPlaneDimensions();
        assertEquals(4, plane.getRows());
        assertEquals(5, plane.getSeatsPerRow());
    }

    @Test(expected = IllegalArgumentException.class)
    public void readPlaneDimensions_itShouldThrowAnExceptionIfDataIsMissingFromFile() throws FileNotFoundException {
        final String fileName = "flight_data_missing_rows.txt";
        final ClassLoader classLoader = getClass().getClassLoader();
        final URL resource = classLoader.getResource(fileName);
        final FlightDataReader flightDataReader = new FlightDataReader(resource.getPath());
        flightDataReader.readPlaneDimensions();
    }

    @Test
    public void readPlaneDimensions_itShouldReadBookingsListDataCorrectly() throws FileNotFoundException {
        final String fileName = "flight_data.txt";
        final ClassLoader classLoader = getClass().getClassLoader();
        final URL resource = classLoader.getResource(fileName);
        final FlightDataReader flightDataReader = new FlightDataReader(resource.getPath());
        final List<List<Traveler>> travelerList = flightDataReader.readBookingsList();
        assertThat(travelerList, is(not(nullValue())));
        assertThat(travelerList, hasSize(7));
        assertThat(travelerList.get(2).stream().map(Traveler::getId).collect(Collectors.toList()), hasItems("8"));
        assertThat(
                travelerList.get(6).stream().map(Traveler::getId).collect(Collectors.toList()), hasItems("15", "16"));
    }
}
