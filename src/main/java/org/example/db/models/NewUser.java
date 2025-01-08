package org.example.db.models;

import java.util.ArrayList;

public class NewUser {
    public String login;
    public String password;
    public ArrayList<Category> categories;


    public NewUser(String login,
                   String password
    ) {
        this.login = login;
        this.password = password;
        this.categories = new ArrayList<>();
    }
}
