import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solve {
    private final Backpack backpack;

    public Solve(Backpack backpack) {
        this.backpack = backpack;
    }

    public void heurystka() {
        List<Item> result = new ArrayList<>();
        int currentCapacity = 0;
        Long startTime = System.nanoTime();
        List<Item> currentDataset = new ArrayList<>(backpack.getDataset());

        Collections.sort(currentDataset);
        Collections.reverse(currentDataset);
        while (true) {
            Item remove = currentDataset.removeFirst();

            if (currentCapacity + remove.getSize() <= backpack.getCapacity()) {
                result.add(remove);
                currentCapacity += remove.getSize();
            } else
                break;
        }
        Long endTime = System.nanoTime();
        System.out.println("HEURESTYKA:");
        System.out.println(result);
        double valve = result.stream().mapToDouble(Item::getVals).sum();
        double capacity = result.stream().mapToDouble(Item::getSize).sum();
        System.out.println("wartosc to: " + valve);
        System.out.println("pojemność to: " + capacity);
        System.out.println("Czas działania to: " + (endTime - startTime) / 1000 + " mikrosekund");
    }

    public void bruteForce() {
        long iterationCounter = 0L;
        List<Item> dataset = backpack.getDataset();
        int currentCapacity = backpack.getCapacity();
        Long startTime = System.nanoTime();
        int[]  arr = new int[dataset.size()];

        List<Item> bestSolution = new ArrayList<>();
        List<Item> tmpSolution = new ArrayList<>();

        double bestValue = 0;

        boolean continueLoop;

        while (true) {
            iterationCounter++;
            tmpSolution.clear();
            int i = arr.length - 1;

            for (int tmpi = 0; tmpi < arr.length; tmpi++) {
                if (arr[tmpi] == 1) {
                    tmpSolution.add(dataset.get(tmpi));
                }
            }

            double tempWeight = 0;
            double tempValue = 0;

            for (Item item : tmpSolution) {
                tempWeight += item.getSize();
                tempValue += item.getVals();
            }

            if (tempWeight <= currentCapacity && bestValue < tempValue) {
                bestValue = tempValue;
                bestSolution = new ArrayList<>(tmpSolution);

            }

            continueLoop = checkifDone(arr);
            if (!continueLoop)
                break;

            addToarr(arr, i);
        }
        Long endTime = System.nanoTime();
        System.out.println("BRUTE FORCE:");
        System.out.println(bestSolution);
        double valve = bestSolution.stream().mapToDouble(Item::getVals).sum();
        double capacity = bestSolution.stream().mapToDouble(Item::getSize).sum();
        System.out.println("wartosc to: " + valve);
        System.out.println("pojemność to: " + capacity);
        System.out.println("Liczba spawdznych zestawów: " + iterationCounter);
        System.out.println("Czas działania to: " + (endTime - startTime) / 1000000000 + "s");
    }

    public static boolean checkifDone(int[] n) {
        for (int i : n) {
            if (i == 0) {
                return true;
            }
        }
        return false;
    }

    public static void addToarr(int[] a, int i) {
        if (i < 0) return;
        if (a[i] == 0) {
            a[i] = 1;
            return;
        }
        a[i] = 0;
        addToarr(a, i - 1);
    }
}
