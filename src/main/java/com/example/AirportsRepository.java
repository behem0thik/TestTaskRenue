package com.example;

import java.io.*;
import java.util.*;

public class AirportsRepository {
    private final String filename;

    public AirportsRepository() {
        filename = "airports.dat";
    }

    public Set<String> findLinesContainsInput(Set<Integer> sortedLinesToCheck, int columnSearch, String input) {
        Set<String> result = new TreeSet<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/" + filename))))) {

            int line = 0;
            String extractedLine;
            while ((extractedLine = bufferedReader.readLine()) != null) {

                line++;
                if (sortedLinesToCheck.contains(line)) {

                    String[] columns = extractedLine.split(",");
                    if (columns.length < columnSearch) {
                        System.out.println("File doesn't contain a column with a number " + columnSearch + " in " + line + " line");
                        throw new RuntimeException();
                    }

                    String searchColumn = columns[columnSearch - 1].replaceAll("\"", "");
                    if (searchColumn.toUpperCase().startsWith(input.toUpperCase())) {
                        result.add(columns[columnSearch - 1] + "[" + extractedLine + "]");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found while finding lines containing input");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Failed or interrupted I/O option while finding line containing input)");
            throw new RuntimeException(e);
        }
        return result;
    }

    public Map<Character, Set<Integer>> findUniqueSymbolPositions(int columnSearch) {
        Map<Character, Set<Integer>> uniqueSymbolPositions = new HashMap<>(26 + 10);

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/" + filename))))) {
            String rawFileLine = bufferedReader.readLine();
            int line = 1;

            while ((rawFileLine = bufferedReader.readLine()) != null) {
                line++;

                String[] columns = rawFileLine.split(",");
                if (columns.length < columnSearch) {
                    System.out.println("File doesn't contain a column with a number " + columnSearch + " in " + line + " line");
                    throw new RuntimeException();
                }

                String searchColumn = columns[columnSearch - 1].replaceAll("\"", "")
                        .replaceAll("« »", "").replaceAll(" ", "").toUpperCase();
                for (int i = 0; i < searchColumn.length(); i++) {
                    Character symbol = searchColumn.charAt(i);

                    Set<Integer> lines = uniqueSymbolPositions.get(symbol);
                    if (lines == null) {
                        uniqueSymbolPositions.put(symbol, new HashSet<>(Set.of(line)));
                    } else {
                        lines.add(line);
                        uniqueSymbolPositions.put(symbol, lines);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found while reading");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Failed or interrupted I/O option while reading file)");
            throw new RuntimeException(e);
        }
        return uniqueSymbolPositions;
    }

    public Set<Integer> findLineNumbersToCheck(Map<Character, Set<Integer>> uniqueSymbolPositions, String input) {

        List<Character> characterList = new LinkedList<>();
        for (int i = 0; i < input.length(); i++) {
            characterList.add(input.toUpperCase(Locale.ROOT).charAt(i));
        }

        List<Set<Integer>> lineSet = new LinkedList<>();
        for (Map.Entry<Character, Set<Integer>> entry : uniqueSymbolPositions.entrySet()) {
            if (characterList.contains(entry.getKey())) {
                lineSet.add(entry.getValue());
            }
        }

        Set<Integer> intersection = new HashSet<>();
        if (lineSet.size() > 0) {
            intersection.addAll(lineSet.remove(0));
            while (!lineSet.isEmpty()) {
                Set<Integer> firstSymbolLines = new HashSet<>(lineSet.remove(0));
                intersection.retainAll(firstSymbolLines);
            }
        }
        return new HashSet<>(intersection);
    }
}