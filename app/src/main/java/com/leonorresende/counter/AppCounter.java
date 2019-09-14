package com.leonorresende.counter;

public class AppCounter implements java.io.Serializable {
    private String title;
    private int number;

    public AppCounter(String requiredTitle, int requiredNumber) {
        this.title = requiredTitle;
        this.number = requiredNumber;
    }

    public String getTitle() {
        return this.title;
    }

    public int getNumber() {
        return this.number;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }
    public void setNumber(int newNumber) {
        this.number = newNumber;
    }
    public void decrementNumber() { number--;}
    public void incrementNumber() { number++;}
}
