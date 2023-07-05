package com.example.logsignsql;

public class Egzersiz {
    private int id;
    private int playerId;
    private String name;

    public Egzersiz(int id, int playerId, String name) {
        this.id = id;
        this.playerId = playerId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }
}