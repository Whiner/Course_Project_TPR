package org.donntu.tpr.targets;

import org.donntu.tpr.Car;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Store {
    private Queue<Car> queue = new LinkedList<>();
    private Car currentUnloading = null;
    private String id;

    public List<Car> subtractTime(double minutes) {
        List<Car> removed = new ArrayList<>();
        if (currentUnloading != null) {
            currentUnloading.addCurrentWaitingTimeToTotal();
            currentUnloading.setRemainingTime(currentUnloading.getRemainingTime() - minutes);
            if (!queue.isEmpty()) {
                queue.forEach(car -> car.addCurrentWaitingTime(minutes));
            }
            if (currentUnloading.getRemainingTime() <= 0) {
                removed.add(currentUnloading);
                currentUnloading = queue.poll();
            }
        }
        return removed;
    }

    public boolean isWaitingInQueue(Car car) {
        for (Car c : queue) {
            if (c.equals(car)) {
                return true;
            }
        }
        return false;
    }

    public void addToQueue(Car car) {
        if (currentUnloading == null) {
            currentUnloading = car;
        } else {
            queue.offer(car);
            car.setQueueWaiting(true);
        }
    }

    public Store(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
