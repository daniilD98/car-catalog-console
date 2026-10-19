package service;

import collection.MyArrayList;
import model.Car;

import java.util.Comparator;
import java.util.stream.Stream;
import sort.SortAlgorithm;

public class CarService {

    public MyArrayList<Car> filterByMinPower(MyArrayList<Car> cars, double minPower) {
        return toMyList(cars.stream()
                .filter(car -> car.getPower() >= minPower));
    }

    public MyArrayList<String> getDescriptions(MyArrayList<Car> cars) {
        return toMyList(cars.stream()
                .map(car -> car.getModel() + " (" + car.getYearOfManufacture() + ")"));
    }

    public MyArrayList<Car> sortBy(MyArrayList<Car> cars,
                                   Comparator<Car> comparator,
                                   SortAlgorithm<Car> algorithm,
                                   boolean ascending) {
        MyArrayList<Car> copy = toMyList(cars.stream());
        Comparator<Car> effective = ascending ? comparator : comparator.reversed();
        algorithm.sort(copy, effective);
        return copy;
    }

    private <T> MyArrayList<T> toMyList(Stream<T> stream) {
        return stream.collect(MyArrayList::new, MyArrayList::add, MyArrayList::addAll);
    }
}