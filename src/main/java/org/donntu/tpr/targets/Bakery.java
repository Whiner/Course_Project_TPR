package org.donntu.tpr.targets;

import org.donntu.tpr.Car;
import org.donntu.tpr.Statistics;
import org.donntu.tpr.modes.CarStatus;

import java.util.ArrayList;
import java.util.List;

public class Bakery {
    private List<Car> loadingCars = new ArrayList<>();
    private int channelCount;

    public Bakery(int channelCount) {
        this.channelCount = channelCount;
    }

    public boolean addCar(Car car) {
        if (loadingCars.size() == channelCount) {
            return false;
        } else {
            Statistics.getInstance().incBakeryDowntimeCount();
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
        if (isHaveFreeChannel()) {
            Statistics.getInstance().addBakeryDowntime(minutes * (channelCount - loadingCars.size()));
        }
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
        return loadingCars.size() < channelCount;
    }

    public int getChannelCount() {
        return channelCount;
    }

}
