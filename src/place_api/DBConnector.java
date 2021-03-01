package place_api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.PreparedStatement;

public class DBConnector {
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/test2_db"; //port 3306
	static final String USERNAME = "test2_user";
	static final String PASSWORD = "test2";
	
	static Connection conn = null;
	static PreparedStatement stmt = null;
    static ResultSet rs = null;
	
	public static void connectDB() {
		try {
			Class.forName(JDBC_DRIVER);
			String url = DB_URL;
			conn = DriverManager.getConnection(url, USERNAME, PASSWORD);
			System.out.println("---Connection success---");
		}
		catch(ClassNotFoundException e){
			System.out.println("---Driver load failure---");
		}
		catch(SQLException e) {
			System.out.println("---Error : " + e + "---"); //
		}
		
		//return conn;
	}
	
	public static void insert(Place place) {
		try{
        	//conn = getConnection();
            
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO test_tb");
            //sb.append(" ('title', 'addr1', 'contentid', 'contenttypeid', 'firstimage', 'firstimage2')");
            sb.append(" VALUES(");
            sb.append("'" + place.title + "'" + ",");
            sb.append("'" + place.addr1 + "'" + ",");
            sb.append("'" + place.contentid + "'" + ",");
            sb.append("'" + place.contenttypeid + "'" + ",");
            sb.append("'" + place.firstimage + "'" + ",");
            sb.append("'" + place.firstimage2 + "'" + ",");
            sb.append("'" + place.mapx + "'" + ",");
            sb.append("'" + place.mapy + "'" + ",");
            sb.append("'" + place.overview + "'" + ")");
            
            stmt = conn.prepareStatement(sb.toString());
            
            System.out.println(sb.toString());
            
            try {
            	stmt.executeUpdate();
            }
            catch(SQLException e) {
            	e.printStackTrace();
            }
        }
        catch(SQLException e){
            System.out.println("Error " + e);
        }
	}
	
	public static void insert(String areaCode, String areaName, String sigunguCode, String sigunguName, int listCount) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO test_tb(areaCode, areaName, sigunguCode, sigunguName, listCount)");
        
        sb.append(" VALUES(");
        sb.append("'" + Integer.parseInt(areaCode) + "'" + ",");
        sb.append("'" + areaName + "'" + ",");
        sb.append("'" + Integer.parseInt(sigunguCode) + "'" + ",");
        sb.append("'" + sigunguName + "'" + ",");
        sb.append("'" + listCount + "'" + ")");
        
        try {
			stmt = conn.prepareStatement(sb.toString());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
        
        System.out.println(sb.toString());
        
        try {
        	stmt.executeUpdate();
        }
        catch(SQLException e) {
        	e.printStackTrace();
        }
        
	}
	//public static select
	
	public static void disconnectDB() {
		try {
			if(conn != null && !conn.isClosed()) {
				conn.close();
				conn = null;
			}
		}
		catch(SQLException e) { 
			e.printStackTrace();
		}
	}
	
}
