package com.fradot.exercise.airline.seats.manager;

import com.fradot.exercise.airline.seats.manager.model.Traveler;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.fradot.exercise.airline.seats.manager.TestUtil.*;
import static junit.framework.TestCase.assertEquals;

public class SeatsAllocatorAlgorithmTest {

    @Test
    public void calculateTravellersSatisfaction_itShouldCorrectlyMeasureTravellersSatisfaction() {
        final List<List<String>> travellersGroups = new ArrayList<List<String>>();
        travellersGroups.add(generateTravellersIdList("1W", "2", "3" , ""));
        travellersGroups.add(generateTravellersIdList("4W", "5", "6W", "7W"));
        final String[][] seatsMatrix = generateSeatsMatrixFromTravellersGroups(travellersGroups);
        final List<List<Traveler>> travellersList = generateTravellersListFromSeats(seatsMatrix);
        final SeatsAllocatorAlgorithm seatsAllocatorAlgorithm =
                new SeatsAllocatorAlgorithm(travellersList, seatsMatrix);

        assertEquals(92.85, seatsAllocatorAlgorithm.calculateTravellersSatisfaction(), 0.01);
    }

    @Test
    public void calculateTravellersSatisfaction_itShouldReturnZeroIfMatrixIsNull() {
        final List<List<String>> travellersGroups = new ArrayList<List<String>>();
        travellersGroups.add(generateTravellersIdList("1W", "2", "3" , ""));
        travellersGroups.add(generateTravellersIdList("4W", "5", "6W", "7W"));
        final String[][] seatsMatrix = generateSeatsMatrixFromTravellersGroups(travellersGroups);
        final List<List<Traveler>> travellersList = generateTravellersListFromSeats(seatsMatrix);
        final SeatsAllocatorAlgorithm seatsAllocatorAlgorithm =
                new SeatsAllocatorAlgorithm(travellersList, null);

        assertEquals(0.0, seatsAllocatorAlgorithm.calculateTravellersSatisfaction());
    }

    @Test(expected = IllegalStateException.class)
    public void calculateTravellersSatisfaction_itShouldThrowAnExceptionIfTravellersAreNotInExpectedPosition() {
        final List<List<String>> travellersGroups = new ArrayList<List<String>>();
        travellersGroups.add(generateTravellersIdList("1W", "2", "3" , ""));
        travellersGroups.add(generateTravellersIdList("4W", "5", "6W", "7W"));
        final String[][] seatsMatrix = generateSeatsMatrixFromTravellersGroups(travellersGroups);
        final List<List<Traveler>> travellersList = generateTravellersListFromSeats(seatsMatrix);

        // switching position of an element in the travellers list
        travellersList.get(0).get(0).setRow(1);
        travellersList.get(0).get(0).setColumn(1);

        final SeatsAllocatorAlgorithm seatsAllocatorAlgorithm =
                new SeatsAllocatorAlgorithm(travellersList, seatsMatrix);

        seatsAllocatorAlgorithm.calculateTravellersSatisfaction();
    }
}
