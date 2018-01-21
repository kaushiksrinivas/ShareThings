package com.sharethings.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet; 

public class UserDAO {
	
	private JdbcTemplate template;

	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}
	
	public HashMap<String,String> validate(User u){
		//Result HashMap <status(false/true),error/success message> return collection.
		HashMap<String,String> result = new HashMap<String,String>();
		
		//UserName Length Needs to be atleast four chars.
		if(!(u.UserName.length()>=4)){
			result.put("flag", "false");
			result.put("message", "UserName Cannot be less than four characters!");
			return result;
		}
		//Mobile number length should be exactly ten chars.
		if(!(u.Mobile.length()== 10)){
			result.put("flag", "false");
			result.put("message", "Mobile number is invalid!");
			return result;
		}
		//Password length validation for security purposes.
		if(!(u.Password.length()>=4)){
			result.put("flag", "false");
			result.put("message", "Password cannot be less than four characters!");
			return result;
		}
		
		result.put("flag", "true");
		result.put("message", "Validation Done");
		return result;
	}
	
	public HashMap<String,String> UserExistsCheck(User u){
		//create the hashmap which holds the result.
		HashMap<String,String> result = new HashMap<String,String>();
		// veriification from the users table.
		String db_name = "users";
		
		//perform user name uniqueness validation on users table.
		String query = "SELECT COUNT(*) FROM "+db_name+" WHERE user_name='"+u.getUserName()+"'";
		int count = this.template.queryForInt(query);
				
		//If an entry found in the database for user name, return with error message.
		if(count > 0){
			result.put("flag", "false");
			result.put("message", "user name already registered, please choose a different user name!");
			return result;
		}
		
		db_name = "users";
		
		//perform user name uniqueness validation on vendors table.
		query = "SELECT COUNT(*) FROM "+db_name+" WHERE user_name='"+u.getUserName()+"'";
		count = this.template.queryForInt(query);
				
		//If an entry found in the database for user name, return with error message.
		if(count > 0){
			result.put("flag", "false");
			result.put("message", "user name already registered, please choose a different user name!");
			return result;
		}
		
		db_name = "users";
		//check for the unique mobile number validation from the database on users table.
	    query = "SELECT COUNT(*) FROM "+db_name+" WHERE phone="+u.getMobile();
		count = this.template.queryForInt(query);
		
		//If an entry found in the database for mobile number, return with error message.
		if(count>0){
			result.put("flag", "false");
			result.put("message", "mobile number already registered, Please login!");
			return result;
		}
		
		
		db_name = "users";
		//Validation in vendors table for mobile no.
		db_name = "users";
		
		query = "SELECT COUNT(*) FROM "+db_name+" WHERE phone="+u.getMobile();
		count = this.template.queryForInt(query);
		
		//If an entry found in the database for mobile number, return with error message.
		if(count>0){
			result.put("flag", "false");
			result.put("message", "mobile number already registered, Please login!");
			return result;
		}
		
		
		
		result.put("flag", "true");
		result.put("message", "No user exists!");
		return result;
		
	}
	
	public HashMap<String,String> AddUser(User u){
		//step1 : validate the user for the fields correctness.
		//variables to check the success of each step.
		String flag = null;
		String message = null;
		String query = null;
		
		HashMap<String,String> result = this.validate(u);
		for(Map.Entry map : result.entrySet()){
				if(map.getKey().equals("flag")){
					flag = (String)map.getValue();
				}
				else if(map.getKey().equals("message")){
					message = (String)map.getValue();
				}
		}
		//de reference the HashMap object for future usage.
		result.clear();
		if(flag.equals("false")){
			result.put("flag", "false");
			result.put("message", message);
			return result;
		}
		
		//step2 : check if the user with same username/phone number already exists.
		
		result = this.UserExistsCheck(u);
		for(Map.Entry map : result.entrySet()){
			if(map.getKey().equals("flag")){
				flag = (String) map.getValue();
			}
			else if(map.getKey().equals("message")){
				message = (String) map.getValue();
			}
		}
		result.clear();
		if(flag.equals("false")){
			result.put("flag", "false");
			result.put("message", message);
			return result;
		}
		
		//step3 : add the user and update the data base.
		// select the table as per the user type.
		String table_name = null;
		if(u.UserType.equals("Consumer")){
			table_name = "users";
		}
		else{
			table_name = "users";
		}
		
		query = "insert into "+table_name+" (user_name,phone,user_type,password,order_count) values('"+u.UserName+"','"+u.Mobile+"','"+u.UserType+"','"+u.Password+"',"+u.OrderCount+")";
		System.out.println(query);
		int status = this.template.update(query);
		
		if(status > 0){
			result.put("flag", "true");
			result.put("message", "Registered Successfully!");
			return result;
		}
		else{
			result.put("flag", "false");
			result.put("message", "Registration Failed! Please try again");
			return result;
		}
	}
	
	public HashMap<String,String> AuthenticateUser(User u){
		// select the table based on the user type being authenticated.
		String table_name = null;
		if(u.UserType.equals("Consumer")){
			table_name = "users";
		}
		else{
			table_name = "users";
		}
		
		String query = "SELECT COUNT(*) FROM "+table_name+" WHERE phone = "+u.getMobile();
		String password = null;
		HashMap<String,String> result = new HashMap<String,String>();
		int count = this.template.queryForInt(query);
	
		if(count<=0){
			result.put("flag", "false");
			result.put("message", "User Does Not Exist !, Sign Up First Please");
			return result;
		}
		
		query = "SELECT password FROM "+table_name+" WHERE phone="+u.getMobile();
				
		SqlRowSet rs = this.template.queryForRowSet(query);
		while (rs.next()){
			password = rs.getString("password");
		}
		
		if(u.Password.equals(password)){
			result.put("flag", "true");
			result.put("message", "Login Success!");
			return result;
		}
		else{
			result.put("flag", "false");
			result.put("message", "Invalid Password!");
			return result;
		}
		
	}
	
	public User GenerateProfile(User u){
		//first test if the user is consumer or vendor and change the table name accordingly.
		String table_name = "users";
		String query = "SELECT COUNT(*) FROM "+table_name+" WHERE phone = "+u.getMobile();
		int resultCount = this.template.queryForInt(query);
		
		if(!(resultCount >0)){
			table_name = "users";
			u.setUserType("ShopKeeper");
		}else{
			u.setUserType("Consumer");
		}
		
		query = "SELECT * FROM "+table_name+" WHERE phone = "+u.getMobile();
		SqlRowSet rs = this.template.queryForRowSet(query);
		
		while(rs.next()){
			u.setUserName(rs.getString("user_name"));
			u.setOrderCount(rs.getInt("order_count"));
		}
		
		return u;
		
	}
	
	public HashMap<String,String> ChangeUserName(User u){
		
		HashMap<String,String> result = new HashMap<String,String>();
		String table_name = "users";
		String query = "SELECT COUNT(*) FROM "+table_name+" WHERE user_name = '"+u.getUserName()+"'";
		int resultCount = this.template.queryForInt(query);
		
		if(resultCount > 0){
			result.put("flag", "false");
			result.put("message", "Username already in use! Try a different one.");
			return result;
		}
		
		//Now if no user with same user name exists, check for vendors with same user name. User name is unique across users and vendors.
		table_name = "users";
		query = "SELECT COUNT(*) FROM "+table_name+" WHERE user_name = '"+u.getUserName()+"'";
		resultCount = this.template.queryForInt(query);
		
		if(resultCount > 0){
			result.put("flag", "false");
			result.put("message", "Username already in use! Try a different one.");
			return result;
		}
		
		// If user name is unique across users and vendors. Go ahead and update the database with new user name.
		if(u.getUserType().equals("Consumer")){
			table_name = "users";
		}else{
			table_name = "users";
		}
		query = "UPDATE "+table_name+" SET user_name = '"+u.getUserName()+"' WHERE phone = '"+u.getMobile()+"'";
		resultCount = this.template.update(query);
		if(resultCount>0){
			result.put("flag", "true");
			result.put("message", "Username changed successfully");
			return result;
		}else{
			result.put("flag", "false");
			result.put("message", "Username change failed! try again.");
			return result;
		}
	}
	
	public HashMap<String,String> ChangeMobile(User u,String OldphoneNumber){
		
		HashMap<String,String> result = new HashMap<String,String>();
		String table_name = "users";
		String query = "SELECT COUNT(*) FROM "+table_name+" WHERE phone = '"+u.getMobile()+"'";
		int resultCount = this.template.queryForInt(query);
		
		if(resultCount > 0){
			result.put("flag", "false");
			result.put("message", "phone Number already in use! Try a different one.");
			return result;
		}
		
		//Now if no user with same user name exists, check for vendors with same user name. User name is unique across users and vendors.
		table_name = "users";
		query = "SELECT COUNT(*) FROM "+table_name+" WHERE phone = '"+u.getUserName()+"'";
		resultCount = this.template.queryForInt(query);
		
		if(resultCount > 0){
			result.put("flag", "false");
			result.put("message", "phone Number already in use! Try a different one.");
			return result;
		}
		
		// If user name is unique across users and vendors. Go ahead and update the database with new user name.
		if(u.getUserType().equals("Consumer")){
			table_name = "users";
		}else{
			table_name = "users";
		}
		query = "UPDATE "+table_name+" SET phone = '"+u.getMobile()+"' WHERE phone = '"+OldphoneNumber+"'";
		resultCount = this.template.update(query);
		if(resultCount>0){
			result.put("flag", "true");
			result.put("message", "phone Number changed successfully");
			return result;
		}else{
			result.put("flag", "false");
			result.put("message", "phone Number Change Failed! try again.");
			return result;
		}
	}
	
	public boolean ChangePassword(User u){
		String table_name = "";
		if(u.getUserType().equals("Consumer")){
			table_name = "users";
		}else{
			table_name = "users";
		}
		
		String query = "UPDATE "+table_name+" SET password = '"+u.getPassword()+"' WHERE phone = '"+u.getMobile()+"'";
		int resultCount = this.template.update(query);
		
		if(resultCount > 0){
			return true;
		}else{
			return false;
		}
	}

}
