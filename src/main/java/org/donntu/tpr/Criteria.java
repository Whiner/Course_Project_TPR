package org.donntu.tpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Criteria {
    private Double[][] table;
    private List<Double> minList;
    private List<Double> maxList;

    public Criteria(Double[][] table) {
        this.table = table;
        minList = new ArrayList<>();
        maxList = new ArrayList<>();
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
    }

    public void addit(double... koef) {
        System.out.println("Метод аддитивной свертки");

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

        int optimalAlternative = findOptimalAlternativeByDop(newTable, true);
        print(newTable);
        System.out.printf("Оптимальное решение %4.2f (A%d)\n", newTable[optimalAlternative][newTable[0].length - 1], optimalAlternative + 1);
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
        int optimalAlternative = findOptimalAlternativeByDop(newTable, true);
        print(newTable);
        System.out.printf("Оптимальное решение %4.2f (A%d)\n", newTable[optimalAlternative][newTable[0].length - 1], optimalAlternative + 1);
    }

    public void mainCrit(int mainIndex, double percent) {
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
                optimal.put(i, table[minIndex(criteria.get(i))][i]);
            }
        }
        boolean found = false;
        List<Integer> optimalAlternative;
        do {
            System.out.println("------------------");
            for (Integer integer : optimal.keySet()) {
                double value = optimal.get(integer) * percent;
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

    public void targetProgramming() {
        System.out.println("Метод целевого программирования");

        Double[][] newTable = new Double[table.length][table[0].length + 1];
        int j;
        for (int i = 0; i < table.length; i++) {
            double ac = 0;
            for (j = 0; j < table[i].length; j++) {
                newTable[i][j] = Math.pow(((maxList.get(j) - table[i][j]) / (maxList.get(j) - minList.get(j)) - 1), 2);
                ac += newTable[i][j];
            }
            newTable[i][j] = ac;
        }

        print(newTable);
        int optimalAlternative = findOptimalAlternativeByDop(newTable, false);
        System.out.printf("Оптимальное решение %4.2f (A%d)\n", newTable[optimalAlternative][newTable[0].length - 1], optimalAlternative + 1);
    }

    public void ustupok(int... priority) {
        System.out.println("Метод последовательных уступок");

        List<List<Integer>> alternatives = new ArrayList<>();

        for (int i = 1; i < priority.length; i++) {
            Double[] crit = getCrit(priority[i - 1]);
            int minIndex = minIndex(crit);
            Double min = table[minIndex][priority[i - 1]];
            System.out.printf("C%d -> min = %4.2f (A%d)\n", priority[i - 1] + 1, min, minIndex + 1);
            double ustupka = Math.round(min / 2) + 1;
            System.out.printf("z%d = %4.2f\n", i, ustupka);
            double ogr = min + ustupka;
            System.out.printf("C%d <= %4.2f + %4.2f = %4.2f\n",
                    priority[i - 1] + 1,
                    min,
                    ustupka,
                    ogr);

            System.out.print("Альтернативы, удовлетворяющие данному ограничению: ");
            alternatives.add(findSuitableAlternative(getCrit(priority[i - 1]), ogr));
            System.out.print("Альтернативы, удовлетворяющие всем ограничениям: ");
            List<Integer> commonAlternatives = findCommonAlternatives(alternatives);
            for (Integer commonAlternative : commonAlternatives) {
                System.out.print("A" + (commonAlternative + 1) + "; ");
            }
            System.out.println();
            System.out.println("___________________");
        }

    }

    public void garantResult() {
        System.out.println("Метод гарантированного результата");

        Double[][] newTable = new Double[table.length][table[0].length + 1];
        int j;
        for (int i = 0; i < table.length; i++) {
            double min = 1e9;
            for (j = 0; j < table[i].length; j++) {
                newTable[i][j] = (maxList.get(j) - table[i][j]) / (maxList.get(j) - minList.get(j));
                if (newTable[i][j] < min) {
                    min = newTable[i][j];
                }
            }
            newTable[i][j] = min;

        }


        print(newTable);
        int optimalAlternative = findOptimalAlternativeByDop(newTable, true);
        System.out.printf("Оптимальное решение %4.2f (A%d)\n", newTable[optimalAlternative][newTable[0].length - 1], optimalAlternative + 1);
    }

    private Double[] getCrit(int index) {
        Double[] crit = new Double[table.length];
        for (int i = 0; i < table.length; i++) {
            crit[i] = table[i][index];
        }
        return crit;
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

    private int findOptimalAlternativeByDop(Double[][] newTable, boolean bigger) {
        double v;
        if (bigger) {
            v = -1e9;
        } else {
            v = 1e9;
        }
        int index = -1;
        for (int i = 0; i < newTable.length; i++) {
            for (int j = 0; j < newTable[i].length; j++) {
                if (j == newTable[i].length - 1) {
                    if (bigger && v < newTable[i][j]) {
                        index = i;
                        v = newTable[i][j];
                    }

                    if (!bigger && v > newTable[i][j]) {
                        index = i;
                        v = newTable[i][j];
                    }
                }
            }
        }
        return index;
    }

    private int minIndex(Double[] crit) {
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
