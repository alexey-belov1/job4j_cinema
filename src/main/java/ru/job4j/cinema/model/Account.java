package ru.job4j.cinema.model;

public class Account {
    private int phone;
    private String name;

    public Account(int phone, String name) {
        this.phone = phone;
        this.name = name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
