package com.example.community.dto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * トーク作成時の情報を保持
 * 
 * @author user
 *
 */
public class TalkRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private int id;
    private int roomId;
    private String userId;
    private String message;
    private String imgName;
    private int editFlag;
    private int deleteFlag;
    private Timestamp createdAt;
    
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
    
    public static long getSerialversionuid () {
        return serialVersionUID;
    }
    
}
