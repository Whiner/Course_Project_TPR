package org.donntu.tpr.targets;

import org.donntu.tpr.Car;

import java.util.ArrayList;
import java.util.List;

public class StoreManager {
    private List<Store> stores = new ArrayList<>();
    private int nextStore = -1;

    public StoreManager(int storesCount) {
        for (int i = 0; i < storesCount; i++) {
            stores.add(new Store("" + (i + 1)));
        }
    }

    public Store getStore(int i) {
        return stores.get(i);
    }

    public boolean isWaitingInQueue(Car car) {
        for (Store store : stores) {
            if (store.isWaitingInQueue(car)) {
                return true;
            }
        }
        return false;
    }

    public List<Car> subtractTime(double minutes) {
        List<Car> removed = new ArrayList<>();
        for (Store store : stores) {
            removed.addAll(store.subtractTime(minutes));
        }
        return removed;
    }

    public int getNextStore() {
        if (nextStore + 1 >= stores.size()) {
            nextStore = 0;
        } else {
            nextStore++;
        }
        return nextStore;
    }

    public double getAvgStoreWaitingTime() {
        double waitingTime = 0;
        for (Store store : stores) {
            waitingTime += store.getStoreWaitingTime();
        }
        return waitingTime / stores.size();
    }

    public void resetWaitingTime() {
        for (Store store : stores) {
            store.resetWaitTime();
        }
    }

}
