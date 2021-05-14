package CartItemModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Cart {
	//A common method to connect to the DB
		private Connection connect() 
		{ 
			Connection con = null; 
			try
			{ 
				Class.forName("com.mysql.jdbc.Driver"); 
		 
				//database server name, username and password
				con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/paf", "root", ""); 
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			} 
			return con; 
		} 
		
		//Add function
		public String addCartItem(String item, String category, String quantity) 
		{ 
			String output = ""; 
			try
			{ 
				Connection con = connect(); 
				if (con == null) 
				{
					return "Error while connecting to the database for inserting.";
				} 
				// create a prepared statement
				String query = " insert into cart(`cartID`,`item`,`category`,`quantity`)"+" values (?, ?, ?, ?)"; 
				PreparedStatement preparedStmt = con.prepareStatement(query); 
				// binding values
				preparedStmt.setInt(1, 0); 
				preparedStmt.setString(2, item); 
				preparedStmt.setString(3, category); 
				preparedStmt.setString(4, quantity); 
				// execute the statement
				preparedStmt.execute(); 
				String newCart = getCartItem(); 
				 output = "{\"status\":\"success\", \"data\": \"" + newCart + "\"}"; 
			} 
			catch (Exception e) 
			{ 
				output = "{\"status\":\"error\", \"data\": \"Error!  adding Items.\"}"; 
				System.err.println(e.getMessage());  
			} 
		 return output; 
		 }
		
	//retrieve function
	public String getCartItem() { 
		String output = ""; 
		try
		{ 
			Connection con = connect(); 
			if (con == null) 
			{
				return "Error while connecting to the database for reading.";
			} 
			// Prepare the html table to be displayed
			output = "<table border='1'><tr><th>item</th>" 
					+"<th>category</th><th>quantity</th>"
					+ "<th>Update</th><th>Remove</th></tr>";
 
	 
			String query = "select * from cart"; 
			Statement stmt = con.createStatement(); 
			ResultSet rs = stmt.executeQuery(query); 
			// iterate through the rows in the result set
			while (rs.next()) 
			{ 
				String cartID = Integer.toString(rs.getInt("cartID")); 
				String item = rs.getString("item"); 
				String category = rs.getString("category"); 
				String quantity = rs.getString("quantity");  
				
				// Add into the html table
				output += "<tr><td><input id='hidItemIDUpdate' name='hidItemIDUpdate' type='hidden' value='" + cartID
						 + "'>" + item + "</td>";
				 output += "<td>" + category + "</td>"; 
				 output += "<td>" + quantity + "</td>";  
			
				// buttons
				 output += "<td><input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-secondary'></td>"+ "<td><input name='btnRemove' type='button' value='Remove' class='btnRemove btn btn-danger' data-cartid='"
						 + cartID + "'>" + "</td></tr>";
			
			} 
			con.close(); 
			// Complete the html table
			output += "</table>"; 
		} 
		catch (Exception e) // check errors
		{ 
			output = "Error while reading the items."; 
			System.err.println(e.getMessage()); 
		} 
		return output; 
	 	}

	//Update function
	public String updateCartItems(String cartID, String item, String category, String quantity){ 
		 String output = ""; 
		 try
		 { 
			 Connection con = connect(); 
			 if (con == null) 
			 {
				 return "Error while connecting to the database for updating.";
			 } 
			 // create a prepared statement
			 String query = "UPDATE cart SET item=?,category=?,quantity=? WHERE cartID=?"; 
			 PreparedStatement preparedStmt = con.prepareStatement(query); 
			 // binding values
			 preparedStmt.setString(1, item); 
			 preparedStmt.setString(2, category); 
			 preparedStmt.setInt(3, Integer.parseInt(quantity)); 
			 preparedStmt.setInt(4, Integer.parseInt(cartID)); 
			 // execute the statement
			 preparedStmt.execute(); 
			 con.close(); 
			 String newCart = getCartItem(); 
		 	 output = "{\"status\":\"success\", \"data\": \"" + 
		 			newCart + "\"}"; 
		 } 
		 catch (Exception e) 
		 { 
			 output = "{\"status\":\"error\", \"data\": \"Error!  updating details.\"}";  
				System.err.println(e.getMessage()); 
		 } 
		 	return output; 
		 }

	//Delete function
	public String deleteCartItems(String cartID) { 
		String output = ""; 
		try
		{ 
			Connection con = connect(); 
			if (con == null) 
			{
				return "Error while connecting to the database for deleting.";
			} 
			// create a prepared statement
			String query = "delete from cart where cartID=?"; 
			PreparedStatement preparedStmt = con.prepareStatement(query); 
			// binding values
			preparedStmt.setInt(1, Integer.parseInt(cartID)); 
			// execute the statement
			preparedStmt.execute(); 
			con.close(); 
			String newCart = getCartItem(); 
			output = "{\"status\":\"success\", \"data\": \"" + newCart + "\"}";  
		} 
		catch (Exception e) 
		{ 
			output = "{\"status\":\"error\", \"data\": \"Error! While deleting Cart Items.\"}";  
			System.err.println(e.getMessage()); 
		} 
		return output; 
		} 
}