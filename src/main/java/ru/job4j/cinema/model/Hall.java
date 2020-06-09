package ru.job4j.cinema.model;

public class Hall {
    private int id;
    private int row;
    private int seat;
    private int price;
    private int accountPhone;

    public Hall(int id, int row, int seat, int price, int accountPhone) {
        this.id = id;
        this.row = row;
        this.seat = seat;
        this.price = price;
        this.accountPhone = accountPhone;
    }

    public int getAccountPhone() {
        return accountPhone;
    }

    public void setAccountPhone(int account_phone) {
        this.accountPhone = account_phone;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public int getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
