package com.benjamin;

import com.benjamin.algorithms.FirstComeFirstServe;
import com.benjamin.algorithms.Process;
import com.benjamin.algorithms.RoundRobin;
import com.benjamin.algorithms.ShortestJobFirst;
import com.benjamin.algorithms.ShortestRemainingTimeNext;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    private static int processNumber;
    private static int quantum;
    private static final List<com.benjamin.algorithms.Process> processes = new ArrayList<>();

    public static void main(String[] args) {
        String file = "input.txt";

        queryDataFromFile(file);

        FirstComeFirstServe();

        RoundRobin();

        ShortestJobFirst();

        ShortestRemainingTimeNext();
    }

    private static void FirstComeFirstServe() {
        Map<Integer, String> schedule = FirstComeFirstServe.schedule(processes);
        List<com.benjamin.algorithms.Process> scheduledProcesses = FirstComeFirstServe.execute(processes);

        String file = "FCFS.txt";
        writeToFile(file, schedule, scheduledProcesses);
    }

    private static void RoundRobin() {
        Map<Integer, String> schedule = RoundRobin.schedule(processes, quantum);
        List<com.benjamin.algorithms.Process> scheduledProcesses = RoundRobin.execute(processes, quantum);

        String file = "RR.txt";
        writeToFile(file, schedule, scheduledProcesses);
    }

    private static void ShortestJobFirst() {
        Map<Integer, String> schedule = ShortestJobFirst.schedule(processes);
        List<com.benjamin.algorithms.Process> scheduledProcesses = ShortestJobFirst.execute(processes);

        String file = "SJF.txt";
        writeToFile(file, schedule, scheduledProcesses);
    }

    private static void ShortestRemainingTimeNext() {
        Map<Integer, String> schedule = ShortestRemainingTimeNext.schedule(processes, quantum);
        List<com.benjamin.algorithms.Process> scheduledProcesses = ShortestRemainingTimeNext.execute(processes, quantum);

        String file = "SRTN.txt";
        writeToFile(file, schedule, scheduledProcesses);
    }

    private static void queryDataFromFile(String file) {
        String path = "src/com/benjamin/" + file;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseTokens(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseTokens(String line) {
        String[] tokens = line.split(" ");

        if (tokens.length < 2) {
            return;
        }

        if (tokens.length == 2) {
            processNumber = Integer.parseInt(tokens[0]);
            quantum = Integer.parseInt(tokens[1]);
        }

        if (tokens.length == 4) {
            com.benjamin.algorithms.Process process = com.benjamin.algorithms.Process.builder()
                    .name(tokens[0])
                    .arrivalTime(Integer.parseInt(tokens[1]))
                    .CPUBurst(Integer.parseInt(tokens[2]))
                    .priority(Integer.parseInt(tokens[3]))
                    .build();
            processes.add(process);
        }
    }

    private static void writeToFile(String file, Map<Integer, String> schedule, List<com.benjamin.algorithms.Process> processes) {
        String path = "src/com/benjamin/output/" + file;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, false))) {

            writeSchedule(writer, schedule, processes);

            writer.write("\n\n");

            writeScheduledProcesses(writer, processes);

            writeAverageValues(writer, processes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeSchedule(BufferedWriter writer, Map<Integer, String> schedule, List<com.benjamin.algorithms.Process> processes)
            throws IOException {
        writer.write("Scheduling chart: " + processes.get(0).getArrivalTime());

        for (Map.Entry<Integer, String> entry : schedule.entrySet()) {
            writer.write(" ~ " + entry.getValue() + " ~ " + entry.getKey());
        }
    }

    private static void writeScheduledProcesses(BufferedWriter writer, List<com.benjamin.algorithms.Process> processes) throws IOException {
        for (com.benjamin.algorithms.Process process : processes) {
            writer.write(process.getName());
            writer.write(": ");

            writer.write("TT=" + process.getTurnAroundTime());
            writer.write(" ");
            writer.write("WT=" + process.getWaitingTime());

            writer.write("\n");
        }
    }

    private static void writeAverageValues(BufferedWriter writer, List<com.benjamin.algorithms.Process> processes) throws IOException {
        double averageTurnAroundTime = calculateAverageTurnAroundTime(processes);
        double averageWaitingTime = calculateAverageWaitingTime(processes);

        writer.write("Average: ");
        writer.write("TT=" + averageTurnAroundTime);
        writer.write(" ");
        writer.write("WT=" + averageWaitingTime);
    }

    private static double calculateAverageTurnAroundTime(List<com.benjamin.algorithms.Process> processes) {
        double averageTurnAroundTime = processes.stream()
                .mapToInt(com.benjamin.algorithms.Process::getTurnAroundTime)
                .sum() * 1.00 / processNumber;
        return Math.round(averageTurnAroundTime * 100d) / 100d;
    }

    private static double calculateAverageWaitingTime(List<com.benjamin.algorithms.Process> processes) {
        double averageWaitingTime = processes.stream()
                .mapToInt(Process::getWaitingTime)
                .sum() * 1.00 / processNumber;
        return Math.round(averageWaitingTime * 100d) / 100d;
    }
}
