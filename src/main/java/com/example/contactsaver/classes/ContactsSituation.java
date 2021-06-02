package com.example.contactsaver.classes;

public class ContactsSituation {
    int autoid;
    int id;
    String name;
    int situation;

    public ContactsSituation(int id, String name, int situation) {
        this.id = id;
        this.name = name;
        this.situation = situation;
    }

    public ContactsSituation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAutoid() {
        return autoid;
    }

    public void setAutoid(int autoid) {
        this.autoid = autoid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSituation() {
        return situation;
    }

    public void setSituation(int situation) {
        this.situation = situation;
    }
}
