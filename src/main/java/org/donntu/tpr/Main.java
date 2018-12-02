package org.donntu.tpr;

public class Main {
    public static void main(String[] args) throws Exception {
        CarDistributor carDistributor = new CarDistributor(1, 1, 2);
        carDistributor.start(1, true);

        System.out.println("========================");
        System.out.printf("Среднее время ожидания на хлебозаводе = %4.2f\n", Statistics.getInstance().getAvgCarsWaitingOnBakery());
        System.out.printf("Среднее время ожидания в магазине = %4.2f\n", Statistics.getInstance().getAvgCarsWaitingOnStores());
        System.out.printf("Среднее время простоя магазина = %4.2f\n", Statistics.getInstance().getAvgStoresDowntime());
        System.out.printf("Среднее время простоя хлебозавода = %4.2f\n", Statistics.getInstance().getAvgBakeryDowntime());
        System.out.printf("Времени прошло = %4.2f\n", carDistributor.getTimePassed());
    }
}
