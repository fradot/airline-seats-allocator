package com.fradot.exercise.airline.seats.manager.service;

import com.fradot.exercise.airline.seats.manager.model.Plane;
import com.fradot.exercise.airline.seats.manager.model.Seat;
import com.fradot.exercise.airline.seats.manager.model.Traveler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.fradot.exercise.airline.seats.manager.service.SeatsAllocatorAlgorithm.EMPTY_SEAT;
import static com.fradot.exercise.airline.seats.manager.service.SeatsAllocatorAlgorithm.calculateBookingsSatisfaction;

public class SeatsAllocatorService {

    private final List<List<Traveler>> travellersGroups;
    private final Plane plane;
    private double travellersSatisfaction;

    public SeatsAllocatorService(final List<List<Traveler>> travellersGroups, final Plane plane) {
        this.travellersGroups = travellersGroups;
        this.plane = plane;
    }

    public double getTravellersSatisfaction() {
        return travellersSatisfaction;
    }

    public void allocateFlight() throws SeatsAllocatorException {
        final String[][] seatsMatrix = generateSeatsMatrix(plane.getRows(), plane.getSeatsPerRow());
        final List<List<String>> travellersGroupIds = generateTravellersIdsList(travellersGroups);

        // Allocate seats matrix
        SeatsAllocatorAlgorithm.allocateSeatsForTravellersGroup(seatsMatrix, travellersGroupIds);

        // Build plane traveller list
        int allocatedTravellers = 0;
        for (int i = 0; i < seatsMatrix.length; i++) {
            for (int j = 0; j < seatsMatrix[i].length; j++) {
                final String id = seatsMatrix[i][j];
                if (!id.equals(EMPTY_SEAT)) {
                    Seat seat = new Seat(i, j);
                    Traveler traveler = new Traveler(id);
                    traveler.setSeat(seat);
                    this.plane.addTraveller(traveler);
                    allocatedTravellers++;
                }
            }
        }

        if (allocatedTravellers == 0
                && travellersGroupIds.size() > 0
                && travellersGroupIds.get(0).size() > 0) {
            throw new SeatsAllocatorException("Error allocating seats! No seat was allocated on the plane!");
        }

        // calculate travellers satisfaction
        this.travellersSatisfaction = calculateBookingsSatisfaction(seatsMatrix, travellersGroupIds);
    }

    public Long[][] generateAllocatedFlightMatrix() throws SeatsAllocatorException {
        final Long[][] flightMatrix = new Long[plane.getRows()][];
        final Set<Traveler> flightTravellersSet = plane.getTravellersSet();

        for(int i = 0; i < flightMatrix.length; i++) {
            flightMatrix[i] = new Long[plane.getSeatsPerRow()];
            Arrays.fill(flightMatrix[i], -1L);
        }

        for (Traveler traveler : flightTravellersSet) {
            final int row = traveler.getSeat().getRow();
            final int column = traveler.getSeat().getColumn();
            if (flightMatrix[row][column] != null) {
                flightMatrix[row][column] = traveler.getLongId();
            } else {
                throw new SeatsAllocatorException(String.format(
                        "Error generating flight seats matrix for traveler %s",
                        traveler.getId()));
            }
        }

        return flightMatrix;
    }

    private static List<List<String>> generateTravellersIdsList(final List<List<Traveler>> travellersGroups) {
        final List<List<String>> travellersGroupIds = new ArrayList<>(travellersGroups.size());
        for (List<Traveler> group : travellersGroups) {
            travellersGroupIds.add(group.stream().map(Traveler::getId).collect(Collectors.toList()));
        }
        return travellersGroupIds;
    }

    private static String[][] generateSeatsMatrix(final int rows, final int columns) {
        final String[][] seatsMatrix = new String[rows][columns];
        for (int i = 0; i < seatsMatrix.length; i++) {
            Arrays.fill(seatsMatrix[i], EMPTY_SEAT);
        }
        return seatsMatrix;
    }
}
