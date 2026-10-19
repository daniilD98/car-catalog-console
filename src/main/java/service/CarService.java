package service;

import collection.MyArrayList;
import model.Car;

import java.util.Comparator;
import java.util.stream.Stream;

public class CarService {

    public MyArrayList<Car> filterByMinPower(MyArrayList<Car> cars, double minPower) {
        return toMyList(cars.stream()
                .filter(car -> car.getPower() >= minPower));
    }

    public MyArrayList<String> getDescriptions(MyArrayList<Car> cars) {
        return toMyList(cars.stream()
                .map(car -> car.getModel() + " (" + car.getYearOfManufacture() + ")"));
    }

    public MyArrayList<Car> sortBy(MyArrayList<Car> cars, Comparator<Car> comparator) {
        return toMyList(cars.stream()
                .sorted(comparator));
    }

    private <T> MyArrayList<T> toMyList(Stream<T> stream) {
        return stream.collect(MyArrayList::new, MyArrayList::add, MyArrayList::addAll);
    }
}