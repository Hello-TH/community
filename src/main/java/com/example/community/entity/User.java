package com.example.community.entity;

/**
 * ユーザーに関する処理を保存するクラス
 * 
 * @author user
 *
 */
public class User {
    
    private String id;
    private String userName;
    private String userComment;
    private String imgName;
    
    public String getId () {
        return id;
    }
    
    public void setId (String id) {
        this.id = id;
    }
    
    public String getUserName () {
        return userName;
    }
    
    public void setUserName (String userName) {
        this.userName = userName;
    }
    
    public String getUserComment () {
        return userComment;
    }
    
    public void setUserComment (String userComment) {
        this.userComment = userComment;
    }
    
    public String getImgName () {
        return imgName;
    }
    
    public void setImgName (String imgName) {
        this.imgName = imgName;
    }
    
    
    
}
