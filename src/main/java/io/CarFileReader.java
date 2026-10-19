package io;

import exception.ValidationException;
import model.Car;
import validation.CarValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CarFileReader {

    private static final String SEPARATOR = ";";

    private final CarValidator validator;

    public CarFileReader(CarValidator validator) {
        this.validator = validator;
    }

    public List<Car> read(String path) throws IOException {
        List<Car> result = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Path.of(path))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }
                try {
                    result.add(parseLine(line));
                } catch (ValidationException e) {
                    System.out.println("Строка " + lineNumber + " пропущена: " + e.getMessage());
                }
            }
        }
        return result;
    }

    private Car parseLine(String line) throws ValidationException {
        String[] parts = line.split(SEPARATOR);
        if (parts.length != 3) {
            throw new ValidationException("Ожидается формат: модель;мощность;год.");
        }

        String model = parts[0].trim();
        double power;
        int year;
        try {
            power = Double.parseDouble(parts[1].trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new ValidationException("Мощность должна быть числом.");
        }
        try {
            year = Integer.parseInt(parts[2].trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("Год должен быть целым числом.");
        }

        validator.validate(model, power, year);
        return new Car(model, power, year);
    }
}