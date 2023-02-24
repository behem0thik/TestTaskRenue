package com.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Needs only one command line argument");
            throw new IllegalArgumentException();
        }

        int columnSearch;
        try {
            columnSearch = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid column number: " + args[0]);
            throw new IllegalArgumentException();
        }

        AirportsRepository airportsRepository = new AirportsRepository();
        Map<Character, Set<Integer>> uniqueSymbolPositions = airportsRepository.findUniqueSymbolPositions(columnSearch);

        Scanner scanner = new Scanner(System.in);
        boolean quit = false;
        while (!quit) {
            System.out.println("\nВведите строку:");
            String input = scanner.nextLine();
            System.out.println("");

            if (input.equals("!quit")) {
                quit = true;
            } else {
                long startSearchTime = System.nanoTime();

                Set<Integer> linesToCheck = airportsRepository.findLineNumbersToCheck(uniqueSymbolPositions, input);
                Set<String> result = airportsRepository.findLinesContainsInput(linesToCheck, columnSearch, input);

                long endSearchTime = System.nanoTime();
                for (String string : result) {
                    System.out.println(string);
                }
                System.out.println("\nКоличество найденных строк: " + result.size() +
                        ", Затрачено времени на поиск: " + (endSearchTime - startSearchTime) / 1000000 + " мс");

            }
        }
    }
}