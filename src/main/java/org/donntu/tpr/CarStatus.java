package org.donntu.tpr;

public enum CarStatus {
    LOADING, MOVING_TO_BAKERY, MOVING_TO_STORE, MOVING_TO_SECOND_STORE, UNLOADING, WAITING;

    public static CarStatus nextStatus(CarStatus status) {
        switch (status) {
            case LOADING:
                return MOVING_TO_STORE;
            case MOVING_TO_STORE:
                return UNLOADING;
            case UNLOADING:
                return MOVING_TO_SECOND_STORE;
            case MOVING_TO_SECOND_STORE:
                return MOVING_TO_BAKERY;
            case MOVING_TO_BAKERY:
                return WAITING;
            case WAITING:
                return LOADING;
        }
        return null;
    }
}
