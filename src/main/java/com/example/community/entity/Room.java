package com.example.community.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import java.sql.Timestamp;

/**
 * 部屋に関しての情報を保持するクラス
 * 
 * @author user
 *
 */
@Entity
@Table (name = "rooms")
public class Room {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    
    private String createUserId;
    
    @Size (min = 1, max = 20, message = "{Size.Room.roomName}")
    @NotBlank (message = "{NotBlank.Room.roomName}")
    private String roomName;
    
    private int people;
    
    private int good;
    
    private int bad;
    
    @Range (min = 1, max = 2, message = "{Range.Room.approval}")
    private int approval;
    
    @Range (min = 1, max = 49, message = "{Range.Room.prefId}")
    private int prefId;
    
    private String prefName;
    
    @Range (min = 1, max = 11, message = "{Range.Room.categoryId}")
    private int categoryId;
    
    private String categoryName;
    
    @Size (min = 1, max = 50, message = "{Size.Room.feature}")
    @NotBlank (message = "{NotBlank.Room.feature}")
    private String feature;
    
    @Size (min = 1, max = 500, message = "{Size.Room.roomInfo}")
    @NotBlank (message = "{NotBlank.Room.roomInfo}")
    private String roomInfo;
    
    private Timestamp createdAt;
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getCreateUserId () {
        return createUserId;
    }
    
    public void setCreateUserId (String createUserId) {
        this.createUserId = createUserId;
    }
    
    public String getRoomName () {
        return roomName;
    }
    
    public void setRoomName (String roomName) {
        this.roomName = roomName;
    }
    
    public int getPeople () {
        return people;
    }
    
    public void setPeople (int people) {
        this.people = people;
    }
    
    public int getGood () {
        return good;
    }
    
    public void setGood (int good) {
        this.good = good;
    }
    
    public int getBad () {
        return bad;
    }
    
    public void setBad (int bad) {
        this.bad = bad;
    }
    
    public int getPrefId () {
        return prefId;
    }
    
    public void setPrefId (int prefId) {
        this.prefId = prefId;
    }
    
    public String getPrefName () {
        return prefName;
    }
    
    public void setPrefName (String prefName) {
        this.prefName = prefName;
    }
    
    public String getCategoryName () {
        return categoryName;
    }
    
    public void setCategoryName (String categoryName) {
        this.categoryName = categoryName;
    }
    
    public int getApproval () {
        return approval;
    }
    
    public void setApproval (int approval) {
        this.approval = approval;
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
    
    public String getRoomInfo () {
        return roomInfo;
    }
    
    public void setRoomInfo (String roomInfo) {
        this.roomInfo = roomInfo;
    }
    
    public Timestamp getCreatedAt () {
        return createdAt;
    }
    
    public void setCreatedAt (Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
}