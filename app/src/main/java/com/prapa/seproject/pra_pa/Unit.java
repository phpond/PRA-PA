package com.prapa.seproject.pra_pa.Unit;

public class Unit {
    private  String month, year, date;
    private float perunit;

    public Unit(String month, String year, float perunit) {
        this.month = month;
        this.year = year;
        this.perunit = perunit;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getPerunit() {
        return perunit;
    }

    public void setPerunit(float perunit) {
        this.perunit = perunit;
    }

    public Unit() {
    }
}
