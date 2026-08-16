package com.findify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.findify.model.FoundItem;
import com.findify.util.DBConnection;

public class FoundItemDAO {

    // INSERT FOUND ITEM

    public boolean addFoundItem(FoundItem item) {

        boolean status = false;

        try {

            Connection con = DBConnection.getConnection();

            String query =
            "INSERT INTO found_items(user_id, category_id, item_name, description, location_found, date_found, image) VALUES(?,?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, item.getUserId());

            ps.setInt(2, item.getCategoryId());

            ps.setString(3, item.getItemName());

            ps.setString(4, item.getDescription());

            ps.setString(5, item.getLocationFound());

            ps.setString(6, item.getDateFound());

            ps.setString(7, item.getImage());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                status = true;
            }

            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return status;

    }



    // GET FOUND ITEM BY ID

    public FoundItem getFoundItemById(int id) {

        FoundItem item = null;

        try {

            Connection con = DBConnection.getConnection();

            String query =
            "SELECT * FROM found_items WHERE found_id=?";

            PreparedStatement ps =
            con.prepareStatement(query);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                item = new FoundItem();

                item.setFoundId(
                rs.getInt("found_id"));

                item.setUserId(
                rs.getInt("user_id"));

                item.setCategoryId(
                rs.getInt("category_id"));

                item.setItemName(
                rs.getString("item_name"));

                item.setDescription(
                rs.getString("description"));

                item.setLocationFound(
                rs.getString("location_found"));

                item.setDateFound(
                rs.getString("date_found"));

                item.setImage(
                rs.getString("image"));

                item.setStatus(
                rs.getString("status"));

            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return item;

    }



    // GET ALL FOUND ITEMS

    public ArrayList<FoundItem> getAllFoundItems() {

        ArrayList<FoundItem> list = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String query =
            		"SELECT f.*, c.category_name " +
            		"FROM found_items f " +
            		"JOIN categories c ON f.category_id = c.category_id " +
            		"ORDER BY f.created_at DESC";

            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                FoundItem item = new FoundItem();

                item.setFoundId(rs.getInt("found_id"));
                item.setUserId(rs.getInt("user_id"));
                item.setCategoryId(rs.getInt("category_id"));
                item.setCategoryName(rs.getString("category_name"));
                item.setItemName(rs.getString("item_name"));
                item.setDescription(rs.getString("description"));
                item.setLocationFound(rs.getString("location_found"));
                item.setDateFound(rs.getString("date_found"));
                item.setImage(rs.getString("image"));
                item.setStatus(rs.getString("status"));

                list.add(item);
            }

            rs.close();
            ps.close();
            con.close();

        }
        catch(Exception e) {

            e.printStackTrace();

        }

        return list;
    }
    
    public int getTotalFoundItems() {

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT COUNT(*) FROM found_items";

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