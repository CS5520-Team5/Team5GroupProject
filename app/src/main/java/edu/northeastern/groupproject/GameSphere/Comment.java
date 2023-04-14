package edu.northeastern.groupproject.GameSphere;

import java.io.Serializable;

public class Comment implements Serializable {

    private String userName, content, commentDate;

    public Comment(String userName, String content, String commentDate) {
        this.userName = userName;
        this.content = content;
        this.commentDate = commentDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "userName='" + userName + '\'' +
                ", content='" + content + '\'' +
                ", commentDate='" + commentDate + '\'' +
                '}';
    }
}
