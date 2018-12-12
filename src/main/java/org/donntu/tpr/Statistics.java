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
    private double intervalsWithoutProduct;
    private int intervalsCount;
    private int transactionsCount;
    private int channelCount;

    public void reset() {
        totalStoresDowntime = 0;
        totalBakeryDowntime = 0;
        carsTotalWaitingTimeOnBakery = 0;
        carsTotalWaitingTimeOnStores = 0;
        carsCount = 0;
        bakeryChannelsCount = 0;
        storesCount = 0;
        intervalsCount = 0;
        intervalsWithoutProduct = 0;
    }

    public double getAvgStoresDowntime() {
        return totalStoresDowntime / storesCount / transactionsCount;
    }

    public double getAvgBakeryDowntime() {
        return totalBakeryDowntime / channelCount / transactionsCount;
    }

    public double getAvgCarsWaitingOnBakery() {
        return carsTotalWaitingTimeOnBakery / channelCount / carsCount / transactionsCount;
    }

    public double getAvgCarsWaitingOnStores() {
        return carsTotalWaitingTimeOnStores / carsCount / storesCount / transactionsCount;
    }

    public double getAvgIntervalWithoutProduct() {
        return intervalsWithoutProduct / intervalsCount;
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

    public void addIntervalWithoutProduct(double intervalWithoutProduct) {
        this.intervalsWithoutProduct += intervalWithoutProduct;
    }

    public void incIntervalsCount() {
        this.intervalsCount++;
    }

}
