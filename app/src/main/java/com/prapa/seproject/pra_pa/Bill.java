package com.prapa.seproject.pra_pa;

public class Bill {
    private Room room;

    private String month;
    private String year;
    private int water_bill;
    private int history_water_bill = 0;
    private String record_date;

    private int total_price_bill;
    private int price_meter = 18;

    private String status = "ยังไม่ชำระเงิน";

    public Bill(){}
    public Bill(Room room, int water_bill, String month, String year, String record_date, int history_water_bill) {
        this.room = room;
        this.month = month;
        this.year = year;
        this.water_bill = water_bill;
        this.record_date = record_date;
        this.history_water_bill = history_water_bill;
        calculateBill();
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

    public int getPrice_meter() {
        return price_meter;
    }

    public void setPrice_meter(int price_meter) {
        this.price_meter = price_meter;
    }

    public int getHistory_water_bill() {
        return history_water_bill;
    }

    public void setHistory_water_bill(int history_water_bill) {
        this.history_water_bill = history_water_bill;
    }

    public int getTotal_price_bill() {
        return total_price_bill;
    }

    public void setTotal_price_bill(int total_peice_bill) {
        this.total_price_bill = total_peice_bill;
    }
    private void calculateBill(){
        total_price_bill = (water_bill - history_water_bill)* price_meter;
    }

    public Room getRoom() {
        return room;
    }

    public String getRoomString(){return room.getRoom();}

    public void setRoom(Room room) {
        this.room = room;
    }



}