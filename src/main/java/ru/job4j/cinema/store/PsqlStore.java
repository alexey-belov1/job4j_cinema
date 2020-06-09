package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Hall;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {

    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(new FileReader("db.properties"))) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Account> findAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM account")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    accounts.add(new Account(it.getInt("phone"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public Collection<Hall> findAllHalls() {
        List<Hall> halls = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM hall")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    halls.add(new Hall(it.getInt("id"), it.getInt("row"), it.getInt("seat"), it.getInt("price"), it.getInt("account_phone")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return halls;
    }

    @Override
    public void save(Hall hall) {
        try (Connection cn = pool.getConnection();
             PreparedStatement st = cn.prepareStatement("insert into hall(row, seat, price) values(?, ?, ?)")) {
            st.setInt(1, hall.getRow());
            st.setInt(2, hall.getSeat());
            st.setInt(3, hall.getPrice());
            st.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Account account, Hall hall) {
        try (Connection cn = pool.getConnection()) {
            cn.setAutoCommit(false);
            try {
                if (findAccountByPhone(account.getPhone()) == null) {
                    createAccount(cn, account);
                }
                updateHall(cn, hall);
                cn.commit();
            } catch (SQLException e) {
                cn.rollback();
            } finally {
                cn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAccount(Connection cn, Account account) throws SQLException {
        try (PreparedStatement ps =  cn.prepareStatement("insert into account(phone, name) values(?, ?);", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, account.getPhone());
            ps.setString(2, account.getName());
            ps.execute();
        }
    }

    private void updateHall(Connection cn, Hall hall) throws SQLException {
        try (PreparedStatement ps =  cn.prepareStatement("UPDATE hall SET row = (?), seat = (?), price = (?), account_phone = (?)  WHERE id = (?);")) {
            ps.setInt(1, hall.getRow());
            ps.setInt(2, hall.getSeat());
            ps.setInt(3, hall.getPrice());
            ps.setInt(4, hall.getAccountPhone());
            ps.setInt(5, hall.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public Account findAccountByPhone(int phone) {
        Account account = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM account WHERE phone = (?)")) {
            ps.setInt(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    account = new Account(phone, rs.getString("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return account;
    }

    @Override
    public Hall findHallById(int id) {
        Hall hall = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM hall WHERE id = (?)")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    hall = new Hall(id, rs.getInt("row"), rs.getInt("seat"), rs.getInt("price"), rs.getInt("account_phone"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hall;
    }

    @Override
    public void deleteAccount(int phone) {
        try (Connection cn = pool.getConnection();
             PreparedStatement st = cn.prepareStatement("DELETE FROM account WHERE phone = (?)")) {
            st.setInt(1, phone);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteHall(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement st = cn.prepareStatement("DELETE FROM hall WHERE id = (?)")) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}