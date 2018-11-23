package org.donntu.tpr;

public class Main {
    public static void main(String[] args) throws Exception {
        CarDistributor carDistributor = new CarDistributor(6, 2);
        carDistributor.start(500);
    }
}
