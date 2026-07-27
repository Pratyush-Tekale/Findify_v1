package com.findify.model;

import java.sql.Timestamp;


public class Claim {

    private int claimId;
    private int foundId;
    private int claimantId;
    private String proof;
    private String status;
    private Timestamp claimDate;
    
    
    public Claim() {
    }
    
    public Claim(int claimId, int foundId, int claimantId, String proof,
            String status, Timestamp claimDate) {
    	
   this.claimId = claimId;
   this.foundId = foundId;
   this.claimantId = claimantId;
   this.proof = proof;
   this.status = status;
   this.claimDate = claimDate;
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
	public String getProof() {
		return proof;
	}
	public void setProof(String proof) {
		this.proof = proof;
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
		return "Claim [claimId=" + claimId + ", foundId=" + foundId + ", claimantId=" + claimantId + ", proof=" + proof
				+ ", status=" + status + ", claimDate=" + claimDate + "]";
	}
    
    
    
    
    
}