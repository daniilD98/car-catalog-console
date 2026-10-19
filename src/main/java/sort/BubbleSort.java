package sort;

import collection.MyArrayList;

import java.util.Comparator;

public class BubbleSort<T> implements SortAlgorithm<T> {

    @Override
    public void sort(MyArrayList<T> list, Comparator<? super T> comparator) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                    list.swap(j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
    }

    @Override
    public String getName() {
        return "Пузырьковая сортировка";
    }
}