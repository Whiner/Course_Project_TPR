package org.donntu.tpr.targets;

import org.donntu.tpr.Car;
import org.donntu.tpr.modes.CarStatus;

import java.util.ArrayList;
import java.util.List;

public class Bakery {
    private List<Car> loadingCars = new ArrayList<>();
    private int simultaneousCarLoadingCount;
    private double waitingTime;

    public Bakery(int simultaneousCarLoadingCount) {
        this.simultaneousCarLoadingCount = simultaneousCarLoadingCount;
    }

    public boolean addCar(Car car) {
        if (loadingCars.size() == simultaneousCarLoadingCount) {
            return false;
        } else {
            loadingCars.add(car);
            car.setStatus(CarStatus.LOADING);
            return true;
        }
    }

    public boolean isOnLoading(Car car) {
        return loadingCars.contains(car);
    }

    public List<Car> subtractTime(double minutes) {
        List<Car> removed = new ArrayList<>();
        if (loadingCars.isEmpty()) {
            waitingTime += minutes;
        } else {
            loadingCars.forEach(car -> {
                car.subtractTime(minutes);
                if (car.getRemainingTime() <= 0) {
                    removed.add(car);
                }
            });
            removed.forEach(car -> loadingCars.remove(car));
        }
        return removed;
    }

    public boolean isHaveFreeChannel() {
        return loadingCars.size() < simultaneousCarLoadingCount;
    }

    public int getSimultaneousCarLoadingCount() {
        return simultaneousCarLoadingCount;
    }

    public void resetWaitingTime() {
        this.waitingTime = 0;
    }

    public double getAvgWaitingTime() {
        return waitingTime / simultaneousCarLoadingCount;
    }
}
