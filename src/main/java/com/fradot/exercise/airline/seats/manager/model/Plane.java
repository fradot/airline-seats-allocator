package com.fradot.exercise.airline.seats.manager.model;

import java.util.HashSet;
import java.util.Set;

/**
 * This class models a Plane.
 */
public class Plane {

    private final int rows;
    private final int seatsPerRow;
    private final Set<Traveler> travellersList;

    public Plane(int rows, int seatsPerRow) {
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
        this.travellersList = new HashSet<>(rows * seatsPerRow);
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

    public Set<Traveler> getTravellersSet() {
        return this.travellersList;
    }
}
