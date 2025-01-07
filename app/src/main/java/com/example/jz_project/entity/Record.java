package com.example.jz_project.entity;
public class Record {
    public Integer id;
    public double money;
    public String type;
    public String time;
    public String note;

    public Record() {
    }

    public Record(Integer id, double money, String type, String time, String note) {
        this.id = id;
        this.money = money;
        this.note = note;
        this.time = time;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type + " " + money + " " + note;
    }
}
