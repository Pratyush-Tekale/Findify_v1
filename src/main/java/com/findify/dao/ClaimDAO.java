package com.findify.dao;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.findify.model.Claim;
import com.findify.util.DBConnection;

public class ClaimDAO {

	public boolean addClaim(Claim claim) {
	    try (Connection con = DBConnection.getConnection()) {
	        String sql =
	            "INSERT INTO claims(found_id, claimant_id, proof, status, trust_score) " +
	            "VALUES (?, ?, ?, ?, ?)";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setInt(1, claim.getFoundId());
	        ps.setInt(2, claim.getClaimantId());
	        ps.setString(3, claim.getProof());
	        ps.setString(4, claim.getStatus());
	        ps.setInt(5, claim.getTrustScore());

	        int rows = ps.executeUpdate();

	        return rows > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return false;
	}

    public List<Claim> getPendingClaims() {
        List<Claim> claims = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM claims WHERE status = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "PENDING");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Claim claim = new Claim();
                claim.setClaimId(rs.getInt("claim_id"));
                claim.setFoundId(rs.getInt("found_id"));
                claim.setClaimantId(rs.getInt("claimant_id"));
                claim.setProof(rs.getString("proof"));
                claim.setStatus(rs.getString("status"));
                claim.setClaimDate(rs.getTimestamp("claim_date"));
                claim.setTrustScore(rs.getInt("trust_score"));

                claims.add(claim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return claims;
    }

    public List<Claim> getClaimsByStatus(String status) {
        List<Claim> claims = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql =
                    "SELECT c.*, " +
                    "f.item_name, " +
                    "f.description, " +
                    "f.location_found, " +
                    "f.date_found, " +
                    "f.image, " +
                    "u.full_name, " +
                    "u.phone " +
                    "FROM claims c " +
                    "LEFT JOIN found_items f " +
                    "ON c.found_id = f.found_id " +
                    "LEFT JOIN users u " +
                    "ON c.claimant_id = u.user_id " +
                    "WHERE c.status = ? " +
                    "ORDER BY c.claim_date DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Claim claim = new Claim();
                claim.setClaimId(rs.getInt("claim_id"));
                claim.setFoundId(rs.getInt("found_id"));
                claim.setClaimantId(rs.getInt("claimant_id"));
                claim.setProof(rs.getString("proof"));
                claim.setStatus(rs.getString("status"));
                claim.setClaimDate(rs.getTimestamp("claim_date"));

                claim.setItemName(rs.getString("item_name"));
                claim.setItemDescription(rs.getString("description"));
                claim.setLocationFound(rs.getString("location_found"));
                claim.setDateFound(rs.getDate("date_found"));
                claim.setItemImage(rs.getString("image"));
                claim.setClaimantName(rs.getString("full_name"));
                claim.setClaimantPhone(rs.getString("phone"));
                claim.setTrustScore(rs.getInt("trust_score"));

                claims.add(claim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return claims;
    }

    public List<Claim> getAllClaims() {

        List<Claim> claims = new ArrayList<>();

        try (Connection con = DBConnection.getConnection()) {

            String sql =
                    "SELECT c.*, " +
                    "f.item_name, " +
                    "f.description, " +
                    "f.location_found, " +
                    "f.date_found, " +
                    "f.image, " +
                    "u.full_name, " +
                    "u.phone " +
                    "FROM claims c " +
                    "LEFT JOIN found_items f " +
                    "ON c.found_id = f.found_id " +
                    "LEFT JOIN users u " +
                    "ON c.claimant_id = u.user_id " +
                    "WHERE c.trust_score >= 75 " +
                    "ORDER BY c.claim_date DESC";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Claim claim = new Claim();

                claim.setClaimId(rs.getInt("claim_id"));
                claim.setFoundId(rs.getInt("found_id"));
                claim.setClaimantId(rs.getInt("claimant_id"));

                claim.setProof(rs.getString("proof"));
                claim.setStatus(rs.getString("status"));
                claim.setClaimDate(rs.getTimestamp("claim_date"));

                claim.setItemName(rs.getString("item_name"));
                claim.setItemDescription(rs.getString("description"));
                claim.setLocationFound(rs.getString("location_found"));
                claim.setDateFound(rs.getDate("date_found"));
                claim.setItemImage(rs.getString("image"));

                claim.setClaimantName(rs.getString("full_name"));
                claim.setClaimantPhone(rs.getString("phone"));

                claim.setTrustScore(rs.getInt("trust_score"));

                claims.add(claim);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return claims;
    }
    /**
     * Most recent claims regardless of status/trust score — used for the
     * admin dashboard "Recent Activity" feed. Unlike getAllClaims(), this
     * is NOT filtered by trust_score, since the activity feed should show
     * everything that just happened, including low-trust claims.
     */
    public List<Claim> getRecentClaims(int limit) {
        List<Claim> claims = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql =
                    "SELECT c.*, " +
                    "f.item_name, " +
                    "f.description, " +
                    "f.location_found, " +
                    "f.date_found, " +
                    "f.image, " +
                    "u.full_name, " +
                    "u.phone " +
                    "FROM claims c " +
                    "LEFT JOIN found_items f " +
                    "ON c.found_id = f.found_id " +
                    "LEFT JOIN users u " +
                    "ON c.claimant_id = u.user_id " +
                    "ORDER BY c.claim_date DESC " +
                    "LIMIT ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Claim claim = new Claim();
                claim.setClaimId(rs.getInt("claim_id"));
                claim.setFoundId(rs.getInt("found_id"));
                claim.setClaimantId(rs.getInt("claimant_id"));
                claim.setProof(rs.getString("proof"));
                claim.setStatus(rs.getString("status"));
                claim.setClaimDate(rs.getTimestamp("claim_date"));

                claim.setItemName(rs.getString("item_name"));
                claim.setItemDescription(rs.getString("description"));
                claim.setLocationFound(rs.getString("location_found"));
                claim.setDateFound(rs.getDate("date_found"));
                claim.setItemImage(rs.getString("image"));
                claim.setClaimantName(rs.getString("full_name"));
                claim.setClaimantPhone(rs.getString("phone"));
                claim.setTrustScore(rs.getInt("trust_score"));

                claims.add(claim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return claims;
    }

    public List<Claim> searchClaims(String search) {
        List<Claim> claims = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
        	String sql =
        	        "SELECT c.*, " +
        	        "f.item_name, " +
        	        "f.description, " +
        	        "f.location_found, " +
        	        "f.date_found, " +
        	        "f.image, " +
        	        "u.full_name, " +
        	        "u.phone " +
        	        "FROM claims c " +
        	        "LEFT JOIN found_items f " +
        	        "ON c.found_id = f.found_id " +
        	        "LEFT JOIN users u " +
        	        "ON c.claimant_id = u.user_id " +
        	        "WHERE (f.item_name LIKE ? " +
        	        "OR u.full_name LIKE ? " +
        	        "OR c.proof LIKE ?) " +
        	        "AND c.trust_score >= 75 " +
        	        "ORDER BY c.claim_date DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + search + "%");
            ps.setString(2, "%" + search + "%");
            ps.setString(3, "%" + search + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Claim claim = new Claim();
                claim.setClaimId(rs.getInt("claim_id"));
                claim.setFoundId(rs.getInt("found_id"));
                claim.setClaimantId(rs.getInt("claimant_id"));

                claim.setItemName(rs.getString("item_name"));
                claim.setItemDescription(rs.getString("description"));
                claim.setLocationFound(rs.getString("location_found"));
                claim.setDateFound(rs.getDate("date_found"));
                claim.setItemImage(rs.getString("image"));
                claim.setClaimantName(rs.getString("full_name"));
                claim.setClaimantPhone(rs.getString("phone"));

                claim.setProof(rs.getString("proof"));
                claim.setStatus(rs.getString("status"));
                claim.setClaimDate(rs.getTimestamp("claim_date"));
                claim.setTrustScore(rs.getInt("trust_score"));

                claims.add(claim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return claims;
    }

    public boolean approveClaim(int claimId) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE CLAIMS SET STATUS = ? WHERE CLAIM_ID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "APPROVED");
            ps.setInt(2, claimId);

            int rows = ps.executeUpdate();

            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean rejectClaim(int claimId) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE CLAIMS SET STATUS = ? WHERE CLAIM_ID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "REJECTED");
            ps.setInt(2, claimId);

            int rows = ps.executeUpdate();

            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Claim getClaimById(int claimId) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM claims WHERE claim_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, claimId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Claim claim = new Claim();
                claim.setClaimId(rs.getInt("claim_id"));
                claim.setFoundId(rs.getInt("found_id"));
                claim.setClaimantId(rs.getInt("claimant_id"));
                claim.setProof(rs.getString("proof"));
                claim.setStatus(rs.getString("status"));
                claim.setClaimDate(rs.getTimestamp("claim_date"));
                claim.setTrustScore(rs.getInt("trust_score"));

                return claim;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getPendingClaimsCount() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM claims WHERE status = 'PENDING'";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("SQL ERROR:");
            e.printStackTrace();
        }

        return 0;
    }

    public int getApprovedClaimsCount() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM claims WHERE status = 'APPROVED'";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("SQL ERROR:");
            e.printStackTrace();
        }

        return 0;
    }

    public int getRejectedClaimsCount() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM claims WHERE status = 'REJECTED'";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("SQL ERROR:");
            e.printStackTrace();
        }

        return 0;
    }
}