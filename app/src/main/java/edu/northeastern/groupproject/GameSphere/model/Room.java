package edu.northeastern.groupproject.GameSphere.model;


import java.util.ArrayList;

public class Room {
    private String roomId;
    private String image;
    private String roomDescription;
    private String roomName;
    private ArrayList<String> members;
    private Integer time;
    private String admin;

    public Room(String roomId, String image, String roomDescription, String roomName, ArrayList<String> members, Integer time, String admin) {
        this.roomId = roomId;
        this.image = image;
        this.roomDescription = roomDescription;
        this.roomName = roomName;
        this.members = members;
        this.time = time;
        this.admin = admin;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}