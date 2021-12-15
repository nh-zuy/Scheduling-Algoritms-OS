package com.benjamin.algorithms;

public class Process {
    private final String name;
    private final int arrivalTime;
    private final int CPUBurst;
    private final int priority;
    private int turnAroundTime;
    private int waitingTime;

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    private Process(Builder builder) {
        this.name = builder.name;
        this.arrivalTime = builder.arrivalTime;
        this.CPUBurst = builder.CPUBurst;
        this.priority = builder.priority;
        this.turnAroundTime = builder.turnAroundTime;
        this.waitingTime = builder.waitingTime;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String name;
        int arrivalTime;
        int CPUBurst;
        int priority;
        int turnAroundTime;
        int waitingTime;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder arrivalTime(int arrivalTime) {
            this.arrivalTime = arrivalTime;
            return this;
        }

        public Builder CPUBurst(int CPUBurst) {
            this.CPUBurst = CPUBurst;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Process build() {
            return new Process(this);
        }
    }

    @Override
    public String toString() {
        return this.name + " " + this.arrivalTime + " " + this.CPUBurst + " " + this.priority;
    }

    public String getName() {
        return name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getCPUBurst() {
        return CPUBurst;
    }

    public int getPriority() {
        return priority;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }
}
