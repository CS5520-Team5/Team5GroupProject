package edu.northeastern.groupproject.GameSphere.model;


import java.util.List;

public class Message {
    private String roomId;
    private String image;
    private String roomDescription;
    private String roomName;
    private List<String> members;
    private Long time;
    private String admin;

    public Message(String roomId, String image, String roomDescription, String roomName, List<String> members, Long time, String admin) {
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

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", image='" + image + '\'' +
                ", roomDescription='" + roomDescription + '\'' +
                ", roomName='" + roomName + '\'' +
                ", members=" + members +
                ", time=" + time +
                ", admin='" + admin + '\'' +
                '}';
    }
}