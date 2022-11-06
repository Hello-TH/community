package com.example.community.dto;

import java.io.Serializable;

/**
 * ルーム作成時の情報を保持
 * 
 * @author user
 *
 */
public class RoomRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private int id;
    private String roomName;
    private String createUserId;
    private int approval;
    private int prefId;
    private int categoryId;
    private String feature;
    private String roomInfo;
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getCreateUserId () {
        return this.createUserId;
    }
    
    public void setCreateUserId (String createUserId) {
        this.createUserId = createUserId;
    }
    
    public String getRoomInfo () {
        return roomInfo;
    }
    
    public void setRoomInfo (String roomInfo) {
        this.roomInfo = roomInfo;
    }
    
    public void setRoomName (String roomName) {
        this.roomName = roomName;
    }
    
    public String getRoomName () {
        return roomName;
    }
    
    public int getApproval () {
        return approval;
    }
    
    public void setApproval (int approval) {
        this.approval = approval;
    }
    
    public int getPrefId () {
        return prefId;
    }
    
    public void setPrefId (int prefId) {
        this.prefId = prefId;
    }
    
    public int getCategoryId () {
        return categoryId;
    }
    
    public void setCategoryId (int categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getFeature () {
        return feature;
    }
    
    public void setFeature (String feature) {
        this.feature = feature;
    }
    
    public static long getSerialversionuid () {
        return serialVersionUID;
    }
    
}
