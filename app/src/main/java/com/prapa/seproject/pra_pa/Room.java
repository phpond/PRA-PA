package com.prapa.seproject.pra_pa;

import android.os.Parcel;
import android.os.Parcelable;

public class Room implements Parcelable {
    private String phase;
    private int floor;
    private String number_room;

    public Room() { }

    public Room(String phase, int floor, String number_room) {
        this.phase = phase.toUpperCase();
        this.floor = floor;
        this.number_room = number_room;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase.toUpperCase();
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getNumber_room() {
        return number_room;
    }

    public void setNumber_room(String number_room) {
        this.number_room = number_room;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(phase);
            dest.writeInt(floor);
            dest.writeString(number_room);
        }

        public static final Parcelable.Creator<Room> CREATOR =
                new Parcelable.Creator<Room>() {
                    @Override
                    public Room createFromParcel(Parcel source) {
                        return new Room();
                    }
                    @Override
                    public Room[] newArray(int size) {
                        return new Room[size];
                    }
                };

    }

