package collection;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MyArrayList<T> implements Iterable<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] elements;
    private int size;

    public MyArrayList() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    public void add(T element) {
        ensureCapacity();
        elements[size++] = element;
    }

    public void addAll(MyArrayList<? extends T> other) {
        for (T element : other) {
            add(element);
        }
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) elements[index];
    }

    public T remove(int index) {
        checkIndex(index);
        T removed = get(index);
        int movedCount = size - index - 1;
        if (movedCount > 0) {
            System.arraycopy(elements, index + 1, elements, index, movedCount);
        }
        elements[--size] = null;
        return removed;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int current = 0;

            @Override
            public boolean hasNext() {
                return current < size;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return get(current++);
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elements[i]);
        }
        return sb.append("]").toString();
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, elements.length * 2);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Индекс " + index + " вне диапазона, размер коллекции: " + size);
        }
    }

    public void swap(int i, int j) {
        checkIndex(i);
        checkIndex(j);
        Object temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
    }
}