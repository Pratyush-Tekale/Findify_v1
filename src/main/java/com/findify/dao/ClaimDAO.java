package com.findify.dao;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.findify.model.Claim;
import com.findify.model.ClaimAnswer;
import com.findify.util.DBConnection;

public class ClaimDAO {

	private final ClaimAnswerDAO answerDao = new ClaimAnswerDAO();

	/**
	 * Inserts the claim row plus one claim_answers row per submitted
	 * verification answer, in a single transaction (all rows share the
	 * generated claim_id, so either everything is saved or nothing is).
	 */
	public boolean addClaim(Claim claim, List<ClaimAnswer> answers) {

	    Connection con = null;

	    try {
	        con = DBConnection.getConnection();
	        con.setAutoCommit(false);

	        String sql =
	            "INSERT INTO claims(found_id, claimant_id, status, matched_answers, total_questions) " +
	            "VALUES (?, ?, ?, ?, ?)";
	        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        ps.setInt(1, claim.getFoundId());
	        ps.setInt(2, claim.getClaimantId());
	        ps.setString(3, claim.getStatus());
	        ps.setInt(4, claim.getMatchedAnswers());
	        ps.setInt(5, claim.getTotalQuestions());

	        int rows = ps.executeUpdate();

	        if (rows == 0) {
	            con.rollback();
	            return false;
	        }

	        int claimId = 0;
	        ResultSet keys = ps.getGeneratedKeys();
	        if (keys.next()) {
	            claimId = keys.getInt(1);
	        }

	        if (answers != null) {
	            for (ClaimAnswer a : answers) {
	                answerDao.addAnswer(con, claimId, a.getQuestionId(),
	                        a.getSubmittedAnswer(), a.isCorrect());
	            }
	        }

	        con.commit();
	        return true;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        if (con != null) {
	            try { con.rollback(); } catch (SQLException ignored) { }
	        }
	    } finally {
	        if (con != null) {
	            try { con.setAutoCommit(true); con.close(); } catch (SQLException ignored) { }
	        }
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
                claim.setStatus(rs.getString("status"));
                claim.setClaimDate(rs.getTimestamp("claim_date"));
                claim.setMatchedAnswers(rs.getInt("matched_answers"));
                claim.setTotalQuestions(rs.getInt("total_questions"));

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
                claims.add(mapClaimWithDetails(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        attachAnswers(claims);
        return claims;
    }

    /**
     * All claims whose verification match rate is at least 75% — the
     * default "trustworthy" view on the admin dashboard. Equivalent to the
     * old trust_score >= 75 filter, now computed from matched/total
     * questions. Claims with zero questions never qualify.
     */
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
                    "WHERE c.total_questions > 0 " +
                    "AND (c.matched_answers / c.total_questions) >= 0.75 " +
                    "ORDER BY c.claim_date DESC";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                claims.add(mapClaimWithDetails(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        attachAnswers(claims);
        return claims;
    }

    /**
     * Most recent claims regardless of status/match rate — used for the
     * admin dashboard "Recent Activity" feed. Unlike getAllClaims(), this
     * is NOT filtered by match rate, since the activity feed should show
     * everything that just happened, including low-match claims.
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
                claims.add(mapClaimWithDetails(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        attachAnswers(claims);
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
        	        "OR u.full_name LIKE ?) " +
        	        "AND c.total_questions > 0 " +
        	        "AND (c.matched_answers / c.total_questions) >= 0.75 " +
        	        "ORDER BY c.claim_date DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + search + "%");
            ps.setString(2, "%" + search + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                claims.add(mapClaimWithDetails(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        attachAnswers(claims);
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
                claim.setStatus(rs.getString("status"));
                claim.setClaimDate(rs.getTimestamp("claim_date"));
                claim.setMatchedAnswers(rs.getInt("matched_answers"));
                claim.setTotalQuestions(rs.getInt("total_questions"));

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

    private Claim mapClaimWithDetails(ResultSet rs) throws SQLException {
        Claim claim = new Claim();

        claim.setClaimId(rs.getInt("claim_id"));
        claim.setFoundId(rs.getInt("found_id"));
        claim.setClaimantId(rs.getInt("claimant_id"));
        claim.setStatus(rs.getString("status"));
        claim.setClaimDate(rs.getTimestamp("claim_date"));
        claim.setMatchedAnswers(rs.getInt("matched_answers"));
        claim.setTotalQuestions(rs.getInt("total_questions"));

        claim.setItemName(rs.getString("item_name"));
        claim.setItemDescription(rs.getString("description"));
        claim.setLocationFound(rs.getString("location_found"));
        claim.setDateFound(rs.getDate("date_found"));
        claim.setItemImage(rs.getString("image"));
        claim.setClaimantName(rs.getString("full_name"));
        claim.setClaimantPhone(rs.getString("phone"));

        return claim;
    }

    // Attaches the per-question breakdown to each claim for the admin
    // detail modal. One query per claim — fine at prototype scale.
    private void attachAnswers(List<Claim> claims) {
        for (Claim c : claims) {
            c.setAnswers(answerDao.getAnswersByClaimId(c.getClaimId()));
        }
    }
}
