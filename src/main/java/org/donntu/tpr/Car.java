package org.donntu.tpr;

import lombok.Data;
import org.donntu.tpr.modes.CarStatus;
import org.donntu.tpr.modes.DeliveryMode;

import static org.donntu.tpr.modes.CarStatus.*;

@Data
public class Car {
    private CarStatus status;
    private DeliveryMode deliveryMode;

    private double remainingTime;

    private String id;

    private double currentWaitingTime = 0;

    private int storeTargetIndex;

    private CarStatus targetBeforeCrash;
    private double remainingTimeBeforeCrash;

    private boolean isQueueWaiting = false;

    private int transactionsCount = 0;

    public void addTransaction() {
        this.transactionsCount++;
    }

    public void resetTransactions() {
        this.transactionsCount = 0;
    }

    public Car(CarStatus status) {
        this.status = status;
    }

    public void subtractTime(double time) {
        this.remainingTime -= time;
    }

    public void addCurrentWaitingTime(double minutes) {
        this.currentWaitingTime += minutes;
    }

    public void resetWaitingTime() {
        currentWaitingTime = 0;
    }

    public void crash(double repairTime) {
        targetBeforeCrash = status;
        remainingTimeBeforeCrash = remainingTime;
        remainingTime = repairTime;
        status = REPAIRING;
    }

    public CarStatus nextStatus() {
        switch (status) {
            case LOADING:
                status = MOVING_TO_FIRST_STORE;
                break;
            case MOVING_TO_FIRST_STORE:
                status = UNLOADING_FIRST;
                break;
            case UNLOADING_FIRST:
                if (deliveryMode.equals(DeliveryMode.ONE_PRODUCT_TWO_STORES)) {
                    status = MOVING_TO_SECOND_STORE;
                } else if (deliveryMode.equals(DeliveryMode.ONE_PRODUCT_ONE_STORE)) {
                    status = MOVING_TO_BAKERY;
                } else {
                    status = UNLOADING_SECOND;
                }
                break;
            case MOVING_TO_SECOND_STORE:
                status = UNLOADING_SECOND;
                break;
            case UNLOADING_SECOND:
                status = MOVING_TO_BAKERY;
                break;
            case MOVING_TO_BAKERY:
                status = LOADING;
                break;
            case LOAD_WAITING:
                status = LOADING;
                break;
            case REPAIRING:
                status = targetBeforeCrash;
                remainingTime = remainingTimeBeforeCrash;
                break;
            case STOPPED:
                break;
        }
        return status;
    }

    public void printStatus() {
        switch (status) {
            case LOAD_WAITING:
                System.out.printf("Машина %s ожидает своей очереди на хлебозавод (%s) уже %4.2f\n", id, status, currentWaitingTime);
                break;
            case LOADING:
                System.out.printf("Машина %s загружается на хлебозаводе (%s). Осталось %4.2f\n", id, status, remainingTime);
                break;
            case MOVING_TO_FIRST_STORE:
                if (deliveryMode.equals(DeliveryMode.ONE_PRODUCT_TWO_STORES)) {
                    System.out.printf("Машина %s едет в первый магазин №%s (%s). Осталось %4.2f\n", id, storeTargetIndex + 1, status, remainingTime);
                } else {
                    System.out.printf("Машина %s едет в магазин №%s (%s). Осталось %4.2f\n", id, storeTargetIndex + 1, status, remainingTime);
                }
                break;
            case UNLOADING_FIRST:
                if (isQueueWaiting) {
                    System.out.printf("Машина %s ждет свою очередь в магазине №%s (%s) уже %4.2f\n", id, storeTargetIndex + 1, status, currentWaitingTime);
                } else {
                    if (deliveryMode.equals(DeliveryMode.ONE_PRODUCT_TWO_STORES)) {
                        System.out.printf("Машина %s разгружается в первом магазине №%s (%s). Осталось %4.2f\n", id, storeTargetIndex + 1, status, remainingTime);
                    } else {
                        System.out.printf("Машина %s разгружается в магазине №%s (%s). Осталось %4.2f\n", id, storeTargetIndex + 1, status, remainingTime);
                    }
                }
                break;
            case MOVING_TO_SECOND_STORE:
                System.out.printf("Машина %s едет во второй магазин №%s (%s). Осталось %4.2f\n", id, storeTargetIndex + 1, status, remainingTime);
                break;
            case UNLOADING_SECOND:
                if (isQueueWaiting) {
                    System.out.printf("Машина %s ждет свою очередь в магазине №%s (%s) уже %4.2f\n", id, storeTargetIndex + 1, status, currentWaitingTime);
                } else {
                    if (deliveryMode.equals(DeliveryMode.TWO_PRODUCT_ONE_STORE)) {
                        System.out.printf("Машина %s разгружает второй товар в магазине №%s (%s). Осталось %4.2f\n", id, storeTargetIndex + 1, status, remainingTime);
                    } else {
                        System.out.printf("Машина %s разгружается во втором магазине №%s (%s). Осталось %4.2f\n", id, storeTargetIndex + 1, status, remainingTime);
                    }
                }
                break;
            case MOVING_TO_BAKERY:
                System.out.printf("Машина %s едет обратно на хлебозавод (%s). Осталось %4.2f\n", id, status, remainingTime);
                break;
            case REPAIRING:
                System.out.printf("Машина %s ремонтируется (%s). Осталось %4.2f\n", id, status, remainingTime);
                break;
            case STOPPED:
                System.out.println("Машина " + id + " завершила работу");
                break;
        }
    }
}
