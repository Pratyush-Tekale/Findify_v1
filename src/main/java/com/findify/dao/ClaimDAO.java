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

    /**
     * Inserts a claim and returns the generated claim_id, or -1 on failure.
     */
    public int addClaim(Claim claim) {
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

            if (ps.executeUpdate() == 0) {
                return -1;
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    claim.setClaimId(id);
                    return id;
                }
            }

            return -1;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }


    // =====================================================
    // GUARD: does this user already have a live claim
    // (PENDING / APPROVED / COLLECTED) on this found item?
    // REJECTED claims are ignored so a user may re-try once.
    // =====================================================
    public boolean hasActiveClaim(int foundId, int claimantId) {

        String sql =
            "SELECT COUNT(*) FROM claims " +
            "WHERE found_id = ? AND claimant_id = ? " +
            "AND status IN ('PENDING','APPROVED','COLLECTED')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, foundId);
            ps.setInt(2, claimantId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // =====================================================
    // GUARD: has this item already been awarded to somebody?
    // =====================================================
    public boolean isItemAlreadyAwarded(int foundId) {

        String sql =
            "SELECT COUNT(*) FROM claims " +
            "WHERE found_id = ? AND status IN ('APPROVED','COLLECTED')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, foundId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // =====================================================
    // OTHER PENDING CLAIMS ON THE SAME ITEM
    // (used to notify the losers when one claim is approved)
    // =====================================================
    public List<Claim> getPendingSiblings(int foundId, int excludeClaimId) {

        List<Claim> claims = new ArrayList<>();

        String sql = baseClaimQuery() +
                "WHERE c.found_id = ? AND c.claim_id <> ? AND c.status = 'PENDING'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, foundId);
            ps.setInt(2, excludeClaimId);

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

    /**
     * Approves a claim as a single transaction:
     *   1. this claim            -> APPROVED
     *   2. every other PENDING claim on the same item -> REJECTED
     *   3. the found item        -> CLAIMED
     * Either all three happen or none of them do.
     */
    public boolean approveClaim(int claimId, int foundId, int adminId) {

        String approveSql =
            "UPDATE claims SET status = 'APPROVED', rejection_reason = NULL, " +
            "handled_by = ? WHERE claim_id = ? AND status = 'PENDING'";

        String rejectSiblingsSql =
            "UPDATE claims SET status = 'REJECTED', handled_by = ?, " +
            "rejection_reason = 'Another claim for this item was approved.' " +
            "WHERE found_id = ? AND claim_id <> ? AND status = 'PENDING'";

        String itemSql =
            "UPDATE found_items SET status = 'CLAIMED' WHERE found_id = ?";

        Connection con = null;

        try {
            con = DBConnection.getConnection();

            if (con == null) {
                return false;
            }

            con.setAutoCommit(false);

            int updated;

            try (PreparedStatement ps = con.prepareStatement(approveSql)) {
                ps.setInt(1, adminId);
                ps.setInt(2, claimId);
                updated = ps.executeUpdate();
            }

            // Already approved / rejected by someone else - do nothing.
            if (updated == 0) {
                con.rollback();
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(rejectSiblingsSql)) {
                ps.setInt(1, adminId);
                ps.setInt(2, foundId);
                ps.setInt(3, claimId);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(itemSql)) {
                ps.setInt(1, foundId);
                ps.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) con.rollback();
            } catch (SQLException ignored) {
            }
            return false;

        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException ignored) {
            }
        }
    }


    public boolean rejectClaim(int claimId, int adminId, String reason) {

        String sql =
            "UPDATE claims SET status = 'REJECTED', handled_by = ?, " +
            "rejection_reason = ? WHERE claim_id = ? AND status = 'PENDING'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, adminId);
            ps.setString(2, (reason == null || reason.trim().isEmpty())
                    ? "The details provided did not sufficiently prove ownership."
                    : reason.trim());
            ps.setInt(3, claimId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Final step of the workflow: the owner physically collected the item
     * at the Lost and Found desk. APPROVED -> COLLECTED.
     */
    public boolean markCollected(int claimId, int adminId) {

        String sql =
            "UPDATE claims SET status = 'COLLECTED', collected_at = NOW(), " +
            "handled_by = ? WHERE claim_id = ? AND status = 'APPROVED'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, adminId);
            ps.setInt(2, claimId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Undo an approval: claim goes back to PENDING and the item is
     * released so other people can claim it again.
     */
    public boolean revertApproval(int claimId, int foundId) {

        Connection con = null;

        try {
            con = DBConnection.getConnection();

            if (con == null) {
                return false;
            }

            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE claims SET status = 'PENDING', collected_at = NULL " +
                    "WHERE claim_id = ? AND status = 'APPROVED'")) {
                ps.setInt(1, claimId);
                if (ps.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE found_items SET status = 'UNCLAIMED' WHERE found_id = ?")) {
                ps.setInt(1, foundId);
                ps.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) con.rollback();
            } catch (SQLException ignored) {
            }
            return false;

        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException ignored) {
            }
        }
    }


    public int getCollectedClaimsCount() {
        return getClaimsCountByStatus("COLLECTED");
    }


    public Claim getClaimById(int claimId) {
        String sql = baseClaimQuery() + "WHERE c.claim_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, claimId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapClaimWithDetails(rs);
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
        claim.setRejectionReason(rs.getString("rejection_reason"));
        claim.setCollectedAt(rs.getTimestamp("collected_at"));

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
