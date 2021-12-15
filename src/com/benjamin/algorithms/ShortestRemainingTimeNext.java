package com.benjamin.algorithms;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShortestRemainingTimeNext {

    public static List<Process> execute(List<Process> processes, int quantum) {
        List<Process> scheduledProcessesList = sortByArrivalTime(processes);

        List<Integer> remainingBurstTimes = getRemainingBurstTime(scheduledProcessesList);

        int runtime = scheduledProcessesList.get(0).getArrivalTime();
        while (true) {
            Integer order = findShortestRemainingTime(scheduledProcessesList, remainingBurstTimes, runtime);
            if (order == null) {
                break;
            }

            int remainingBurstTimeOfProcess = remainingBurstTimes.get(order);

            if (remainingBurstTimeOfProcess > quantum) {
                runtime += quantum;
                remainingBurstTimes.set(order, remainingBurstTimeOfProcess - quantum);
            }

            if (remainingBurstTimeOfProcess <= quantum) {
                runtime += remainingBurstTimeOfProcess;

                Process process = scheduledProcessesList.get(order);
                process.setWaitingTime(runtime - process.getCPUBurst() - process.getArrivalTime());
                process.setTurnAroundTime(runtime - process.getArrivalTime());

                remainingBurstTimes.set(order, 0);
            }
        }

        return scheduledProcessesList;
    }

    private static List<Process> sortByArrivalTime(List<Process> processes) {
        return processes.stream()
                .sorted(Comparator.comparingInt(Process::getArrivalTime))
                .collect(Collectors.toList());
    }

    private static List<Integer> getRemainingBurstTime(List<Process> processes) {
        return processes.stream()
                .mapToInt(Process::getCPUBurst)
                .boxed()
                .collect(Collectors.toList());
    }

    private static Integer findShortestRemainingTime(List<Process> scheduledProcessesList, List<Integer> remainingBurstTimes, int runtime) {
        Integer minIndex = null;
        Integer minValue = Integer.MAX_VALUE;
        for (int order = 0; order < remainingBurstTimes.size(); ++order) {
            int arrivalTime = scheduledProcessesList.get(order).getArrivalTime();
            int burstTime = remainingBurstTimes.get(order);

            if (burstTime == 0) {
                continue;
            }

            if (runtime < arrivalTime) {
                continue;
            }

            if (burstTime < minValue) {
                minValue = burstTime;
                minIndex = order;
            }
        }

        return minIndex;
    }

    public static Map<Integer, String> schedule(List<Process> processes, int quantum) {
        List<Process> scheduledProcessesList = sortByArrivalTime(processes);

        List<Integer> remainingBurstTimes = getRemainingBurstTime(scheduledProcessesList);

        Map<Integer, String> schedules = new LinkedHashMap<>();

        int runtime = scheduledProcessesList.get(0).getArrivalTime();
        while (true) {
            Integer order = findShortestRemainingTime(scheduledProcessesList, remainingBurstTimes, runtime);
            if (order == null) {
                break;
            }

            int remainingBurstTimeOfProcess = remainingBurstTimes.get(order);

            if (remainingBurstTimeOfProcess > quantum) {
                runtime += quantum;
                remainingBurstTimes.set(order, remainingBurstTimeOfProcess - quantum);
            }

            if (remainingBurstTimeOfProcess <= quantum) {
                runtime += remainingBurstTimeOfProcess;
                remainingBurstTimes.set(order, 0);
            }

            schedulingForProcess(schedules, runtime, scheduledProcessesList.get(order).getName());
        }

        return schedules;
    }

    private static void schedulingForProcess(Map<Integer, String> schedules, int runtime, String processName) {
        String lastProcess = schedules.values().stream()
                .reduce((first, second) -> second)
                .orElse(null);
        if (lastProcess == processName) {
            Integer lastRuntime = schedules.keySet().stream()
                    .reduce((first, second) -> second)
                    .orElse(null);
            schedules.remove(lastRuntime);
        }
        schedules.put(runtime, processName);
    }
}
