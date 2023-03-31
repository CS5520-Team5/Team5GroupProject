package edu.northeastern.groupproject.GameSphere;

import java.util.List;

public class News {

    private Long newsId, numberOfLikes;
    private String title, newsDate, content;
    private List<Comment> commentList;

    public News() {}

    public News(Long newsId, String title, String content, String newsDate, Long numberOfLikes, List<Comment> commentList) {
        this.newsId = newsId;
        this.title = title;
        this.content = content;
        this.newsDate = newsDate;
        this.numberOfLikes = numberOfLikes;
        this.commentList = commentList;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(Long numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public String toString() {
        return "News{" +
                "newsId=" + newsId +
                ", numberOfLikes=" + numberOfLikes +
                ", title='" + title + '\'' +
                ", newsDate='" + newsDate + '\'' +
                ", content='" + content + '\'' +
                ", commentList=" + commentList +
                '}';
    }
}
