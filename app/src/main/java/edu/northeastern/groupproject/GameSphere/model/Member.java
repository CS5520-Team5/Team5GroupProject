package edu.northeastern.groupproject.GameSphere.model;

public class Member {
    private String key;
    private String username;
    private String image;
    private Integer count=0;

    public Member(String key, String username, String image) {
        this.key = key;
        this.username = username;
        this.image = image;
    }


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    public void addCount(){
        this.count=this.count+1;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Member{" +
                "key='" + key + '\'' +
                ", username='" + username + '\'' +
                ", image='" + image + '\'' +
                ", count=" + count +
                '}';
    }
}
