package com.prapa.seproject.pra_pa;

public class Room {
    private char phase;
    private int floor;
    private int number_room;

    public Room() { }

    public Room(char phase, int floor, int number_room) {
        this.phase = phase;
        this.floor = floor;
        this.number_room = number_room;
    }

    public char getPhase() {
        return phase;
    }

    public void setPhase(char phase) {
        this.phase = phase;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getNumber_room() {
        return number_room;
    }

    public void setNumber_room(int number_room) {
        this.number_room = number_room;
    }
}