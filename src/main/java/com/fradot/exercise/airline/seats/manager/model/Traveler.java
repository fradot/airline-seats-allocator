package com.fradot.exercise.airline.seats.manager.model;

/**
 * This class models a Traveler.
 */
public class Traveler {

    private final String id;
    private Seat seat;
    private static final String WINDOW = "W";

    public Traveler(final String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Long getLongId() {
        return Long.valueOf(id.endsWith(WINDOW) ? id.substring(0, id.length() - 1) : id);
    }
}
