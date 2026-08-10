package com.findify.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.findify.model.LostItem;
import com.findify.util.DBConnection;


public class LostItemDAO {


    // INSERT LOST ITEM

    public boolean addLostItem(LostItem item) {


        boolean status = false;


        try {

            Connection con = DBConnection.getConnection();


            String query = 
            "INSERT INTO lost_items(user_id, category_id, item_name, description, location_lost, date_lost, image) VALUES(?,?,?,?,?,?,?)";


            PreparedStatement ps = con.prepareStatement(query);


            ps.setInt(1, item.getUserId());

            ps.setInt(2, item.getCategoryId());

            ps.setString(3, item.getItemName());

            ps.setString(4, item.getDescription());

            ps.setString(5, item.getLocationLost());

            ps.setDate(6, item.getDateLost());

            ps.setString(7, item.getImage());


            int rows = ps.executeUpdate();

            if(rows > 0) {
                status = true;
            }

            ps.close();
            con.close();


        } catch(Exception e) {

            e.printStackTrace();

        }


        return status;

    }


    public List<LostItem> getAllLostItems() {

        List<LostItem> lostItems = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String sql = 
            "SELECT * FROM lost_items ORDER BY created_at DESC";


            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();


            while(rs.next()) {

                LostItem item = new LostItem();

                item.setLostId(rs.getInt("lost_id"));
                item.setUserId(rs.getInt("user_id"));
                item.setCategoryId(rs.getInt("category_id"));

                item.setItemName(
                    rs.getString("item_name")
                );

                item.setDescription(
                    rs.getString("description")
                );

                item.setLocationLost(
                    rs.getString("location_lost")
                );

                item.setDateLost(
                    rs.getDate("date_lost")
                );

                item.setImage(
                    rs.getString("image")
                );

                item.setStatus(
                    rs.getString("status")
                );


                lostItems.add(item);
            }


        } catch(Exception e) {

            e.printStackTrace();

        }


        return lostItems;
    }


    // GET LOST ITEM BY ID

    public LostItem getLostItemById(int id) {


        LostItem item = null;


        try {


            Connection con = DBConnection.getConnection();


            String query =
            "SELECT * FROM lost_items WHERE lost_id=?";


            PreparedStatement ps =
            con.prepareStatement(query);


            ps.setInt(1,id);


            ResultSet rs = ps.executeQuery();



            if(rs.next()) {


                item = new LostItem();


                item.setLostId(
                rs.getInt("lost_id"));


                item.setUserId(
                rs.getInt("user_id"));


                item.setCategoryId(
                rs.getInt("category_id"));


                item.setItemName(
                rs.getString("item_name"));


                item.setDescription(
                rs.getString("description"));


                item.setLocationLost(
                rs.getString("location_lost"));


                item.setDateLost(
                rs.getDate("date_lost"));


                item.setImage(
                rs.getString("image"));


                item.setStatus(
                rs.getString("status"));

            }


            rs.close();
            ps.close();
            con.close();


        }catch(Exception e){

            e.printStackTrace();

        }



        return item;

    }

}