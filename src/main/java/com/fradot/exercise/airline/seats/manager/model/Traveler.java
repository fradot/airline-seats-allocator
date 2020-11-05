package com.fradot.exercise.airline.seats.manager.model;

public class Traveler {

    private final String id;
    private int row;
    private int column;

    public Traveler(String id, int row, int column) {
        this.id = id;
        this.row = row;
        this.column = column;
    }

    public boolean isWindowPreference() {
        return id.endsWith("W");
    }

    public String getId() {
        return id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
