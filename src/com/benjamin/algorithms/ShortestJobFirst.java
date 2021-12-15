package com.benjamin.algorithms;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShortestJobFirst {

    public static List<Process> execute(List<Process> processes) {
        List<Process> sortedProcessesList = sortByCPUBurst(processes);

        List<Integer> remainingBurstTimes = getRemainingBurstTime(sortedProcessesList);

        int runtime = processes.get(0).getArrivalTime();
        while (true) {
            for (int order = 0; order < sortedProcessesList.size(); ++order) {
                Process process = sortedProcessesList.get(order);

                if (runtime < process.getArrivalTime()) {
                    continue;
                }

                if (remainingBurstTimes.get(order) <= 0) {
                    continue;
                }

                process.setWaitingTime(runtime - process.getArrivalTime());

                process.setTurnAroundTime(process.getCPUBurst() + process.getWaitingTime());

                runtime += process.getCPUBurst();
                remainingBurstTimes.set(order, 0);
            }

            boolean isAllProcessDone = remainingBurstTimes.stream().allMatch(burstTime -> burstTime == 0);
            if (isAllProcessDone) {
                break;
            }
        }

        return sortByArrivalTime(sortedProcessesList);
    }

    private static List<Process> sortByCPUBurst(List<Process> processes) {
        return processes.stream()
                .sorted(Comparator.comparingInt(Process::getCPUBurst))
                .collect(Collectors.toList());
    }

    private static List<Integer> getRemainingBurstTime(List<Process> processes) {
        return processes.stream()
                .mapToInt(Process::getCPUBurst)
                .boxed()
                .collect(Collectors.toList());
    }

    private static List<Process> sortByArrivalTime(List<Process> processes) {
        return processes.stream()
                .sorted(Comparator.comparingInt(Process::getArrivalTime))
                .collect(Collectors.toList());
    }

    public static Map<Integer, String> schedule(List<Process> processes) {
        List<Process> scheduledProcessesList = sortByCPUBurst(processes);

        List<Integer> remainingBurstTimes = getRemainingBurstTime(scheduledProcessesList);

        Map<Integer, String> schedules = new LinkedHashMap<>();

        int runtime = processes.get(0).getArrivalTime();
        while (true) {
            for (int order = 0; order < scheduledProcessesList.size(); ++order) {
                Process process = scheduledProcessesList.get(order);

                if (runtime < process.getArrivalTime()) {
                    continue;
                }

                if (remainingBurstTimes.get(order) <= 0) {
                    continue;
                }

                runtime += process.getCPUBurst();
                remainingBurstTimes.set(order, 0);

                schedules.put(runtime, process.getName());
            }

            boolean isAllProcessDone = remainingBurstTimes.stream().allMatch(burstTime -> burstTime == 0);
            if (isAllProcessDone) {
                break;
            }
        }

        return schedules;
    }
}
