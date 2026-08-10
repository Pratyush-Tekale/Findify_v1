package com.findify.model;

import java.sql.Date;

public class LostItem {

    private int lostId;
    private int userId;
    private int categoryId;
    private String itemName;
    private String description;
    private String locationLost;
    private Date dateLost;
    private String image;
    private String status;


    public LostItem() {

    }


    public LostItem(int userId, int categoryId, String itemName,
                    String description, String locationLost,
                    Date dateLost, String image) {

        this.userId = userId;
        this.categoryId = categoryId;
        this.itemName = itemName;
        this.description = description;
        this.locationLost = locationLost;
        this.dateLost = dateLost;
        this.image = image;
    }



    public int getLostId() {
        return lostId;
    }

    public void setLostId(int lostId) {
        this.lostId = lostId;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getLocationLost() {
        return locationLost;
    }

    public void setLocationLost(String locationLost) {
        this.locationLost = locationLost;
    }


    public Date getDateLost() {
        return dateLost;
    }

    public void setDateLost(Date dateLost) {
        this.dateLost = dateLost;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}