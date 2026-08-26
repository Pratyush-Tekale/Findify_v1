package com.findify.model;

import java.sql.Timestamp;
import java.util.List;


public class Claim {

    private int claimId;
    private int foundId;
    private int claimantId;
    private String status;
    private Timestamp claimDate;

    private String itemName;
    private String claimantName;
    private String claimantPhone;

    // Details pulled in from found_items, for the admin claim-detail view
    private String itemDescription;
    private String locationFound;
    private java.sql.Date dateFound;
    private String itemImage;

    // Verification-question result (replaces the old keyword trust score)
    private int matchedAnswers;
    private int totalQuestions;

    // Per-question breakdown for the admin review screen
    private List<ClaimAnswer> answers;

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


	public int getMatchedAnswers() {
		return matchedAnswers;
	}

	public void setMatchedAnswers(int matchedAnswers) {
		this.matchedAnswers = matchedAnswers;
	}

	public int getTotalQuestions() {
		return totalQuestions;
	}

	public void setTotalQuestions(int totalQuestions) {
		this.totalQuestions = totalQuestions;
	}

	/**
	 * 0-100 match percentage, used for the admin dashboard's colour-coded
	 * bar/pill. Returns 0 when there were no questions to avoid a
	 * divide-by-zero.
	 */
	public int getMatchPercentage() {
		if (totalQuestions <= 0) {
			return 0;
		}
		return (matchedAnswers * 100) / totalQuestions;
	}

	public List<ClaimAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<ClaimAnswer> answers) {
		this.answers = answers;
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
				+ ", matchedAnswers=" + matchedAnswers + ", totalQuestions=" + totalQuestions + "]";
	}

}
