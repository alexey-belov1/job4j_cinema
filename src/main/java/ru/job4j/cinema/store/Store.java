package ru.job4j.cinema.store;

import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Hall;

import java.util.Collection;

public interface Store {
    Collection<Account> findAllAccounts();

    Collection<Hall> findAllHalls();

    void save(Hall hall);

    void save(Account account, Hall hall);

    Account findAccountByPhone(int phone);

    Hall findHallById(int id);

    void deleteAccount(int phone);

    void deleteHall(int id);
}