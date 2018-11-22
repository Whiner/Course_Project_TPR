package org.donntu.tpr;

public class Car {
    private CarStatus status;
    private DeliveryMode deliveryMode;
    private double remainingTime;
    private String id;
    private boolean movedToFirstStore = false;

    public Car(CarStatus status) {
        this.status = status;
    }

    public boolean isMovedToFirstStore() {
        return movedToFirstStore;
    }

    public void setMovedToFirstStore(boolean movedToFirstStore) {
        this.movedToFirstStore = movedToFirstStore;
    }

    public CarStatus getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(CarStatus status) {
        this.status = status;
    }

    public DeliveryMode getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(DeliveryMode deliveryMode) {
        this.deliveryMode = deliveryMode;
    }


    public void subtractTime(double time) {
        this.remainingTime -= time;
    }

    public double getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(double remainingTime) {
        this.remainingTime = remainingTime;
    }
}
