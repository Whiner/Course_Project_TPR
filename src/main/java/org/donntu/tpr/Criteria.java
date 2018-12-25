package org.donntu.tpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Criteria {
    private Double[][] table;

    public Criteria(Double[][] table) {
        this.table = table;
    }

    public void addit(double... koef) {
        System.out.println("Метод аддитивной свертки");
        List<Double> minList = new ArrayList<>();
        List<Double> maxList = new ArrayList<>();
        for (int i = 0; i < table[0].length; i++) {
            double min = 10E9;
            double max = -10E9;
            for (int j = 0; j < table.length; j++) {
                if (table[j][i] > max) {
                    max = table[j][i];
                }
                if (table[j][i] < min) {
                    min = table[j][i];
                }
            }
            minList.add(min);
            maxList.add(max);
        }

        Double[][] newTable = new Double[table.length][table[0].length + 1];
        int j;
        for (int i = 0; i < table.length; i++) {
            double ac = 0;
            for (j = 0; j < table[i].length; j++) {
                newTable[i][j] = (maxList.get(j) - table[i][j]) / (maxList.get(j) - minList.get(j));
                ac += newTable[i][j] * koef[j];
            }
            newTable[i][j] = ac;
        }

        int optimalAlternative = findOptimalAlternativeByDop(newTable);
        print(newTable);
        System.out.printf("Оптимальное решение %4.2f (%d)\n", newTable[optimalAlternative][newTable[0].length - 1], optimalAlternative + 1);
    }

    public void multiplic(double... koef) {
        System.out.println("Метод мультипликативной свертки");
        List<Double> minList = new ArrayList<>();
        for (int i = 0; i < table[0].length; i++) {
            double min = 10E9;
            for (final Double[] doubles : table) {
                if (doubles[i] < min) {
                    min = doubles[i];
                }
            }
            minList.add(min);
        }

        Double[][] newTable = new Double[table.length][table[0].length + 1];
        int j;
        for (int i = 0; i < table.length; i++) {
            double mul = 1;
            for (j = 0; j < table[i].length; j++) {
                newTable[i][j] = (minList.get(j) / table[i][j]);
                mul *= Math.pow(newTable[i][j], koef[j]);
            }
            newTable[i][j] = mul;
        }
        int optimalAlternative = findOptimalAlternativeByDop(newTable);
        print(newTable);
        System.out.printf("Оптимальное решение %4.2f (%d)\n", newTable[optimalAlternative][newTable[0].length - 1], optimalAlternative + 1);
    }

    public void mainCrit(int mainIndex, double persent) {
        System.out.println("Метод главного критерия");
        System.out.println("Главный критерий - С" + (mainIndex + 1));
        Map<Integer, Double> optimal = new HashMap<>();
        List<Double[]> criteria = new ArrayList<>();
        for (int i = 0; i < table[0].length; i++) {
            if (i != mainIndex) {
                criteria.add(new Double[table.length]);
                for (int j = 0; j < table.length; j++) {
                    criteria.get(i)[j] = table[j][i];
                }
                optimal.put(i, table[findOptimalAlternative(criteria.get(i))][i]);
            }
        }
        boolean found = false;
        List<Integer> optimalAlternative;
        do {
            System.out.println("------------------");
            for (Integer integer : optimal.keySet()) {
                double value = optimal.get(integer) * persent;
                System.out.printf("C%d < %4.2f\n", integer + 1, value);
                optimal.put(integer, value);
            }

            List<List<Integer>> suitableAlternatives = new ArrayList<>();
            for (int i = 0; i < criteria.size(); i++) {
                suitableAlternatives.add(findSuitableAlternative(criteria.get(i), optimal.get(i)));
            }
            optimalAlternative = findCommonAlternatives(suitableAlternatives);
            if (!optimalAlternative.isEmpty()) {
                found = true;
            }
        } while (!found);
        double min = 1e9;
        int index = -1;
        for (int i = 0; i < optimalAlternative.size(); i++) {
            if (table[optimalAlternative.get(i)][mainIndex] < min) {
                min = table[optimalAlternative.get(i)][mainIndex];
                index = i;
            }
        }

        System.out.printf("Оптимальное решение - %d\n", optimalAlternative.get(index) + 1);
    }

    private List<Integer> findSuitableAlternative(Double[] column, Double lessThan) {
        List<Integer> suitable = new ArrayList<>();
        for (int i = 0; i < column.length; i++) {
            if (column[i] < lessThan) {
                System.out.print("A" + (i + 1) + "; ");
                suitable.add(i);
            }
        }
        System.out.println();
        return suitable;
    }

    private List<Integer> findCommonAlternatives(List<List<Integer>> alternatives) {
        List<Integer> common = new ArrayList<>();
        int comparable;
        int k;
        for (int i = 0; i < alternatives.get(0).size(); i++) {
            comparable = alternatives.get(0).get(i);
            for (int j = 1; j < alternatives.size(); j++) {
                for (k = 0; k < alternatives.get(j).size(); k++) {
                    if (alternatives.get(j).get(k) == comparable) {
                        break;
                    }
                }
                if (k == alternatives.get(j).size()) {
                    break;
                }
                if (j == alternatives.size() - 1) {
                    common.add(comparable);
                }
            }
        }
        return common;
    }

    private int findOptimalAlternativeByDop(Double[][] newTable) {
        double max = -1e9;
        int index = -1;
        for (int i = 0; i < newTable.length; i++) {
            for (int j = 0; j < newTable[i].length; j++) {
                if (j == newTable[i].length - 1) {
                    if (max < newTable[i][j]) {
                        index = i;
                        max = newTable[i][j];
                    }
                }
            }
        }
        return index;
    }

    private int findOptimalAlternative(Double[] crit) {
        double min = 1e9;
        int index = -1;
        for (int i = 0; i < crit.length; i++) {
            if (min > crit[i]) {
                index = i;
                min = crit[i];
            }
        }
        return index;
    }

    private void print(Double[][] table) {
        for (int i = 0; i < table.length; i++) {
            System.out.print((i + 1) + " |");
            for (int j = 0; j < table[i].length; j++) {
                if (j == table[i].length - 1) {
                    System.out.print(" | ");
                }
                System.out.printf("%4.2f  ", table[i][j]);
            }
            System.out.println();
        }
    }
}
