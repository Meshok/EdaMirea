package com.app.edamirea;

public class Meal {
    private final int id;
    private String name;
    private double price;
    private MealType type;

    public Meal(String... params) {
        this.id = Integer.parseInt(params[0]);
        this.name = params[1];
        this.price = Double.parseDouble(params[2]);
        switch (params[3]) {
            case "суп":
                this.type = MealType.soup;
                break;
            case "второе":
                this.type = MealType.second_course;
                break;
            case "напиток":
                this.type = MealType.drink;
                break;
            case "салат":
                this.type = MealType.salad;
                break;
            case "гарнир":
                this.type = MealType.side_dish;
                break;
            default:
                this.name = params[3];
                this.type = MealType.soup;
                break;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public MealType getType() {
        return type;
    }

    public void setType(MealType type) {
        this.type = type;
    }
}
