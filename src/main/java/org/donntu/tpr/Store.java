package org.donntu.tpr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Store {
    private Queue<Car> queue = new LinkedList<>();
    private Car currentUnloading = null;

    public List<Car> subtractTime(double minutes) {
        List<Car> removed = new ArrayList<>();
        while (minutes > 0 && !queue.isEmpty() && currentUnloading != null) {
            currentUnloading.setRemainingTime(currentUnloading.getRemainingTime() - minutes);
            if (currentUnloading.getRemainingTime() <= 0) {
                removed.add(currentUnloading);
                minutes = currentUnloading.getRemainingTime() * -1;
                currentUnloading = queue.poll();
            }
        }
        return removed;
    }

    public void addToQueue(Car car) {
        if (currentUnloading == null) {
            currentUnloading = car;
        } else {
            queue.offer(car);
        }
    }

}
