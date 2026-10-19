package validation;

import exception.ValidationException;

import java.time.Year;

public class CarValidator {

    private static final double MIN_POWER = 10;
    private static final double MAX_POWER = 1000;
    private static final int MIN_YEAR = 1900;

    public void validate(String model, double power, int year) throws ValidationException {
        validateModel(model);
        validatePower(power);
        validateYear(year);
    }

    public void validateModel(String model) throws ValidationException {
        if (model == null || model.isBlank()) {
            throw new ValidationException("Модель не может быть пустой.");
        }
    }

    public void validatePower(double power) throws ValidationException {
        if (power < MIN_POWER || power > MAX_POWER) {
            throw new ValidationException(
                    "Мощность должна быть от " + MIN_POWER + " до " + MAX_POWER + ".");
        }
    }

    public void validateYear(int year) throws ValidationException {
        int currentYear = Year.now().getValue();
        if (year < MIN_YEAR || year > currentYear) {
            throw new ValidationException(
                    "Год выпуска должен быть от " + MIN_YEAR + " до " + currentYear + ".");
        }
    }
}