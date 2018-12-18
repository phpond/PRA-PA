package com.prapa.seproject.pra_pa;

public class User {

    private String room_id;
    private String username;
    private String password;
    private String role;

    public User(){}

    public User(String room_id, String username, String password, String role) {
        this.room_id = room_id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
