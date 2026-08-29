package com.findify.model;

import java.sql.Timestamp;

public class Claim {

    private int claimId;
    private int foundId;
    private int claimantId;
    private String status;
    private Timestamp claimDate;

    private String itemName;
    private String claimantName;
    private String claimantPhone;

    private String itemDescription;
    private String locationFound;
    private java.sql.Date dateFound;
    private String itemImage;

    private String submittedDescription;
    private boolean aiMatch;
    private int aiConfidence;
    private String aiReasoning;

    public Claim() {
    }

    public Claim(int claimId, int foundId, int claimantId,
            String status, Timestamp claimDate) {
        this.claimId = claimId;
        this.foundId = foundId;
        this.claimantId = claimantId;
        this.status = status;
        this.claimDate = claimDate;
    }

    public String getSubmittedDescription() {
        return submittedDescription;
    }

    public void setSubmittedDescription(String submittedDescription) {
        this.submittedDescription = submittedDescription;
    }

    public boolean isAiMatch() {
        return aiMatch;
    }

    public void setAiMatch(boolean aiMatch) {
        this.aiMatch = aiMatch;
    }

    public int getAiConfidence() {
        return aiConfidence;
    }

    public void setAiConfidence(int aiConfidence) {
        this.aiConfidence = aiConfidence;
    }

    public String getAiReasoning() {
        return aiReasoning;
    }

    public void setAiReasoning(String aiReasoning) {
        this.aiReasoning = aiReasoning;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getClaimantName() {
        return claimantName;
    }

    public void setClaimantName(String claimantName) {
        this.claimantName = claimantName;
    }

    public String getClaimantPhone() {
        return claimantPhone;
    }

    public void setClaimantPhone(String claimantPhone) {
        this.claimantPhone = claimantPhone;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getLocationFound() {
        return locationFound;
    }

    public void setLocationFound(String locationFound) {
        this.locationFound = locationFound;
    }

    public java.sql.Date getDateFound() {
        return dateFound;
    }

    public void setDateFound(java.sql.Date dateFound) {
        this.dateFound = dateFound;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public int getFoundId() {
        return foundId;
    }

    public void setFoundId(int foundId) {
        this.foundId = foundId;
    }

    public int getClaimantId() {
        return claimantId;
    }

    public void setClaimantId(int claimantId) {
        this.claimantId = claimantId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Timestamp claimDate) {
        this.claimDate = claimDate;
    }

    @Override
    public String toString() {
        return "Claim [claimId=" + claimId + ", foundId=" + foundId + ", claimantId=" + claimantId
                + ", status=" + status + ", claimDate=" + claimDate
                + ", aiMatch=" + aiMatch + ", aiConfidence=" + aiConfidence + "]";
    }
}
