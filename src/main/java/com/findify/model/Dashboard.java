package com.findify.model;

public class Dashboard {

    private int lostItems;
    private int foundItems;
    private int pendingClaims;
    private int approvedClaims;

    public Dashboard() {

    }

    public int getLostItems() {
        return lostItems;
    }

    public void setLostItems(int lostItems) {
        this.lostItems = lostItems;
    }

    public int getFoundItems() {
        return foundItems;
    }

    public void setFoundItems(int foundItems) {
        this.foundItems = foundItems;
    }

    public int getPendingClaims() {
        return pendingClaims;
    }

    public void setPendingClaims(int pendingClaims) {
        this.pendingClaims = pendingClaims;
    }

    public int getApprovedClaims() {
        return approvedClaims;
    }

    public void setApprovedClaims(int approvedClaims) {
        this.approvedClaims = approvedClaims;
    }

}