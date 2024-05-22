package com.example.trabalhofacul.model;

public class Card {
    private String title;
    private int id;
    private String content;
    private int forgettingLevel;

    public Card(int id, String title, String content, int forgettingLevel) {
        this.title = title;
        this.id = id;
        this.content = content;
        this.forgettingLevel = forgettingLevel;
    }
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getForgettingLevel() {
        return forgettingLevel;
    }
}

