package com.fradot.exercise.airline.seats.manager.service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class contains the logic to populate a seats matrix according to the group of {@link com.fradot.exercise.airline.seats.manager.model.Traveler}s
 * in input.
 */
class SeatsAllocatorAlgorithm {

    static final double MAX_TRAVELLERS_SATISFACTION = 100.0;
    static final String WINDOW = "W";
    static final String EMPTY_SEAT = "";

    private SeatsAllocatorAlgorithm() {}

    /**
     * This method populate the <code>seatsMatrix</code> using a backtracking approach even if in this case no recursion
     * is used. The <code>seatsMatrix</code> is an array of arrays of {@link String}s,
     * each entry of the matrix is an empty {@link String} initially.
     *
     * Initially the {@link com.fradot.exercise.airline.seats.manager.model.Traveler} group contained in <code>travellersIds</code>
     * gets allocated in the first row available with enough space. The row travellers satisfaction
     * is then calculated, if it's lower than the <code>MAX_TRAVELLERS_SATISFACTION</code>, the algorithm "backtrack" by
     * removing the solution and trying to find a better place which maximise the satisfaction. At each step the row satisfaction
     * is stored in a hashmap, associated with the corresponding row. When there are no more rows available the group is
     * allocated in the row where the satisfaction was higher than all the other satisfactions calculated. An heap is used to
     * extract the first row which maximise the satisfaction.
     *
     *
     * @param seatsMatrix The matrix to be allocated with the {@link com.fradot.exercise.airline.seats.manager.model.Traveler}s ids
     * @param travellersIds The {@link List} of groups of {@link com.fradot.exercise.airline.seats.manager.model.Traveler}s
     */
    static void allocateSeatsForTravellersGroup(final String[][] seatsMatrix, final List<List<String>> travellersIds) {
        final Map<Integer, Double> travellersSatisfactionByRow = new HashMap<>();
        final PriorityQueue<Map.Entry<Integer, Double>> heap = new PriorityQueue<>((entryA, entryB) -> {
            final int rowA = entryA.getKey();
            final double satA = entryA.getValue();
            final int rowB = entryB.getKey();
            final double satB = entryB.getValue();
            if (satA == satB && rowA == rowB) return 0;
            if (satA > satB) return -1;
            if (satA < satB) return 1;
            return satA == satB && rowA <= rowB ? -1 : 1;
        });

        if (travellersIds == null || travellersIds.size() == 0) {
            return;
        }

        for (List<String> group : travellersIds) {
            for (int row = 0; row < seatsMatrix.length; row++) {
                final int availableSeatsForCurrentRow = getNumberOfAvailableSeatsPerRow(seatsMatrix[row]);

                if (availableSeatsForCurrentRow >= group.size()) {
                    allocateSeats(seatsMatrix[row], group);

                    final double currentTravelerSatisfaction = calculateRowSatisfaction(seatsMatrix[row]);
                    if (currentTravelerSatisfaction < MAX_TRAVELLERS_SATISFACTION) {
                        travellersSatisfactionByRow.putIfAbsent(row, currentTravelerSatisfaction);
                        deAllocateSeats(seatsMatrix[row], group);

                        if (row == seatsMatrix.length - 1) {
                            heap.addAll(travellersSatisfactionByRow.entrySet());
                            allocateSeats(seatsMatrix[heap.poll().getKey()], group);
                            heap.clear();
                            travellersSatisfactionByRow.clear();
                        }
                    } else {
                        heap.clear();
                        travellersSatisfactionByRow.clear();
                        break;
                    }
                }
            }

            // if no more rows are available to fit the group but a place was found then allocate it
            if (heap.isEmpty() && !travellersSatisfactionByRow.isEmpty()) {
                heap.addAll(travellersSatisfactionByRow.entrySet());
                allocateSeats(seatsMatrix[heap.poll().getKey()], group);
                heap.clear();
                travellersSatisfactionByRow.clear();
            }
        }
    }

    static void deAllocateSeats(final String[] seatsMatrix, final List<String> group) {
        final Set<String> groupSet = new HashSet<>(group);
        for (int i = 0; i < seatsMatrix.length; i++) {
            if (groupSet.contains(seatsMatrix[i])) {
                seatsMatrix[i] = EMPTY_SEAT;
            }
        }
    }

