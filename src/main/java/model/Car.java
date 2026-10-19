package model;

public class Car {

    private final String model;
    private final double power;
    private final int yearOfManufacture;

    public Car(String model, double power, int yearOfManufacture) {
        this.model = model;
        this.power = power;
        this.yearOfManufacture = yearOfManufacture;
    }

    public String getModel() {
        return model;
    }

    public double getPower() {
        return power;
    }

    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

    @Override
    public String toString() {
        return String.format("Car{model='%s', power=%.1f, year=%d}",
                model, power, yearOfManufacture);
    }
}