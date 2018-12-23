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
    private int storesDowntimeCount;
    private int bakeryDowntimeCount;
    private int storesWaitingCount;
    private int bakeryWaitingCount;
    //    private int carsCount;

//    private int channelCount;
//    private int bakeryChannelsCount;
//    private int storesCount;
//    private int transactionsCount;

    public void reset() {
        totalStoresDowntime = 0;
        totalBakeryDowntime = 0;
        carsTotalWaitingTimeOnBakery = 0;
        carsTotalWaitingTimeOnStores = 0;
//        carsCount = 0;

        storesDowntimeCount = 0;
        bakeryDowntimeCount = 0;
        storesWaitingCount = 0;
        bakeryWaitingCount = 0;
//        bakeryChannelsCount = 0;
//        storesCount = 0;
    }

    public double getAvgStoresDowntime() {
        return totalStoresDowntime / storesDowntimeCount;
    }

    public double getAvgBakeryDowntime() {
        return totalBakeryDowntime / bakeryDowntimeCount;
    }

    public double getAvgCarsWaitingOnBakery() {
        return carsTotalWaitingTimeOnBakery / bakeryWaitingCount;
    }

    public double getAvgCarsWaitingOnStores() {
        return carsTotalWaitingTimeOnStores / storesWaitingCount;
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


    public void incStoresDowntimeCount() {
        this.storesDowntimeCount++;
    }

    public void incBakeryDowntimeCount() {
        this.bakeryDowntimeCount++;
    }

    public void incStoresWaitingCount() {
        this.storesWaitingCount++;
    }

    public void incBakeryWaitingCount() {
        this.bakeryWaitingCount++;
    }


}