    static void allocateSeats(final String[] seats, final List<String> group) {

        final PriorityQueue<String> toAllocate = new PriorityQueue<>((travelerA, travelerB) -> {
            if (travelerA.isEmpty()) return -1;
            if (travelerB.isEmpty()) return 1;
            final int aPriority = isWindowPreference(travelerA)
                    ? Integer.valueOf(travelerA.substring(0, travelerA.length() - 1))
                    : Integer.valueOf(travelerA);
            final int bPriority = isWindowPreference(travelerB)
                    ? Integer.valueOf(travelerB.substring(0, travelerB.length() - 1))
                    : Integer.valueOf(travelerB);
            if (aPriority == bPriority) return 0;
            return aPriority > bPriority ? 1 : -1;
        });

        toAllocate.addAll(group);
        toAllocate.addAll(Arrays.asList(seats));
        Arrays.fill(seats, EMPTY_SEAT);

        // Allocate seats based on priority
        while (!toAllocate.isEmpty()) {
            final String travelerId = toAllocate.poll();
            Optional<Integer> seat = Optional.empty();
            final Integer nextAvailableSeat = getNextAvailableSeat(seats)
                    .orElseThrow(() -> new IllegalStateException(
                            String.format("No more places available for traveler %s", travelerId)));

            if (isWindowPreference(travelerId)) {
                seat = getAvailableWindowSeat(seats);

                if (seat.isPresent() && seats[seat.get()].isEmpty()) {
                    seats[seat.get()] = travelerId;
                } else if (seat.isPresent() && !seats[seat.get()].isEmpty()) {
                    // switch traveler seat
                    final int windowSeat = seat.get();
                    seats[nextAvailableSeat] = seats[windowSeat];
                    seats[windowSeat] = travelerId;
                }
            }

            if (!seat.isPresent()) {
                seats[nextAvailableSeat] = travelerId;
            }
        }
    }

    static double calculateBookingsSatisfaction(final String[][] seatsMatrix, final List<List<String>> travellersList) {
        final Set<String> travellersSet = new HashSet<>(
                travellersList.stream().flatMap(Collection::stream).collect(Collectors.toList()));
        final int totalBookings = travellersSet.size();
        double bookingsSatisfaction = 0.0;
        if (travellersSet.size() > 0 && seatsMatrix != null) {
            for (int i = 0; i < seatsMatrix.length; i++) {
                for (int j = 0; j < seatsMatrix[i].length; j++) {
                    final String travelerId = seatsMatrix[i][j];
                    if (travellersSet.contains(travelerId)) {
                        if (isWindowPreference(travelerId)) {
                            bookingsSatisfaction += (j == 0 || j == seatsMatrix[i].length - 1) ? 1 : 0.5;
                        } else {
                            bookingsSatisfaction += 1;
                        }
                    }
                }
            }

            return (bookingsSatisfaction * 100) / totalBookings;
        }
        return bookingsSatisfaction;
    }

    static double calculateRowSatisfaction(final String[] seatsMatrix) {
        double satisfiedTravellers = 0.0;
        int totalTravellers = 0;

        if (seatsMatrix != null) {
            for (int j = 0; j < seatsMatrix.length; j++) {
                if (seatsMatrix[j] != null && !seatsMatrix[j].isEmpty()) {
                    if (seatsMatrix[j].endsWith(WINDOW)) {
                        satisfiedTravellers += (j == 0 || j == seatsMatrix.length - 1) ? 1 : 0.5;
                    } else {
                        satisfiedTravellers += 1;
                    }
                    totalTravellers++;
                }
            }
            return totalTravellers > 0 ? (satisfiedTravellers * 100) / totalTravellers : 0.0;
        }

        return 0.0;
    }

    private static boolean isWindowPreference(final String travelerId) {
        return travelerId.endsWith(WINDOW);
    }

    private static Optional<Integer> getAvailableWindowSeat(final String[] seats) {
        if (seats != null && seats.length > 0) {
            final int rowLength = seats.length;
            if (seats[0].isEmpty() || !seats[0].contains(WINDOW)) return Optional.of(0);
            if (seats[rowLength - 1].isEmpty() || !seats[rowLength - 1].contains(WINDOW))
                return Optional.of(seats.length - 1);
        }
        return Optional.empty();
    }

    private static Optional<Integer> getNextAvailableSeat(final String[] seats) {
        for (int i = 0; i < seats.length; i++) {
            if (seats[i].isEmpty()) return Optional.of(i);
        }
        return Optional.empty();
    }

    private static int getNumberOfAvailableSeatsPerRow(final String[] seats) {
        if (seats != null && seats.length > 0) {
            int emptySeats = 0;
            for (int i = 0; i < seats.length; i++) {
                if (seats[i].isEmpty()) emptySeats++;
            }
            return emptySeats;
        }

        return seats.length;
    }
}
