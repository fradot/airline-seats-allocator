package com.fradot.exercise.airline.seats.manager;

import com.fradot.exercise.airline.seats.manager.model.Traveler;

import java.util.ArrayList;
import java.util.List;

public class TestUtil {

    private TestUtil(){};

    public static List<List<Traveler>> generateTravellersListFromSeats(final String[][] seatsMatrix) {
        final List<List<Traveler>> travellersList = new ArrayList<List<Traveler>>(seatsMatrix.length);
        for(int i=0; i < seatsMatrix.length; i++) {
            List<Traveler> travellers = new ArrayList<Traveler>();
            for(int j=0; j < seatsMatrix[i].length; j++) {
                if (seatsMatrix[i][j] != null) {
                    String id = seatsMatrix[i][j];
                    if (id != null && id.length() > 0) {
                        Traveler traveler = new Traveler(id, i, j);
                        travellers.add(traveler);
                    }
                }
            }
            travellersList.add(travellers);
        }

        return travellersList;
    }

    public static String[][] generateSeatsMatrixFromTravellersGroups(final List<List<String>> travellersGroups) {
        final String[][] seatsMatrix = new String[travellersGroups.size()][];
        int i = 0;
        for (List<String> group : travellersGroups) {
            final String[] groupArray = new String[group.size()];
            for (int j =0; j < groupArray.length; j++) {
                groupArray[j] = group.get(j);
            }
            seatsMatrix[i] = groupArray;
            i++;
        }

        return seatsMatrix;
    }

    public static List<String> generateTravellersIdList(String ... ids) {
        final List<String> result = new ArrayList<String>(ids.length);
        for(String id: ids) {
            result.add(id);
        }
        return result;
    }
}
