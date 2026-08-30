package com.findify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.findify.model.Notification;
import com.findify.util.DBConnection;

public class NotificationDAO {

    // =====================================================
    // ADD NOTIFICATION
    // =====================================================

    public boolean addNotification(int userId, String message) {

        String sql =
                "INSERT INTO notifications " +
                "(user_id, message, is_read) " +
                "VALUES (?, ?, 0)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, message);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }


    // =====================================================
    // GET ALL NOTIFICATIONS FOR A USER
    // =====================================================

    public List<Notification> getUserNotifications(int userId) {

        List<Notification> notifications = new ArrayList<>();

        String sql =
                "SELECT * FROM notifications " +
                "WHERE user_id = ? " +
                "ORDER BY created_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Notification notification =
                            new Notification();

                    notification.setNotificationId(
                            rs.getInt("notification_id")
                    );

                    notification.setUserId(
                            rs.getInt("user_id")
                    );

                    notification.setMessage(
                            rs.getString("message")
                    );

                    notification.setRead(
                            rs.getBoolean("is_read")
                    );

                    notification.setCreatedAt(
                            rs.getTimestamp("created_at")
                    );

                    notifications.add(notification);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return notifications;
    }


    // =====================================================
    // GET UNREAD NOTIFICATION COUNT
    // =====================================================

    public int getUnreadCount(int userId) {

        String sql =
                "SELECT COUNT(*) FROM notifications " +
                "WHERE user_id = ? AND is_read = 0";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return rs.getInt(1);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }


    // =====================================================
    // MARK ONE NOTIFICATION AS READ
    // =====================================================

    public boolean markAsRead(int notificationId, int userId) {

        String sql =
                "UPDATE notifications " +
                "SET is_read = 1 " +
                "WHERE notification_id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, notificationId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }


    // =====================================================
    // MARK ALL NOTIFICATIONS AS READ
    // =====================================================

    public boolean markAllAsRead(int userId) {

        String sql =
                "UPDATE notifications " +
                "SET is_read = 1 " +
                "WHERE user_id = ? AND is_read = 0";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            return ps.executeUpdate() >= 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
}