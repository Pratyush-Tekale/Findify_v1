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
    		
    	

  

}
