package com.benjamin.algorithms;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RoundRobin {

    public static List<Process> execute(List<Process> processes, int quantum) {
        List<Process> sortedProcessesList = sortByArrivalTime(processes);

        List<Integer> remainingBurstTimes = getRemainingBurstTime(sortedProcessesList);

        int runtime = sortedProcessesList.get(0).getArrivalTime();
        while (true) {
            for (int order = 0; order < sortedProcessesList.size(); ++order) {
                Process process = sortedProcessesList.get(order);

                int remainingBurstTimeOfProcess = remainingBurstTimes.get(order);
                if (remainingBurstTimeOfProcess <= 0) {
                    continue;
                }

                if (remainingBurstTimeOfProcess > quantum) {
                    runtime += quantum;
                    remainingBurstTimes.set(order, remainingBurstTimeOfProcess - quantum);
                }

                if (remainingBurstTimeOfProcess <= quantum) {
                    runtime += remainingBurstTimeOfProcess;

                    process.setWaitingTime(runtime - process.getCPUBurst() - process.getArrivalTime());
                    process.setTurnAroundTime(runtime - process.getArrivalTime());

                    remainingBurstTimes.set(order, 0);
                }
            }

            boolean isAllProcessDone = remainingBurstTimes.stream().allMatch(burstTime -> burstTime == 0);
            if (isAllProcessDone) {
                break;
            }
        }

        return sortedProcessesList;
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

    public static Map<Integer, String> schedule(List<Process> processes, int quantum) {
        List<Process> sortedProcessesList = sortByArrivalTime(processes);

        List<Integer> remainingBurstTimes = getRemainingBurstTime(sortedProcessesList);

        Map<Integer, String> schedules = new LinkedHashMap<>();

        int runtime = sortedProcessesList.get(0).getArrivalTime();
        while (true) {
            for (int order = 0; order < sortedProcessesList.size(); ++order) {
                int remainingBurstTimeOfProcess = remainingBurstTimes.get(order);
                if (remainingBurstTimeOfProcess <= 0) {
                    continue;
                }

                if (remainingBurstTimeOfProcess > quantum) {
                    runtime += quantum;
                    remainingBurstTimes.set(order, remainingBurstTimeOfProcess - quantum);
                }

                if (remainingBurstTimeOfProcess <= quantum) {
                    runtime += remainingBurstTimeOfProcess;
                    remainingBurstTimes.set(order, 0);
                }

                schedulingForProcess(schedules, runtime, sortedProcessesList.get(order).getName());
            }

            boolean isAllProcessDone = remainingBurstTimes.stream().allMatch(burstTime -> burstTime == 0);
            if (isAllProcessDone) {
                break;
            }
        }

        return schedules;
    }

    private static void schedulingForProcess(Map<Integer, String> schedules, int runtime, String processName) {
        String lastProcess = schedules.values().stream().reduce((first, second) -> second).orElse(null);
        if (lastProcess == processName) {
            Integer lastRuntime = schedules.keySet().stream().reduce((first, second) -> second).orElse(null);
            schedules.remove(lastRuntime);
        }
        schedules.put(runtime, processName);
    }
}
