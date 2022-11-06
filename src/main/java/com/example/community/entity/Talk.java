package com.example.community.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.sql.Timestamp;

/**
 * トーク情報を保持するクラス
 * 
 * @author user
 *
 */
@Entity
@Table (name = "talks")
public class Talk {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    
    private String userId;
    
    @Size (min = 1, max = 50)
    @NotBlank
    private String message;
    
    private String imgName;
    private int editFlag;
    private int deleteFlag;
    private Timestamp createdAt;
    
    // ユーザー名 外部テーブル
    private int roomId;
    private String userName;
    private String roomName;
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public int getRoomId () {
        return roomId;
    }
    
    public void setRoomId (int roomId) {
        this.roomId = roomId;
    }
    
    public String getUserId () {
        return userId;
    }
    
    public void setUserId (String userId) {
        this.userId = userId;
    }
    
    public String getMessage () {
        return message;
    }
    
    public void setMessage (String message) {
        this.message = message;
    }
    
    public String getImgName () {
        return imgName;
    }
    
    public void setImgName (String imgName) {
        this.imgName = imgName;
    }
    
    public int getEditFlag () {
        return editFlag;
    }
    
    public void setEditFlag (int editFlag) {
        this.editFlag = editFlag;
    }
    
    public int getDeleteFlag () {
        return deleteFlag;
    }
    
    public void setDeleteFlag (int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
    
    public Timestamp getCreatedAt () {
        return createdAt;
    }
    
    public void setCreatedAt (Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUserName () {
        return userName;
    }
    
    public void setUserName (String userName) {
        this.userName = userName;
    }
    
    public String getRoomName () {
        return roomName;
    }
    
    public void setRoomName (String roomName) {
        this.roomName = roomName;
    }
    
}
