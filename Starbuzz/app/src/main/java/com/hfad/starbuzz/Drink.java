package com.hfad.starbuzz;

/**
 * Created by davidg on 30/04/2017.
 */

public class Drink {
    private String name; private String description; private int imageResourceId;
    //drinks is an array of Drinks


    //Each Drink has a name, description, and an image resource
    private Drink(String name, String description, int imageResourceId) {
        this.name = name;
        this.description = description;
        this.imageResourceId = imageResourceId;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String toString() {
        return this.name;
    }
}
