package org.donntu.tpr;

public class Main {
    public static void main(String[] args) throws Exception {
        /*int channelCnt = 2;
        CarDistributor carDistributor = new CarDistributor(channelCnt, 6);
        int st = 6;
        int kol = 6;
        int trans = 100;
        System.out.println("========================");
        System.out.printf("|%8s|%8s|%8s|%8s|%8s|\n", "Маш.кол", "Ож.хлебз", "Ож.магаз", "Пр.хлебз", "Пр.магаз");
        for (int i = st; i < st + kol + 1; i++) {
            carDistributor.start(i, trans, false);
            System.out.printf("|%8d|%8.2f|%8.2f|%8.2f|%8.2f|\n",
                    i,
                    Statistics.getInstance().getAvgCarsWaitingOnBakery(),
                    Statistics.getInstance().getAvgCarsWaitingOnStores(),
                    Statistics.getInstance().getAvgBakeryDowntime(),
                    Statistics.getInstance().getAvgStoresDowntime()
            );

        }*/

        Double[][] table = {
                {5.74, 1.12, 8.52, 83.87, 6.0},
                {7.44, 1.2, 2.97, 67.86, 7.0},
                {9.53, 2.36, 6.18, 65.77, 8.0},
                {19.22, 3.43, 3.67, 58.18, 9.0},
                {18.58, 3.49, 1.82, 47.99, 10.0},
                {24.15, 4.4, 5.8, 63.97, 11.0},
                {29.15, 4.48, 4.16, 56.9, 12.0}};
        Criteria criteria = new Criteria(table);
        //criteria.addit(0.2, 0.2, 0.1, 0.1, 0.4);
        //criteria.multiplic(0.2, 0.2, 0.1, 0.1, 0.4);
        criteria.mainCrit(4, 1.2);
    }
}
