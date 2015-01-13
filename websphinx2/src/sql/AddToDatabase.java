package sql;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;

import websphinx.*;

//将已经获取到的网站具体信息加入到数据库中
public class AddToDatabase {
	public static void addMysql(String mval, Connection conn) {
		PreparedStatement pstmt = null;
		
		
		try {
			pstmt = conn.prepareStatement("insert into urls values(?)");
			pstmt.setString(1, mval);			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
