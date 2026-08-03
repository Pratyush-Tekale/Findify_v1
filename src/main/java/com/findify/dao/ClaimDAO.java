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

	        String sql = "INSERT INTO claims (found_id, claimant_id, proof) VALUES (?,?,?)";

	        PreparedStatement ps = con.prepareStatement(sql);

	        ps.setInt(1, claim.getFoundId());
	        ps.setInt(2, claim.getClaimantId());
	        ps.setString(3, claim.getProof());

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

        String sql = "SELECT * FROM claims WHERE status = ?";

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

            claims.add(claim);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return claims;
}

public boolean approveClaim(int claimId)
{
	try(Connection con=DBConnection.getConnection())
	{
		String sql="UPDATE CLAIMS SET STATUS =? WHERE CLAIM_ID=?";
		PreparedStatement ps = con.prepareStatement(sql);
		
		ps.setString(1, "APPROVED");
		ps.setInt(2, claimId);
	
		int rows = ps.executeUpdate();
		if (rows > 0) {
		    return true;
		}
		else {
			return false;
		}
	
	}catch (SQLException e) {
        e.printStackTrace();
    }


	return false;
	}

public boolean rejectClaim(int claimId)
{
	try(Connection con=DBConnection.getConnection())
	{
		String sql="UPDATE CLAIMS SET STATUS =? WHERE CLAIM_ID=?";
		PreparedStatement ps = con.prepareStatement(sql);
		
		ps.setString(1, "REJECTED");
		ps.setInt(2, claimId);
	
		int rows = ps.executeUpdate();
		if (rows > 0) {
		    return true;
		}
		else {
			return false;
		}
	
	}catch (SQLException e) {
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

            return claim;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }


    return null;
}



public List<Claim> getAllClaims(){
    List<Claim> claims = new ArrayList<>();

	try(Connection con=DBConnection.getConnection())
	{
		String sql="SELECT * FROM CLAIMS ORDER BY CLAIM_DATE DESC";
		PreparedStatement ps=con.prepareStatement(sql);
		
		 ResultSet rs = ps.executeQuery();

	        while (rs.next()) {

	            Claim claim = new Claim();

	            claim.setClaimId(rs.getInt("claim_id"));
	            claim.setFoundId(rs.getInt("found_id"));
	            claim.setClaimantId(rs.getInt("claimant_id"));
	            claim.setProof(rs.getString("proof"));
	            claim.setStatus(rs.getString("status"));
	            claim.setClaimDate(rs.getTimestamp("claim_date"));

	            claims.add(claim);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return claims;
	}
		
	
}
