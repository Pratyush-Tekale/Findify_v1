package com.findify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.findify.model.Dashboard;
import com.findify.util.DBConnection;

public class DashboardDAO {

    public Dashboard getDashboardData(int userId) {

        Dashboard dashboard = new Dashboard();

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                System.out.println("Database connection failed!");
                return dashboard;
            }

            // ==============================
            // 1. LOST ITEMS
            // ==============================

            String lostSql =
                    "SELECT COUNT(*) FROM lost_items WHERE user_id=?";

            try (PreparedStatement ps = con.prepareStatement(lostSql)) {

                ps.setInt(1, userId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        dashboard.setLostItems(rs.getInt(1));
                    }
                }
            }


            // ==============================
            // 2. FOUND ITEMS
            // ==============================

            String foundSql =
                    "SELECT COUNT(*) FROM found_items WHERE user_id=?";

            try (PreparedStatement ps = con.prepareStatement(foundSql)) {

                ps.setInt(1, userId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        dashboard.setFoundItems(rs.getInt(1));
                    }
                }
            }


            // ==============================
            // 3. PENDING CLAIMS
            // ==============================

            String pendingSql =
                    "SELECT COUNT(*) FROM claims " +
                    "WHERE claimant_id=? AND status='PENDING'";

            try (PreparedStatement ps = con.prepareStatement(pendingSql)) {

                ps.setInt(1, userId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        dashboard.setPendingClaims(rs.getInt(1));
                    }
                }
            }


            // ==============================
            // 4. APPROVED CLAIMS
            // ==============================

            String approvedSql =
                    "SELECT COUNT(*) FROM claims " +
                    "WHERE claimant_id=? AND status='APPROVED'";

            try (PreparedStatement ps = con.prepareStatement(approvedSql)) {

                ps.setInt(1, userId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        dashboard.setApprovedClaims(rs.getInt(1));
                    }
                }
            }

            System.out.println("Dashboard data loaded successfully!");
            System.out.println("Lost Items = " + dashboard.getLostItems());
            System.out.println("Found Items = " + dashboard.getFoundItems());
            System.out.println("Pending Claims = " + dashboard.getPendingClaims());
            System.out.println("Approved Claims = " + dashboard.getApprovedClaims());

        } catch (SQLException e) {

            System.out.println("Dashboard SQL ERROR:");
            e.printStackTrace();
        }

        return dashboard;
    }
}