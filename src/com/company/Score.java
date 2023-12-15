package com.company;

import java.io.Serializable;

public class Score implements Serializable {
    private final String name;
    private final int score;

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }

    @Override
    public String toString() {
        return this.name + ": " + this.score;
    }
}
