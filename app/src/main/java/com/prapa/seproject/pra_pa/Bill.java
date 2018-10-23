package com.prapa.seproject.pra_pa;

public class Bill {
    private Room room;

    private String month;
    private int water_bill;
    private String record_date;

    private String status = "ยังไม่ชำระเงิน";

    public Bill(Room room, int water_bill,String month,  String record_date) {
        this.room = room;
        this.month = month;
        this.water_bill = water_bill;
        this.record_date = record_date;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getWater_bill() {
        return water_bill;
    }

    public void setWater_bill(int water_bill) {
        this.water_bill = water_bill;
    }

    public String getRecord_date() {
        return record_date;
    }

    public void setRecord_date(String record_date) {
        this.record_date = record_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}