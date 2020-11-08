package com.fradot.exercise.airline.seats.manager.service;

import java.util.List;

public class SeatsAllocatorService {

    private final List<List<String>> travellersList;
    private final String[][] seatsMatrix;

    public SeatsAllocatorService(final List<List<String>> travellersList, final String[][] seatsMatrix) {
        this.travellersList = travellersList;
        this.seatsMatrix = seatsMatrix;
    }
}
