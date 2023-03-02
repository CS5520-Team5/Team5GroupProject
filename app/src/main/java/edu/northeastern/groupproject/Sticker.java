package edu.northeastern.groupproject;

public class Sticker {


    private Long time;
    private String sender;
    private String recipient;
    private String imageId;

    public Sticker() {
    }

    public String getImageId() {
        return this.imageId;
    }

    public Long getTime() {
        return this.time;
    }

    public String getSender() {
        return this.sender;
    }

    public String getRecipient() {
        return this.recipient;
    }
}
