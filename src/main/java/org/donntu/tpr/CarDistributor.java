package org.donntu.tpr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarDistributor {
    private final int STORES_COUNT = 6;
    private int carsCount;
    private int simultaneousCarLoadingCount;
    private double lastMinutes;
    private List<Car> cars;

    public CarDistributor(int carsCount, int simultaneousCarLoadingCount) {
        this.carsCount = carsCount;
        cars = new ArrayList<>(carsCount);
        Collections.fill(cars, new Car());
        this.simultaneousCarLoadingCount = simultaneousCarLoadingCount;
    }

    public void start(double minutes){
        this.lastMinutes = minutes;


    }

    private void load(){

    }
}
