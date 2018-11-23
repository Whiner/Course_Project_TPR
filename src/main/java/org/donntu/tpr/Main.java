package org.donntu.tpr;

public class Main {
    public static void main(String[] args) throws Exception {
        CarDistributor carDistributor = new CarDistributor(6, 2, 6);
        carDistributor.start(500);

        System.out.println("========================");
        System.out.printf("Среднее время ожидания = %4.2f", carDistributor.getAvgWaitingTime());
    }
}
