package service;

import model.Car;

import java.util.Comparator;
import java.util.List;

public class CarService {

    public List<Car> filterByMinPower(List<Car> cars, double minPower) {
        return cars.stream()
                .filter(car -> car.getPower() >= minPower)
                .toList();
    }

    public List<String> getDescriptions(List<Car> cars) {
        return cars.stream()
                .map(car -> car.getModel() + " (" + car.getYearOfManufacture() + ")")
                .toList();
    }

    public List<Car> sortBy(List<Car> cars, Comparator<Car> comparator) {
        return cars.stream()
                .sorted(comparator)
                .toList();
    }
}