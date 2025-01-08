package org.example.db.models;

import java.util.ArrayList;
import java.util.Objects;

public class User {
    public int id;
    public String login;
    String password;
    public ArrayList<Category> categories;


    public User(int id,
                String login,
                String password,
                ArrayList<Category> categories
    ) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.categories = categories;
    }
    public boolean checkByPassword(String curPassword) {
        return Objects.equals(password, curPassword);
    }
}
