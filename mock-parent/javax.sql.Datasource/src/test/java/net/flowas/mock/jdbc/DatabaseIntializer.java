package net.flowas.mock.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseIntializer {

	public static void init(InputStream[] ins) {
		Connection conn;
		try {			
			conn = getConnection();
			for(int i=0;i<ins.length ;i++){
				byte[] b=new byte[ins[i].available()];
				ins[i].read(b);
				String text=new String(b);
				Statement stmt = conn.createStatement();
				stmt.addBatch(text);
				stmt.executeBatch();
				ins[i].close();
			}			
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	public static void clean() {
		Connection conn;
		try {			
			conn = getConnection();			
				String text="drop all objects";
				Statement stmt = conn.createStatement();
				stmt.addBatch(text);
				stmt.executeBatch();
						
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection() throws ClassNotFoundException, SQLException{		
			Class.forName("org.h2.Driver");
			Connection conn = DriverManager.getConnection("jdbc:h2:db/test",
					"sa", "");
			return conn;
	}
}
