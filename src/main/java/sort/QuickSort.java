package sort;

import collection.MyArrayList;

import java.util.Comparator;

public class QuickSort<T> implements SortAlgorithm<T> {

    @Override
    public void sort(MyArrayList<T> list, Comparator<? super T> comparator) {
        quickSort(list, comparator, 0, list.size() - 1);
    }

    @Override
    public String getName() {
        return "Быстрая сортировка";
    }

    private void quickSort(MyArrayList<T> list, Comparator<? super T> comparator,
                           int low, int high) {
        if (low >= high) {
            return;
        }
        int pivotIndex = partition(list, comparator, low, high);
        quickSort(list, comparator, low, pivotIndex - 1);
        quickSort(list, comparator, pivotIndex + 1, high);
    }

    private int partition(MyArrayList<T> list, Comparator<? super T> comparator,
                          int low, int high) {
        list.swap(low + (high - low) / 2, high);
        T pivot = list.get(high);
        int boundary = low;
        for (int i = low; i < high; i++) {
            if (comparator.compare(list.get(i), pivot) < 0) {
                list.swap(boundary, i);
                boundary++;
            }
        }
        list.swap(boundary, high);
        return boundary;
    }
}