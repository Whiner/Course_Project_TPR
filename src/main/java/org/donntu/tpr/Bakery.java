package org.donntu.tpr;

import java.util.ArrayList;
import java.util.List;

public class Bakery {
    private List<Car> loadingCars = new ArrayList<>();
    private int simultaneousCarLoadingCount;

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

    public List<Car> substractTime(double minutes) {
        List<Car> removed = new ArrayList<>();
        loadingCars.forEach(car -> {
            car.subtractTime(minutes);
            if (car.getRemainingTime() <= 0) {
                removed.add(car);
            }
        });
        removed.forEach(car -> loadingCars.remove(car));
        return removed;
    }

    public boolean isHaveFreeChannel() {
        return loadingCars.size() < simultaneousCarLoadingCount;
    }

    public int getSimultaneousCarLoadingCount() {
        return simultaneousCarLoadingCount;
    }
}
