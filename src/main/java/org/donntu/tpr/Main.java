package org.donntu.tpr;

public class Main {
    public static void main(String[] args) throws Exception {
        CarDistributor carDistributor = new CarDistributor(8, 2, 1);
        carDistributor.start(3, true);

        System.out.println("========================");
        System.out.printf("Среднее время ожидания = %4.2f\n", carDistributor.getAvgWaitingTime());
        System.out.printf("Среднее время простоя магазина = %4.2f\n", carDistributor.getAvgStoreWaitingTime());
        System.out.printf("Среднее время простоя пекарни = %4.2f\n", carDistributor.getAvgBakeryWaitingTime());
        System.out.printf("Времени прошло = %4.2f\n", carDistributor.getTimePassed());
    }
}
