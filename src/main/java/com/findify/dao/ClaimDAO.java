package com.findify.dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.findify.model.Claim;
import com.findify.util.DBConnection;

public class ClaimDAO {

    public boolean addClaim(Claim claim) {
        String sql =
            "INSERT INTO claims(found_id, claimant_id, status, " +
            "submitted_description, ai_match, ai_confidence, ai_reasoning) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, claim.getFoundId());
            ps.setInt(2, claim.getClaimantId());
            ps.setString(3, claim.getStatus());
            ps.setString(4, claim.getSubmittedDescription());
            ps.setBoolean(5, claim.isAiMatch());
            ps.setInt(6, claim.getAiConfidence());
            ps.setString(7, claim.getAiReasoning());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Claim> getPendingClaims() {
        return getClaimsByStatus("PENDING");
    }

    public List<Claim> getClaimsByStatus(String status) {
        List<Claim> claims = new ArrayList<>();
        String sql = baseClaimQuery() +
                "WHERE c.status = ? " +
                "ORDER BY c.claim_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    claims.add(mapClaimWithDetails(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return claims;
    }

    public List<Claim> getAllClaims() {
        List<Claim> claims = new ArrayList<>();
        String sql = baseClaimQuery() + "ORDER BY c.claim_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                claims.add(mapClaimWithDetails(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return claims;
    }

    public List<Claim> getRecentClaims(int limit) {
        List<Claim> claims = new ArrayList<>();
        String sql = baseClaimQuery() + "ORDER BY c.claim_date DESC LIMIT ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    claims.add(mapClaimWithDetails(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return claims;
    }

    public List<Claim> searchClaims(String search) {
        List<Claim> claims = new ArrayList<>();
        String sql = baseClaimQuery() +
                "WHERE f.item_name LIKE ? OR u.full_name LIKE ? " +
                "ORDER BY c.claim_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String pattern = "%" + search + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    claims.add(mapClaimWithDetails(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return claims;
    }

    public boolean approveClaim(int claimId) {
        return updateClaimStatus(claimId, "APPROVED");
    }

    public boolean rejectClaim(int claimId) {
        return updateClaimStatus(claimId, "REJECTED");
    }

    private boolean updateClaimStatus(int claimId, String status) {
        String sql = "UPDATE claims SET status = ? WHERE claim_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, claimId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Claim getClaimById(int claimId) {
        String sql = "SELECT * FROM claims WHERE claim_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, claimId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapClaim(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getPendingClaimsCount() {
        return getClaimsCountByStatus("PENDING");
    }

    public int getApprovedClaimsCount() {
        return getClaimsCountByStatus("APPROVED");
    }

    public int getRejectedClaimsCount() {
        return getClaimsCountByStatus("REJECTED");
    }

    private int getClaimsCountByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM claims WHERE status = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private String baseClaimQuery() {
        return "SELECT c.*, " +
                "f.item_name, f.description, f.location_found, f.date_found, f.image, " +
                "u.full_name, u.phone " +
                "FROM claims c " +
                "LEFT JOIN found_items f ON c.found_id = f.found_id " +
                "LEFT JOIN users u ON c.claimant_id = u.user_id ";
    }

    private Claim mapClaimWithDetails(ResultSet rs) throws SQLException {
        Claim claim = mapClaim(rs);

        claim.setItemName(rs.getString("item_name"));
        claim.setItemDescription(rs.getString("description"));
        claim.setLocationFound(rs.getString("location_found"));
        claim.setDateFound(rs.getDate("date_found"));
        claim.setItemImage(rs.getString("image"));
        claim.setClaimantName(rs.getString("full_name"));
        claim.setClaimantPhone(rs.getString("phone"));

        return claim;
    }

    private Claim mapClaim(ResultSet rs) throws SQLException {
        Claim claim = new Claim();

        claim.setClaimId(rs.getInt("claim_id"));
        claim.setFoundId(rs.getInt("found_id"));
        claim.setClaimantId(rs.getInt("claimant_id"));
        claim.setStatus(rs.getString("status"));
        claim.setClaimDate(rs.getTimestamp("claim_date"));
        claim.setSubmittedDescription(rs.getString("submitted_description"));
        claim.setAiMatch(rs.getBoolean("ai_match"));
        claim.setAiConfidence(rs.getInt("ai_confidence"));
        claim.setAiReasoning(rs.getString("ai_reasoning"));

        return claim;
    }
    
 // =====================================================
 // GET CLAIMS MADE BY A USER
 // =====================================================
 public List<Claim> getClaimsByUser(int userId) {

     List<Claim> claims = new ArrayList<>();

     String sql =
         baseClaimQuery() +
         "WHERE c.claimant_id = ? " +
         "ORDER BY c.claim_date DESC";

     try (Connection con = DBConnection.getConnection();
          PreparedStatement ps = con.prepareStatement(sql)) {

         ps.setInt(1, userId);

         try (ResultSet rs = ps.executeQuery()) {

             while (rs.next()) {

                 claims.add(
                     mapClaimWithDetails(rs)
                 );
             }
         }

     } catch (SQLException e) {

         e.printStackTrace();
     }

     return claims;
 }
 
 
}
