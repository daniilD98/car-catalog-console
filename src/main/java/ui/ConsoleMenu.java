package ui;

import collection.MyArrayList;
import exception.ValidationException;
import io.CarFileReader;
import model.Car;
import service.CarService;
import validation.CarValidator;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.Comparator;
import java.util.Scanner;
import sort.BubbleSort;
import sort.QuickSort;
import sort.SortAlgorithm;

public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final MyArrayList<Car> cars = new MyArrayList<>();
    private final CarValidator validator = new CarValidator();
    private final CarFileReader fileReader = new CarFileReader(validator);
    private final CarService service = new CarService();
    private final MyArrayList<SortAlgorithm<Car>> algorithms = createAlgorithms();

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
                case 5 -> showCarByIndex();
                case 6 -> removeCarByIndex();
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
                5. Показать автомобиль по номеру
                6. Удалить автомобиль по номеру
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
            MyArrayList<Car> loaded = fileReader.read(path);
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
            case 3 -> sortCars(Comparator.comparingDouble(Car::getPower));
            case 4 -> sortCars(Comparator.comparingInt(Car::getYearOfManufacture));
            case 5 -> sortCars(Comparator.comparing(Car::getModel, String.CASE_INSENSITIVE_ORDER));
            case 0 -> { }
            default -> System.out.println("Неизвестная команда.");
        }
    }

    private void printAll() {
        if (cars.isEmpty()) {
            System.out.println("Список пуст.");
            return;
        }
        for (int i = 0; i < cars.size(); i++) {
            System.out.println((i + 1) + ". " + cars.get(i));
        }
    }

    private void showCarByIndex() {
        if (cars.isEmpty()) {
            System.out.println("Список пуст.");
            return;
        }
        int number = readInt("Номер автомобиля (1-" + cars.size() + "): ");
        try {
            System.out.println(cars.get(number - 1));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void removeCarByIndex() {
        if (cars.isEmpty()) {
            System.out.println("Список пуст.");
            return;
        }
        int number = readInt("Номер автомобиля для удаления (1-" + cars.size() + "): ");
        try {
            Car removed = cars.remove(number - 1);
            System.out.println("Удалён: " + removed);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void printCars(MyArrayList<Car> list) {
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
    private static MyArrayList<SortAlgorithm<Car>> createAlgorithms() {
        MyArrayList<SortAlgorithm<Car>> list = new MyArrayList<>();
        list.add(new BubbleSort<>());
        list.add(new QuickSort<>());
        return list;
    }

    private void sortCars(Comparator<Car> comparator) {
        SortAlgorithm<Car> algorithm = chooseAlgorithm();
        boolean ascending = chooseDirection();
        System.out.println("Алгоритм: " + algorithm.getName());
        printCars(service.sortBy(cars, comparator, algorithm, ascending));
    }

    private SortAlgorithm<Car> chooseAlgorithm() {
        System.out.println("Выберите алгоритм сортировки:");
        for (int i = 0; i < algorithms.size(); i++) {
            System.out.println((i + 1) + ". " + algorithms.get(i).getName());
        }
        while (true) {
            int number = readInt("Номер алгоритма: ");
            if (number >= 1 && number <= algorithms.size()) {
                return algorithms.get(number - 1);
            }
            System.out.println("Ошибка: введите число от 1 до " + algorithms.size() + ".");
        }
    }

    private boolean chooseDirection() {
        System.out.println("Направление сортировки:");
        System.out.println("1. По возрастанию");
        System.out.println("2. По убыванию");
        while (true) {
            int number = readInt("Ваш выбор: ");
            if (number == 1) {
                return true;
            }
            if (number == 2) {
                return false;
            }
            System.out.println("Ошибка: введите 1 или 2.");
        }
    }
}