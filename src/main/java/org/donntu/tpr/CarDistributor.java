package org.donntu.tpr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.donntu.tpr.RandomGeneratorConstants.*;

public class CarDistributor {
    private RandomGenerator randomGenerator = new RandomGenerator();
    private Bakery bakery;

    private int carsCount;
    private List<Car> cars;

    private double lastMinutes;

    private final int STORES_COUNT = 6;
    private int nextStore = 0;
    private List<Store> stores;

    public CarDistributor(int carsCount, int simultaneousCarLoadingCount) {
        this.carsCount = carsCount;
        cars = new ArrayList<>(carsCount);
        stores = new ArrayList<>(STORES_COUNT);
        Collections.fill(cars, new Car(CarStatus.WAITING));
        Collections.fill(stores, new Store());
        bakery = new Bakery(simultaneousCarLoadingCount);
    }

    public void start(double minutes) throws Exception {
        this.lastMinutes = minutes;
        redistributeCars();
        while (lastMinutes > 0) {
            double lessRemainingTime = getLessRemainingTime();
            subtractTime(lessRemainingTime);
            redistributeCars();
            lastMinutes -= lessRemainingTime;
        }
    }

    private void subtractTime(double minutes) {
        cars.stream().filter(
                car -> !car.getStatus().equals(CarStatus.UNLOADING)
                        && !car.getStatus().equals(CarStatus.LOADING)
        ).forEach(car -> car.subtractTime(minutes));
        bakery.substractTime(minutes);
        for (Store store : stores) {
            store.subtractTime(minutes);
        }
    }

    private void redistributeCars() throws Exception {
        boolean isLastCarAddedToBakery = true;
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getRemainingTime() <= 0) {
                CarStatus nextStatus = CarStatus.nextStatus(cars.get(i).getStatus());
                if (nextStatus == null) {
                    throw new Exception("Статус не указан для машины " + cars.get(i).getId());
                }
                switch (nextStatus) {
                    case LOADING:
                        if (isLastCarAddedToBakery) {
                            fillCarParamsForBakery(cars.get(i));
                            isLastCarAddedToBakery = bakery.addCar(cars.get(i));
                        }
                        break;
                    case MOVING_TO_STORE:
                        if (!bakery.isOnLoading(cars.get(i))) {
                            double remainingTime = Math.abs(cars.get(i).getRemainingTime());
                            cars.get(i).setStatus(CarStatus.MOVING_TO_STORE);
                            cars.get(i).setRemainingTime(randomGenerator.getExpY(M_MOVING_B_S) - remainingTime);
                        }
                        break;
                    case UNLOADING:
                        break;
                    case MOVING_TO_SECOND_STORE:
                        if (!cars.get(i).getDeliveryMode().equals(DeliveryMode.ONE_PRODUCT_TWO_STORES)) {
                            cars.get(i).setStatus(CarStatus.MOVING_TO_SECOND_STORE);
                            i--;
                            continue;
                        }
                        break;
                    case MOVING_TO_BAKERY:
                        break;
                    case WAITING:
                        break;
                }
                if (cars.get(i).getRemainingTime() <= 0) {
                    i--;
                }
            }
        }
    }

    private void fillCarParamsForBakery(Car car) throws Exception {
        selectDeliveryMode(car);
        selectLoadingTime(car);
    }

    private double getLessRemainingTime() {
        double min = 1E10;
        for (Car car : cars) {
            if (car.getRemainingTime() < min) {
                min = car.getRemainingTime();
            }
        }
        return min;
    }

    private boolean putCarsOnLoadingChannels() throws Exception {
        boolean isAtLeastOneAdded = false;
        for (int i = 0; i < bakery.getSimultaneousCarLoadingCount(); i++) {
            Car waitingCar = getWaitingCar();
            if (waitingCar != null) {
                selectDeliveryMode(waitingCar);
                selectLoadingTime(waitingCar);
                boolean isCarAdded = bakery.addCar(waitingCar);
                if (!isCarAdded) {
                    break;
                }
                isAtLeastOneAdded = true;
            } else {
                break;
            }
        }
        return isAtLeastOneAdded;
    }

    private List<Car> getByStatus(CarStatus status) {
        return cars.stream().filter(car -> car.getStatus() == status).collect(Collectors.toList());
    }

    private void selectLoadingTime(Car car) throws Exception {
        switch (car.getDeliveryMode()) {
            case ONE_PRODUCT_ONE_STORE:
                car.setRemainingTime(
                        randomGenerator.getNormalY(
                                M_PRODUCT_1_STORE_1,
                                CKO_PRODUCT_1_STORE_1,
                                DEFAULT_N));
                break;
            case ONE_PRODUCT_TWO_STORES:
                car.setRemainingTime(
                        randomGenerator.getNormalY(
                                M_PRODUCT_1_STORE_2,
                                CKO_PRODUCT_1_STORE_2,
                                DEFAULT_N));
                break;
            case TWO_PRODUCT_ONE_STORE:
                car.setRemainingTime(
                        randomGenerator.getNormalY(
                                M_PRODUCT_2_STORE_1,
                                CKO_PRODUCT_2_STORE_1,
                                DEFAULT_N));
                break;
            default:
                throw new Exception("Тип доставки не указан (DeliveryMode)");
        }
    }

    private void selectDeliveryMode(Car car) {
        double kcu = randomGenerator.getKcu();
        if (kcu <= 0.4) {
            car.setDeliveryMode(DeliveryMode.ONE_PRODUCT_ONE_STORE);
        } else if (kcu <= 0.4 + 0.2) {
            car.setDeliveryMode(DeliveryMode.ONE_PRODUCT_TWO_STORES);
        } else {
            car.setDeliveryMode(DeliveryMode.TWO_PRODUCT_ONE_STORE);
        }
    }

    private Car getWaitingCar() {
        for (Car car : cars) {
            if (car.getStatus().equals(CarStatus.WAITING)) {
                return car;
            }
        }
        return null;
    }
}
