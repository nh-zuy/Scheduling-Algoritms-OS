package com.benjamin.algorithms;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FirstComeFirstServe {

    public static List<Process> execute(List<Process> processes) {
        List<Process> sortedProcessesList = sortByArrivalTime(processes);

        int runtime = sortedProcessesList.get(0).getArrivalTime();
        for (Process process : sortedProcessesList) {
            int waitingTime = runtime - process.getArrivalTime();
            process.setWaitingTime(waitingTime);

            int turnAroundTime = process.getCPUBurst() + process.getWaitingTime();
            process.setTurnAroundTime(turnAroundTime);

            runtime += process.getCPUBurst();
        }

        return sortedProcessesList;
    }

    private static List<Process> sortByArrivalTime(List<Process> processes) {
        return processes.stream()
                .sorted(Comparator.comparingInt(Process::getArrivalTime))
                .collect(Collectors.toList());
    }

    public static Map<Integer, String> schedule(List<Process> processes) {
        List<Process> scheduledProcessesList = sortByArrivalTime(processes);

        Map<Integer, String> schedule = new LinkedHashMap<>();

        int runtime = scheduledProcessesList.get(0).getArrivalTime();
        for (Process process : scheduledProcessesList) {
            runtime += process.getCPUBurst();
            schedule.put(runtime, process.getName());
        }

        return schedule;
    }
}
