package org.example.db.models;

public class Category {
    public int id;
    public String name;
    public float quota;
    public boolean isProfit;


    public Category(int id,
                    String name,
                    float quota,
                    boolean isProfit
    ) {
        this.id = id;
        this.name = name;
        this.quota = quota;
        this.isProfit = isProfit;
    }
}
