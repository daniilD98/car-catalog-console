package ui;

import exception.ValidationException;
import io.CarFileReader;
import model.Car;
import service.CarService;
import validation.CarValidator;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final List<Car> cars = new ArrayList<>();
    private final CarValidator validator = new CarValidator();
    private final CarFileReader fileReader = new CarFileReader(validator);
    private final CarService service = new CarService();

    public void start() {
        while (true) {
            printMenu();
            int command;
            try {
                command = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: необходимо ввести число.");
                continue;
            }

            switch (command) {
                case 1 -> addCarManually();
                case 2 -> loadFromFile();
                case 3 -> processData();
                case 4 -> printAll();
                case 0 -> {
                    System.out.println("Завершение работы программы...");
                    return;
                }
                default -> System.out.println("Неизвестная команда.");
            }
        }
    }

    private void printMenu() {
        System.out.println("""
                МЕНЮ
                1. Добавить автомобиль вручную
                2. Загрузить данные из файла
                3. Обработать данные
                4. Показать все автомобили
                0. Выход
                """);
        System.out.print("Выберите пункт меню: ");
    }

    private void addCarManually() {
        while (true) {
            try {
                System.out.print("Модель: ");
                String model = scanner.nextLine();
                double power = readDouble("Мощность (л.с.): ");
                int year = readInt("Год выпуска: ");

                validator.validate(model, power, year);
                cars.add(new Car(model.trim(), power, year));
                System.out.println("Автомобиль добавлен.");
                return;
            } catch (ValidationException e) {
                System.out.println("Ошибка: " + e.getMessage() + " Попробуйте ещё раз.");
            }
        }
    }

    private void loadFromFile() {
        System.out.print("Путь к файлу: ");
        String path = scanner.nextLine().trim();
        try {
            List<Car> loaded = fileReader.read(path);
            cars.addAll(loaded);
            System.out.println("Загружено автомобилей: " + loaded.size());
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        } catch (InvalidPathException e) {
            System.out.println("Некорректный путь к файлу.");
        }
    }

    private void processData() {
        if (cars.isEmpty()) {
            System.out.println("Список пуст. Сначала добавьте данные.");
            return;
        }

        System.out.println("""
                Обработка данных
                1. Фильтр: мощность не меньше заданной
                2. Список "модель (год)" (map)
                3. Сортировка по мощности
                4. Сортировка по году выпуска
                5. Сортировка по модели
                0. Назад
                """);
        System.out.print("Выберите действие: ");

        int command;
        try {
            command = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: необходимо ввести число.");
            return;
        }

        switch (command) {
            case 1 -> {
                double minPower = readDouble("Минимальная мощность: ");
                printCars(service.filterByMinPower(cars, minPower));
            }
            case 2 -> service.getDescriptions(cars).forEach(System.out::println);
            case 3 -> printCars(service.sortBy(cars, Comparator.comparingDouble(Car::getPower)));
            case 4 -> printCars(service.sortBy(cars, Comparator.comparingInt(Car::getYearOfManufacture)));
            case 5 -> printCars(service.sortBy(cars, Comparator.comparing(Car::getModel, String.CASE_INSENSITIVE_ORDER)));
            case 0 -> { }
            default -> System.out.println("Неизвестная команда.");
        }
    }

    private void printAll() {
        printCars(cars);
    }

    private void printCars(List<Car> list) {
        if (list.isEmpty()) {
            System.out.println("Ничего не найдено.");
            return;
        }
        list.forEach(System.out::println);
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число.");
            }
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число.");
            }
        }
    }
}
