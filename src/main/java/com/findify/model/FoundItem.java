package com.findify.model;

public class FoundItem {

    private int foundId;
    private int userId;
    private int categoryId;
    private String itemName;
    private String description;
    private String locationFound;
    private String dateFound;
    private String image;
    private String status;
    private String categoryName;


    public FoundItem() {

    }


    public FoundItem(int userId,
                     int categoryId,
                     String itemName,
                     String description,
                     String locationFound,
                     String dateFound,
                     String image) {

        this.userId = userId;
        this.categoryId = categoryId;
        this.itemName = itemName;
        this.description = description;
        this.locationFound = locationFound;
        this.dateFound = dateFound;
        this.image = image;
    }


    public int getFoundId() {
        return foundId;
    }

    public void setFoundId(int foundId) {
        this.foundId = foundId;
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


    public String getLocationFound() {
        return locationFound;
    }

    public void setLocationFound(String locationFound) {
        this.locationFound = locationFound;
    }


    public String getDateFound() {
        return dateFound;
    }

    public void setDateFound(String dateFound) {
        this.dateFound = dateFound;
    }
    
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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