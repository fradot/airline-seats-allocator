package com.fradot.exercise.airline.seats.manager.service;

import com.fradot.exercise.airline.seats.manager.model.Plane;
import com.fradot.exercise.airline.seats.manager.model.Traveler;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.fradot.exercise.airline.seats.manager.TestUtil.*;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class SeatsAllocatorServiceTest {

    @Test
    public void allocateFlight_itShouldAllocateTheFlightInOrderToSatisfyTheTravellers() throws SeatsAllocatorException {
        final List<List<String>> travellersGroupsIds = new ArrayList<>();
        travellersGroupsIds.add(generateTravellersIdList("1W", "2", "3"));
        travellersGroupsIds.add(generateTravellersIdList("4W", "5", "6W", "7W"));
        travellersGroupsIds.add(generateTravellersIdList("8"));
        travellersGroupsIds.add(generateTravellersIdList("9W", "10W"));
        final Plane plane = new Plane(4, 4);
        final List<List<Traveler>> travellersGroups = generateTravellersGroupsFromIdGroups(travellersGroupsIds);
        final SeatsAllocatorService seatsAllocatorService = new SeatsAllocatorService(travellersGroups, plane);
        seatsAllocatorService.allocateFlight();
        assertEquals(95, seatsAllocatorService.getTravellersSatisfaction(), 0.01);
        assertArrayEquals(
                new Long[][] {
                    {1L, 2L, 3L, 8L},
                    {4L, 5L, 7L, 6L},
                    {9L, -1L, -1L, 10L},
                    {-1L, -1L, -1L, -1L}
                },
                seatsAllocatorService.generateAllocatedFlightMatrix());
    }
}
