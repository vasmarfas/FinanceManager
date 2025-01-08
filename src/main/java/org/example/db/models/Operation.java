package org.example.db.models;

public class Operation {
    public int id;
    public User user;
    public Category category;
    public float amount;


    public Operation(int id,
                     User user,
                     Category category,
                     float amount
    ) {
        this.id = id;
        this.user = user;
        this.category = category;
        this.amount = amount;
    }
}
