package org.donntu.tpr;

public class Main {
    public static void main(String[] args) throws Exception {
        int channelCnt = 2;
        CarDistributor carDistributor = new CarDistributor(channelCnt, 6);
        int st = 1;
        int kol = 20;
        int trans = 10;
        System.out.println("========================");
        System.out.printf("|%8s|%8s|%8s|%8s|%8s|%8s|\n", "Маш.кол", "Ож.хлебз", "Ож.магаз", "Пр.хлебз", "Пр.магаз", "Интервал");
        for (int i = st; i < st + kol; i++) {
            carDistributor.start(i, trans, false);
            System.out.printf("|%8d|%8.2f|%8.2f|%8.2f|%8.2f|%8.2f|\n",
                    i,
                    Statistics.getInstance().getAvgCarsWaitingOnBakery(),
                    Statistics.getInstance().getAvgCarsWaitingOnStores(),
                    Statistics.getInstance().getAvgBakeryDowntime(),
                    Statistics.getInstance().getAvgStoresDowntime(),
                    Statistics.getInstance().getAvgIntervalWithoutProduct()
            );

        }

       /* System.out.printf("Среднее время ожидания на хлебозаводе = %4.2f\n", Statistics.getInstance().getAvgCarsWaitingOnBakery());
        System.out.printf("Среднее время ожидания в магазине = %4.2f\n", Statistics.getInstance().getAvgCarsWaitingOnStores());
        System.out.printf("Среднее время простоя магазина = %4.2f\n", Statistics.getInstance().getAvgStoresDowntime());
        System.out.printf("Среднее время простоя хлебозавода = %4.2f\n", Statistics.getInstance().getAvgBakeryDowntime());
        System.out.printf("Времени прошло = %4.2f\n", carDistributor.getTimePassed());*/
    }
}
