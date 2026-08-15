package com.findify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.findify.model.User;
import com.findify.util.DBConnection;

public class UserDAO {

    public User login(String email, String password) {
    	try(Connection con=DBConnection.getConnection())
    	{
    		String sql="select *from users where email=? and password=?";
    		PreparedStatement ps = con.prepareStatement(sql);

    		
    		ps.setString(1,email);
    		ps.setString(2, password);
    		
        	ResultSet rs = ps.executeQuery();
    		if(rs.next()) {
    			User user = new User();

    			user.setUserId(rs.getInt("user_id"));
    			user.setFullName(rs.getString("full_name"));
    			user.setEmail(rs.getString("email"));
    			user.setRole(rs.getString("role"));
    			user.setPhone(rs.getString("phone"));
    			user.setPassword(rs.getString("password"));
    			user.setCreatedAt(rs.getTimestamp("created_at"));		
    			
    			return user;
    		}
			}catch (SQLException e) {
		        e.printStackTrace();
		    
			}
		return null;
    			}
    public boolean register(User user) {

        try (Connection con = DBConnection.getConnection()) {

            System.out.println("Connected to DB");

            String sql = "INSERT INTO users(full_name,email,phone,password,role) VALUES(?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getRole());

            int rows = ps.executeUpdate();

            System.out.println("Rows inserted = " + rows);

            return rows > 0;

        } catch (SQLException e) {
            System.out.println("SQL ERROR:");
            e.printStackTrace();
        }

        return false;
    }    		
    	
    public int getTotalUsers() {

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT COUNT(*) FROM users";

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
    
    public boolean emailExists(String email) {

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT user_id FROM users WHERE email = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }
    
    public boolean updatePassword(String email, String password) {

        try (Connection con = DBConnection.getConnection()) {

            String sql = "UPDATE users SET password=? WHERE email=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, password);
            ps.setString(2, email);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;
    }
}