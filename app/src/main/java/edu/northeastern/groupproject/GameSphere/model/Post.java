package edu.northeastern.groupproject.GameSphere.model;


import java.util.ArrayList;
import java.util.Date;

public class Post {
    private String postId;
    private String username;
    private String content;
    private int numberOfLikes;
    private Date postDate;
    private String title;
    private ArrayList<Comment> comments;


    public class Comment{
        private Date postDate;
        private String content;
        private String username;

        public Comment(Date postDate, String content, String username) {
            this.postDate = postDate;
            this.content = content;
            this.username = username;
        }

        public Date getPostDate() {
            return postDate;
        }

        public void setPostDate(Date postDate) {
            this.postDate = postDate;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public Post(String postId, String username, String content, int numberOfLikes, Date postDate, String title, ArrayList<Comment> comments) {
        this.postId = postId;
        this.username = username;
        this.content = content;
        this.numberOfLikes = numberOfLikes;
        this.postDate = postDate;
        this.title = title;
        this.comments = comments;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}