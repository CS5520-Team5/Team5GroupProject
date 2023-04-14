package edu.northeastern.groupproject.GameSphere.model;


public class Message {
    private String messageId;
    private String content;
    private String roomId;
    private String sender;
    private String avatar;
    private Long time;

    public Message(String messageId, String content, String roomId, String sender, String avatar, Long time) {
        this.messageId = messageId;
        this.content = content;
        this.roomId = roomId;
        this.sender = sender;
        this.avatar = avatar;
        this.time = time;
    }

    public Message(String messageId, String content, String roomId, String sender, Long time) {
        this.messageId = messageId;
        this.content = content;
        this.roomId = roomId;
        this.sender = sender;
        this.time = time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", content='" + content + '\'' +
                ", roomId='" + roomId + '\'' +
                ", sender='" + sender + '\'' +
                ", time=" + time +
                '}';
    }
}