package sort;

import collection.MyArrayList;

import java.util.Comparator;

public interface SortAlgorithm<T> {

    void sort(MyArrayList<T> list, Comparator<? super T> comparator);

    String getName();
}
