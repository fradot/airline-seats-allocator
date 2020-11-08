package com.fradot.exercise.airline.seats.manager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class models a Plane.
 */
public class Plane {

    private final int rows;
    private final int seatsPerRow;
    private final List<Traveler> travellersList;

    public Plane(int rows, int seatsPerRow) {
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
        this.travellersList = new ArrayList<>(rows * seatsPerRow);
    }

    public int getRows() {
        return rows;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public void addTraveller(final Traveler traveler) {
        this.travellersList.add(traveler);
    }

    public List<Traveler> getTravellersList() {
        return this.travellersList;
    }
}
