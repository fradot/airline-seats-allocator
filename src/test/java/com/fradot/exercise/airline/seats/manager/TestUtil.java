package com.fradot.exercise.airline.seats.manager;

import com.fradot.exercise.airline.seats.manager.model.Traveler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtil {

    private TestUtil() {}

    public static String[][] fillSeatsMatrixFromTravellersGroups(
            final List<List<String>> travellersGroups, final String[][] seatsMatrix) {
        for (int i = 0; i < travellersGroups.size(); i++) {
            final List<String> group = travellersGroups.get(i);
            if (group != null && group.size() > 0) {
                for (int j = 0; j < group.size(); j++) {
                    seatsMatrix[i][j] = group.get(j);
                }
            }
        }

        return seatsMatrix;
    }

    public static String[][] generateEmptySeatsMatrix(final int rows, final int columns) {
        final String[][] seatsMatrix = new String[rows][columns];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(seatsMatrix[i], "");
        }
        return seatsMatrix;
    }

    public static List<String> generateTravellersIdList(final String... ids) {
        final List<String> result = new ArrayList<>(ids.length);
        for (String id : ids) {
            result.add(id);
        }
        return result;
    }

    public static List<List<Traveler>> generateTravellersGroupsFromIdGroups(final List<List<String>> idGroups) {
        final List<List<Traveler>> travellersGroups = new ArrayList<>(idGroups.size());
        for(List<String> idGroup: idGroups) {
            travellersGroups.add(idGroup.stream().map(Traveler::new).collect(Collectors.toList()));
        }
        return travellersGroups;
    }

    public static void deAllocateSeatsMatrix(final String[][] seatsMatrix, final int... rows) {
        for (int row : rows) {
            Arrays.fill(seatsMatrix[row], "");
        }
    }
}
