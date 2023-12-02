package com.example.lab4;

import java.util.ArrayList;
import java.util.List;

public enum Level {

    NON_SECRET(1),
    SECRET(2),
    TOP_SECRET(3);

    private int value;

    Level(int value) {
        this.value = value;
    }

    public static List<String> getAll() {
        List<String> result = new ArrayList<>();

        for (Level level : values())
            result.add(level.name());
        return result;
    }

    private int getValue() {
        return this.value;
    }

    public Boolean checkLevel(Level that) {
        return this.value > that.getValue();
    }
}
