package server;

import com.mysql.jdbc.Driver;
import java.sql.*;


// mySQL driver class
// Steven

public class MySQLDriver {
	private Connection con;
	private final static String selectName = "SELECT * FROM USERINFO WHERE USERNAME = ?";
	private final static String addUser = "INSERT INTO USERINFO(USERNAME,PASSWORD,COLOR,HISTORYHIGH,TOTALSCORE) VALUES(?,?,?,?,?)";
	private final static String updateUser = "UPDATE USERINFO SET ? = ? WHERE USERNAME = ?";
	
	
	// constructor
	public MySQLDriver(){
		try{
			new Driver();
		} catch (SQLException sqle){
			sqle.printStackTrace();
		}
	}
	
	// method to connect
	public void connect(){
		try{
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/snakeDatabase?user=root&password=root&useSSL=false");
		} catch (SQLException sqle){
			sqle.printStackTrace();
		}
	}
	
	// method to stop
	public void stop(){
		try{
			con.close();
		} catch (SQLException sqle){
			sqle.printStackTrace();
		}
	}
	
	
	// method to check if a given username exists
	public boolean userExist(String inusername){
		try{
			PreparedStatement ps = con.prepareStatement(selectName);
			ps.setString(1, inusername);
			ResultSet result = ps.executeQuery();
			while (result.next())
				return true;
		} catch (SQLException sqle){
			sqle.printStackTrace();
		}
		return false;
	}
	
	
	
	// method to add a new user to the database
	public void add(String inusername,String inpassword,String incolor){
		try{
			PreparedStatement ps = con.prepareStatement(addUser);
			ps.setString(1, inusername);
			ps.setString(2, inpassword);
			ps.setString(3,incolor);
			ps.setInt(4,0);
			ps.setInt(5,0);
			ps.executeUpdate();
			
		} catch (SQLException sqle){
			sqle.printStackTrace();
		}
	}
	
	// method to check if the given credentials are valid
	public boolean validLogin(String inusername,String inpassword){
		if (getPassword(inusername).trim().equals(inpassword.trim()))
			return true;
		else return false;
	}
	
	
	// method to get the password for a given username
	public String getPassword(String inusername){
		try{
			PreparedStatement ps = con.prepareStatement(selectName);
			ps.setString(1, inusername);
			ResultSet result = ps.executeQuery();
			while (result.next())
				return result.getString(2);
		} catch (SQLException sqle){
			sqle.printStackTrace();
		}
		return "";
	}
	
	// method to get the snake color for a given username
	public String getColor(String inusername){
		try{
			PreparedStatement ps = con.prepareStatement(selectName);
			ps.setString(1, inusername);
			ResultSet result = ps.executeQuery();
			while (result.next())
				return result.getString(3);
		} catch (SQLException sqle){
			sqle.printStackTrace();
		}
		return "";
	}
	
	// method to get the history highscore for a given username
	public int getHistoryHigh(String inusername){
		try{
			PreparedStatement ps = con.prepareStatement(selectName);
			ps.setString(1, inusername);
			ResultSet result = ps.executeQuery();
			while (result.next())
				return result.getInt(4);
		} catch (SQLException sqle){
			sqle.printStackTrace();
		}
		return -1;
	}
	
	
	// method to get the total score for a given username
	public int getTotalScore(String inusername){
		try{
			PreparedStatement ps = con.prepareStatement(selectName);
			ps.setString(1, inusername);
			ResultSet result = ps.executeQuery();
			while (result.next())
				return result.getInt(5);
		} catch (SQLException sqle){
			sqle.printStackTrace();
		}
		return -1;
	}
	
	// method to set the color for a given username
	public void setColor(String inusername, String incolor){
		try{
			PreparedStatement ps = con.prepareStatement(updateUser);
			ps.setString(1,"COLOR");
			ps.setString(2, incolor);
			ps.setString(3, inusername);
			ps.executeUpdate();
		} catch (SQLException sqle){
			sqle.printStackTrace();
		}
	}
	
	// method to update the history high score for a given username
	// if the input is higher than the recorded high score, update the database
	// if not, do nothing
	public void updateHistoryHigh(String inusername, int inscore){
		try{
			if (inscore > getHistoryHigh(inusername)){
				PreparedStatement ps = con.prepareStatement(updateUser);
				ps.setString(1, "HISTORYHIGH");
				ps.setInt(2, inscore);
				ps.setString(3, inusername);
				ps.executeUpdate();
			}
		} catch (SQLException sqle){
			sqle.printStackTrace();
		}
	}
	
	// method to add a given score to the given username's total score
	public void updateTotalScore(String inusername, int inscore){
		try{
			PreparedStatement ps = con.prepareStatement(updateUser);
			ps.setString(1, "TOTALSCORE");
			ps.setInt(2, inscore+getTotalScore(inusername));
			ps.setString(3, inusername);
			ps.executeUpdate();
		} catch (SQLException sqle){
			sqle.printStackTrace();
		}
	}
}
