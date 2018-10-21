package com.prapa.seproject.pra_pa;

import java.sql.Timestamp;
import java.util.Date;

public class Bill {
    private Room room;

    private Date month;
    private int water_bill;
    private Timestamp record_date;

    private String status = "ยังไม่ชำระเงิน";

    public Bill(Room room, Date month, int water_bill, Timestamp record_date) {
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

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public int getWater_bill() {
        return water_bill;
    }

    public void setWater_bill(int water_bill) {
        this.water_bill = water_bill;
    }

    public Date getRecord_date() {
        return record_date;
    }

    public void setRecord_date(Timestamp record_date) {
        this.record_date = record_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
