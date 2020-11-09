package com.fradot.exercise.airline.seats.manager.service;

import org.junit.Test;

import java.util.*;

import static com.fradot.exercise.airline.seats.manager.TestUtil.*;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class SeatsAllocatorAlgorithmTest {

    @Test
    public void calculateRowSatisfaction_itShouldCorrectlyMeasureTravellersSatisfaction() {
        final List<List<String>> travellersGroups = new ArrayList<>();
        travellersGroups.add(generateTravellersIdList("4W", "5", "6W", "7W"));
        final String[][] seatsMatrix = generateEmptySeatsMatrix(1, 4);
        fillSeatsMatrixFromTravellersGroups(travellersGroups, seatsMatrix);

        assertEquals(87.5, SeatsAllocatorAlgorithm.calculateRowSatisfaction(seatsMatrix[0]), 0.01);
    }

    @Test
    public void calculateRowSatisfaction_itShouldReturnZeroIfMatrixIsNull() {
        assertEquals(0.0, SeatsAllocatorAlgorithm.calculateRowSatisfaction(null));
    }

    @Test
    public void allocateSeats_itShouldAllocateAGroupOfTravellers() {
        final List<List<String>> travellersGroups = new ArrayList<>();
        travellersGroups.add(generateTravellersIdList("1", "2W", "3W", "4"));
        travellersGroups.add(generateTravellersIdList("4W", "5W", "6", "7W"));
        travellersGroups.add(generateTravellersIdList("8W", "9W", "10W", "11W"));

        final String[][] seatsMatrix = generateEmptySeatsMatrix(3, 4);

        // Re-setting the matrix to empty
        deAllocateSeatsMatrix(seatsMatrix, 0, 1, 2);

        // Allocate matrix
        SeatsAllocatorAlgorithm.allocateSeats(seatsMatrix[0], travellersGroups.get(0));
        SeatsAllocatorAlgorithm.allocateSeats(seatsMatrix[1], travellersGroups.get(1));
        SeatsAllocatorAlgorithm.allocateSeats(seatsMatrix[2], travellersGroups.get(2));

        assertArrayEquals(Arrays.asList("2W", "1", "4", "3W").toArray(), seatsMatrix[0]);
        assertArrayEquals(Arrays.asList("4W", "6", "7W", "5W").toArray(), seatsMatrix[1]);
        assertArrayEquals(Arrays.asList("8W", "10W", "11W", "9W").toArray(), seatsMatrix[2]);
    }

    @Test
    public void allocateSeats_itShouldAllocateAGroupOfTravellersOnAPartiallyAllocatedRow() {
        final List<List<String>> travellersGroups = new ArrayList<>();
        travellersGroups.add(generateTravellersIdList("2W", "1"));
        travellersGroups.add(generateTravellersIdList("3", "4", "5"));
        travellersGroups.add(generateTravellersIdList("8W"));
        final String[][] seatsMatrix = generateEmptySeatsMatrix(3, 4);
        fillSeatsMatrixFromTravellersGroups(travellersGroups, seatsMatrix);

        // Creating new travellers groups
        final List<List<String>> newTravellersGroups = new ArrayList<>();
        newTravellersGroups.add(generateTravellersIdList("9W", "10"));
        newTravellersGroups.add(generateTravellersIdList("11W"));
        newTravellersGroups.add(generateTravellersIdList("12W", "13W", "14"));

        // Allocate matrix
        SeatsAllocatorAlgorithm.allocateSeats(seatsMatrix[0], newTravellersGroups.get(0));
        SeatsAllocatorAlgorithm.allocateSeats(seatsMatrix[1], newTravellersGroups.get(1));
        SeatsAllocatorAlgorithm.allocateSeats(seatsMatrix[2], newTravellersGroups.get(2));

        assertArrayEquals(Arrays.asList("2W", "1", "10", "9W").toArray(), seatsMatrix[0]);
        assertArrayEquals(Arrays.asList("11W", "4", "5", "3").toArray(), seatsMatrix[1]);
        assertArrayEquals(Arrays.asList("8W", "13W", "14", "12W").toArray(), seatsMatrix[2]);
    }

    @Test(expected = IllegalStateException.class)
    public void allocateSeats_itShouldThrowAnIllegalStateExceptionIfNoMoreSeatsAreAvailable() {
        final List<List<String>> travellersGroups = new ArrayList<>();
        travellersGroups.add(generateTravellersIdList("1", "2W", "3", "4", "5"));
        final String[][] seatsMatrix = generateEmptySeatsMatrix(1, 4);

        // Allocate matrix
        SeatsAllocatorAlgorithm.allocateSeats(seatsMatrix[0], travellersGroups.get(0));
    }

    @Test
    public void deAllocateSeats_itShouldDeAllocateAGroupOfTravellers() {
        final List<List<String>> travellersGroups = new ArrayList<>();
        travellersGroups.add(generateTravellersIdList("1W", "2", "3"));
        travellersGroups.add(generateTravellersIdList("4W", "5", "6W", "7W"));
        travellersGroups.add(generateTravellersIdList("8W", "9W", "10W", "11W"));

        final String[][] seatsMatrix = generateEmptySeatsMatrix(3, 4);
        fillSeatsMatrixFromTravellersGroups(travellersGroups, seatsMatrix);

        // De-Allocate matrix row 1
        SeatsAllocatorAlgorithm.deAllocateSeats(seatsMatrix[0], travellersGroups.get(0));
        SeatsAllocatorAlgorithm.deAllocateSeats(seatsMatrix[1], travellersGroups.get(1));
        SeatsAllocatorAlgorithm.deAllocateSeats(seatsMatrix[2], travellersGroups.get(2));

        assertArrayEquals(Arrays.asList("", "", "", "").toArray(), seatsMatrix[0]);
        assertArrayEquals(Arrays.asList("", "", "", "").toArray(), seatsMatrix[1]);
        assertArrayEquals(Arrays.asList("", "", "", "").toArray(), seatsMatrix[2]);
    }

    @Test
    public void allocateSeatsForTravellersGroup_itShouldFindSeatsForTravellersThatMaximiseSatisfaction() {
        // Not all window seat preferences satisfied
        final List<List<String>> travellersGroups1 = new ArrayList<>();
        travellersGroups1.add(generateTravellersIdList("1W", "2", "3"));
        travellersGroups1.add(generateTravellersIdList("4W", "5", "6W", "7W"));
        travellersGroups1.add(generateTravellersIdList("8"));
        travellersGroups1.add(generateTravellersIdList("9W", "10W"));
        final String[][] seatsMatrix1 = generateEmptySeatsMatrix(4, 4);

        // All preferences satisfied
        final List<List<String>> travellersGroups2 = new ArrayList<>();
        travellersGroups2.add(generateTravellersIdList("1", "2"));
        travellersGroups2.add(generateTravellersIdList("4W", "5", "6", "7W"));
        travellersGroups2.add(generateTravellersIdList("8", "10W", "11", "12W"));
        travellersGroups2.add(generateTravellersIdList("13W", "14W"));
        travellersGroups2.add(generateTravellersIdList("15W", "16", "17W", "18"));
        final String[][] seatsMatrix2 = generateEmptySeatsMatrix(4, 4);

        // One group is too large
        final List<List<String>> travellersGroups3 = new ArrayList<>();
        travellersGroups3.add(generateTravellersIdList("1", "2"));
        travellersGroups3.add(generateTravellersIdList("4W", "5", "6", "7W"));
        travellersGroups3.add(generateTravellersIdList("8", "10W", "11", "12W", "13"));
        travellersGroups3.add(generateTravellersIdList("14W", "15W"));
        travellersGroups3.add(generateTravellersIdList("16W", "17", "18W", "19"));
        final String[][] seatsMatrix3 = generateEmptySeatsMatrix(4, 4);

        // All groups are too large
        final List<List<String>> travellersGroups4 = new ArrayList<>();
        travellersGroups4.add(generateTravellersIdList("1", "2"));
        travellersGroups4.add(generateTravellersIdList("4W", "5", "6", "7W"));
        travellersGroups4.add(generateTravellersIdList("8", "10W", "11", "12W", "13"));
        travellersGroups4.add(generateTravellersIdList("14W", "15W"));
        travellersGroups4.add(generateTravellersIdList("16W", "17", "18W", "19"));
        final String[][] seatsMatrix4 = generateEmptySeatsMatrix(4, 1);

        // Last row should be placed at the end to maximise satisfaction
        final List<List<String>> travellersGroups5 = new ArrayList<>();
        travellersGroups5.add(generateTravellersIdList("1W", "2", "3"));
        travellersGroups5.add(generateTravellersIdList("4W", "5W", "6W", "7W"));
        travellersGroups5.add(generateTravellersIdList("8W", "10W", "11W", "12W", "13W"));
        travellersGroups5.add(generateTravellersIdList("16W", "17W"));
        final String[][] seatsMatrix5 = generateEmptySeatsMatrix(4, 5);

        // Last row should be placed on the second row as maximise satisfaction
        final List<List<String>> travellersGroups6 = new ArrayList<>();
        travellersGroups6.add(generateTravellersIdList("1W", "2W", "3W"));
        travellersGroups6.add(generateTravellersIdList("4W", "5", "6"));
        travellersGroups6.add(generateTravellersIdList("8W", "10W", "11W", "12W", "13W"));
        travellersGroups6.add(generateTravellersIdList("16W", "17W"));
        final String[][] seatsMatrix6 = generateEmptySeatsMatrix(3, 5);

        // No travellers
        final List<List<String>> travellersGroups7 = new ArrayList<>();
        final String[][] seatsMatrix7 = generateEmptySeatsMatrix(4, 4);

        SeatsAllocatorAlgorithm.allocateSeatsForTravellersGroup(seatsMatrix1, travellersGroups1);
        SeatsAllocatorAlgorithm.allocateSeatsForTravellersGroup(seatsMatrix2, travellersGroups2);
        SeatsAllocatorAlgorithm.allocateSeatsForTravellersGroup(seatsMatrix3, travellersGroups3);
        SeatsAllocatorAlgorithm.allocateSeatsForTravellersGroup(seatsMatrix4, travellersGroups4);
        SeatsAllocatorAlgorithm.allocateSeatsForTravellersGroup(seatsMatrix5, travellersGroups5);
        SeatsAllocatorAlgorithm.allocateSeatsForTravellersGroup(seatsMatrix6, travellersGroups6);
        SeatsAllocatorAlgorithm.allocateSeatsForTravellersGroup(seatsMatrix7, travellersGroups7);

        // 1
        assertEquals(95, SeatsAllocatorAlgorithm.calculateBookingsSatisfaction(seatsMatrix1, travellersGroups1), 0.01);
        assertArrayEquals(
                new String[][] {
                    {"1W", "2", "3", "8"},
                    {"4W", "5", "7W", "6W"},
                    {"9W", "", "", "10W"},
                    {"", "", "", ""}
                },
                seatsMatrix1);
        // 2
        assertEquals(
                100.0, SeatsAllocatorAlgorithm.calculateBookingsSatisfaction(seatsMatrix2, travellersGroups2), 0.01);
        assertArrayEquals(
                new String[][] {
                    {"13W", "2", "1", "14W"},
                    {"4W", "5", "6", "7W"},
                    {"10W", "8", "11", "12W"},
                    {"15W", "16", "18", "17W"}
                },
                seatsMatrix2);

        // 3
        assertArrayEquals(
                new String[][] {
                    {"14W", "2", "1", "15W"},
                    {"4W", "5", "6", "7W"},
                    {"16W", "17", "19", "18W"},
                    {"", "", "", ""}
                },
                seatsMatrix3);
        assertEquals(
                70.58, SeatsAllocatorAlgorithm.calculateBookingsSatisfaction(seatsMatrix3, travellersGroups3), 0.01);

        // 4
        assertArrayEquals(new String[][] {{""}, {""}, {""}, {""}}, seatsMatrix4);
        assertEquals(0.0, SeatsAllocatorAlgorithm.calculateBookingsSatisfaction(seatsMatrix4, travellersGroups4), 0.01);

        // 5
        assertArrayEquals(
                new String[][] {
                        {"1W", "2", "3", "", ""},
                        {"4W", "6W", "7W", "", "5W"},
                        {"8W", "11W", "12W", "13W", "10W"},
                        {"16W", "", "", "", "17W"}
                },
                seatsMatrix5);
        assertEquals(
                82.14, SeatsAllocatorAlgorithm.calculateBookingsSatisfaction(seatsMatrix5, travellersGroups5), 0.01);

        // 6
        assertArrayEquals(
                new String[][] {
                        {"1W", "3W", "", "", "2W"},
                        {"4W", "5", "6", "17W", "16W"},
                        {"8W", "11W", "12W", "13W", "10W"}
                },
                seatsMatrix6);
        assertEquals(
                80.76, SeatsAllocatorAlgorithm.calculateBookingsSatisfaction(seatsMatrix6, travellersGroups6), 0.01);

        // 7
        assertEquals(0.0, SeatsAllocatorAlgorithm.calculateBookingsSatisfaction(seatsMatrix7, travellersGroups7), 0.01);
    }
}
