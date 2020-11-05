package com.fradot.exercise.airline.seats.manager;

import com.fradot.exercise.airline.seats.manager.model.Traveler;

import java.util.List;

public class SeatsAllocatorAlgorithm {

    private final List<List<Traveler>> travellersList;
    private final String[][] seatsMatrix;

    public SeatsAllocatorAlgorithm(final List<List<Traveler>> travellersList, final String[][] seatsMatrix) {
        this.travellersList = travellersList;
        this.seatsMatrix = seatsMatrix;
    }

    public double calculateTravellersSatisfaction() {
        double satisfiedTravellers = 0;
        int totalTravellers = 0;

        for (List<Traveler> group : travellersList) {
            for (Traveler traveler : group) {
                final int t_row = traveler.getRow();
                final int t_column = traveler.getColumn();
                totalTravellers++;

                // Let's first check if a seat was allocated for the traveller
                if ((t_row >= 0 && t_column >= 0) && seatsMatrix != null) {
                    if (seatsMatrix[t_row][t_column].equals(traveler.getId())) {
                        // Check if customer is seating were requested
                        if (traveler.isWindowPreference()) {
                            satisfiedTravellers += isSeatingNearWindow(traveler) ? 1 : 0.5;
                        } else {
                            satisfiedTravellers++;
                        }
                    } else {
                        throw new IllegalStateException(String.format(
                                "Traveller %s not found in the expected seat(%d, %d)!",
                                traveler.getId(), traveler.getRow(), traveler.getColumn()));
                    }
                }
            }
        }

        return (satisfiedTravellers * 100) / totalTravellers;
    }

    private boolean isSeatingNearWindow(final Traveler traveler) {
        if (seatsMatrix != null && seatsMatrix[0] != null) {
            return traveler.getColumn() == 0 || traveler.getColumn() == seatsMatrix[0].length - 1;
        }
        return false;
    }
}
