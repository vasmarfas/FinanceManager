package org.example.db.models;

public class NewOperation {
    public User user;
    public Category category;
    public float amount;


    public NewOperation(User user,
                        Category category,
                        float amount
    ) {
        this.user = user;
        this.category = category;
        this.amount = amount;
    }
}
