package org.donntu.tpr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Store {
    private Queue<Car> queue = new LinkedList<>();
    private Car currentUnloading = null;
    private String id;

    public List<Car> subtractTime(double minutes) { // отнять от текущего и поставить следующий если = 0
        List<Car> removed = new ArrayList<>();
        if (currentUnloading != null) {
            currentUnloading.setRemainingTime(currentUnloading.getRemainingTime() - minutes);
            if (currentUnloading.getRemainingTime() <= 0) {
                removed.add(currentUnloading);
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
