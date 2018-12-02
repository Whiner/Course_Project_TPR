package org.donntu.tpr;

import lombok.Data;

@Data
public class Statistics {
    private static Statistics instance = new Statistics();

    private Statistics() {
        reset();
    }

    public static Statistics getInstance() {
        return instance;
    }

    private double totalStoresDowntime;
    private double totalBakeryDowntime;
    private double carsTotalWaitingTimeOnBakery;
    private double carsTotalWaitingTimeOnStores;
    private int carsCount;
    private int bakeryChannelsCount;
    private int storesCount;

    public void reset() {
        totalStoresDowntime = 0;
        totalBakeryDowntime = 0;
        carsTotalWaitingTimeOnBakery = 0;
        carsTotalWaitingTimeOnStores = 0;
        carsCount = 0;
        bakeryChannelsCount = 0;
        storesCount = 0;
    }

    public double getAvgStoresDowntime() {
        return totalStoresDowntime / carsCount;
    }

    public double getAvgBakeryDowntime() {
        return totalBakeryDowntime / carsCount;
    }

    public double getAvgCarsWaitingOnBakery() {
        return carsTotalWaitingTimeOnBakery / carsCount;
    }

    public double getAvgCarsWaitingOnStores() {
        return carsTotalWaitingTimeOnStores / carsCount;
    }

    public void addBakeryDowntime(double minutes) {
        this.totalBakeryDowntime += minutes;
    }

    public void addStoresDowntime(double minutes) {
        this.totalStoresDowntime += minutes;
    }

    public void addCarsWaitingTimeOnBakery(double minutes) {
        this.carsTotalWaitingTimeOnBakery += minutes;
    }

    public void addCarsWaitingTimeOnStores(double minutes) {
        carsTotalWaitingTimeOnStores += minutes;
    }
}
