package ru.job4j.cinema.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class PaymentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        int id = Integer.valueOf(req.getParameter("id"));
        Hall hall = PsqlStore.instOf().findHallById(id);
        String json = new Gson().toJson(hall);
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        writer.println(json);
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("windows-1251");
        JsonObject json = new JsonObject();

        int id = Integer.valueOf(req.getParameter("id"));
        String username = req.getParameter("username");
        int phone = Integer.valueOf(req.getParameter("phone"));

        Account account = PsqlStore.instOf().findAccountByPhone(phone);
        if (account != null && !username.equals(account.getName())) {
            json.addProperty("result", false);
            json.addProperty("msg", "Данный номер занят другим аккаунтом");
        } else {
            Hall hall = PsqlStore.instOf().findHallById(id);
            hall.setAccountPhone(phone);
            PsqlStore.instOf().save(new Account(phone, username), hall);
            json.addProperty("result", true);
            json.addProperty("msg", "Бронирование прошло успешно");
        }
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        writer.println(json);
        writer.flush();

    }
}