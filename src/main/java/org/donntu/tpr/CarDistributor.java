package org.donntu.tpr;

import org.donntu.tpr.modes.CarStatus;
import org.donntu.tpr.modes.DeliveryMode;
import org.donntu.tpr.random.RandomGenerator;
import org.donntu.tpr.targets.Bakery;
import org.donntu.tpr.targets.StoreManager;

import java.util.ArrayList;
import java.util.List;

import static org.donntu.tpr.random.RandomGeneratorConstants.*;

public class CarDistributor {
    private RandomGenerator randomGenerator = new RandomGenerator();
    private Bakery bakery;

    private List<Car> cars = new ArrayList<>();

    private StoreManager storeManager;

    public CarDistributor(int carsCount, int simultaneousCarLoadingCount, int storesCount) {
        for (int i = 0; i < carsCount; i++) {
            Car car = new Car(CarStatus.LOAD_WAITING);
            car.setId((i + 1) + "");
            cars.add(car);
        }
        storeManager = new StoreManager(storesCount);
        bakery = new Bakery(simultaneousCarLoadingCount);
    }

    public void start(double minutes) throws Exception {
        double lastMinutes = minutes;
        redistributeCars();
        double lessRemainingTime;
        while (lastMinutes > 0) {
            lessRemainingTime = getLessRemainingTime();
            if (lastMinutes < lessRemainingTime) {
                lessRemainingTime = lastMinutes;
            }
            System.out.printf("-------------------- Прошло %4.2f минуты --------------------\n", lessRemainingTime);
            subtractTime(lessRemainingTime);
            redistributeCars();
            lastMinutes -= lessRemainingTime;
        }
    }

    private void redistributeCars() throws Exception {
        for (Car car : cars) {
            boolean isRepaired;
            System.out.print("+ ");
            if (car.getRemainingTime() <= 0) {
                isRepaired = car.getStatus() == CarStatus.REPAIRING;
                CarStatus status = car.nextStatus();
                if (status == null) {
                    throw new Exception("Статус не указан для машины " + car.getId());
                }
                if (!isRepaired) {
                    switch (status) {
                        case LOADING:
                            if (bakery.isHaveFreeChannel()) {
                                if (car.getDeliveryMode() == null) {
                                    fillCarParamsForBakery(car);
                                }
                                car.addCurrentWaitingTimeToTotal();
                                bakery.addCar(car);
                            } else {
                                car.setStatus(CarStatus.LOAD_WAITING);
                                car.printStatus();
                                continue;
                            }
                            break;
                        case MOVING_TO_FIRST_STORE:
                            car.setRemainingTime(randomGenerator.getExpY(M_MOVING_B_S));
                            car.setStoreTargetIndex(storeManager.getNextStore());
                            checkCrash(car);
                            break;
                        case UNLOADING_FIRST:
                            car.setRemainingTime(randomGenerator.getExpY(M_UNLOADING));
                            storeManager.getStore(car.getStoreTargetIndex()).addToQueue(car);
                            break;
                        case MOVING_TO_SECOND_STORE:
                            car.setRemainingTime(randomGenerator.getExpY(M_MOVING_S_S));
                            car.setStoreTargetIndex(storeManager.getNextStore());
                            checkCrash(car);
                            break;
                        case UNLOADING_SECOND:
                            car.setRemainingTime(randomGenerator.getExpY(M_UNLOADING));
                            storeManager.getStore(car.getStoreTargetIndex()).addToQueue(car);
                            break;
                        case MOVING_TO_BAKERY:
                            car.setDeliveryMode(null);
                            car.setRemainingTime(randomGenerator.getExpY(M_MOVING_S_B));
                            checkCrash(car);
                            break;
                        case LOAD_WAITING:
                            break;
                        case REPAIRING:
                            break;
                    }
                }
            }
            car.printStatus();
        }
    }

    private void subtractTime(double minutes) {
        cars.stream().filter(
                car -> !car.getStatus().equals(CarStatus.UNLOADING_FIRST)
                        && !car.getStatus().equals(CarStatus.UNLOADING_SECOND)
                        && !car.getStatus().equals(CarStatus.LOADING)
                        && !car.getStatus().equals(CarStatus.LOAD_WAITING)
        ).forEach(car -> car.subtractTime(minutes));
        cars.stream().filter(car -> car.getStatus().equals(CarStatus.LOAD_WAITING))
                .forEach(car -> car.addCurrentWaitingTime(minutes));
        bakery.subtractTime(minutes);
        storeManager.subtractTime(minutes);
    }

    private boolean checkCrash(Car car) {
        if (randomGenerator.getKcu() <= CRASH_CHANCE) {
            car.crash(randomGenerator.getExpY(M_REPAIRING));
            return true;
        }
        return false;
    }

    public double getAvgWaitingTime() {
        double totalWaitingTime = 0;
        for (Car car : cars) {
            totalWaitingTime += car.getTotalWaitingTime();
        }
        return totalWaitingTime / cars.size();
    }


    private void fillCarParamsForBakery(Car car) throws Exception {
        selectDeliveryMode(car);
        selectLoadingTime(car);
    }

    private double getLessRemainingTime() {
        double min = 1E10;
        for (Car car : cars) {
            if (car.getRemainingTime() < min && car.getRemainingTime() > 0) {
                min = car.getRemainingTime();
            }
        }
        return min;
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

}
