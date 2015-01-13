package sql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import websphinx.*;

public class JDBCTest {
	public static void main(String[] args){
		try {
			// ������������
			Class.forName("com.mysql.jdbc.Driver");
			// URLָ��Ҫ���ʵ����ݿ���test
			String url = "jdbc:mysql://127.0.0.1:3306/test";

			// MySQL����ʱ���û���
			String user = "root";
			// MySQL����ʱ������
			String password = "root";
			// �������ݿ�
			Connection connection = DriverManager.getConnection(url, user, password);
			if(!connection.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			// statement����ִ��SQL���
			Statement statement = connection.createStatement();
			// Ҫִ�е�SQL���
			String sql = "select * from urls";
//String sql2 = "insert into urls(url) values('"+new LinkEvent(new Crawler(),1,new Link("http://baidu.com")).toString()+"')";
//String sql2 = "insert into urls(url) values('http:www.sina.com')";
			// ִ��SQL��䲢���ؽ����
			ResultSet rs = statement.executeQuery(sql);
//int rows = statement.executeUpdate(sql2);
//System.out.println(rows);	
			String mval= new Link("http://alnur.com.cn").toDescription();
			AddToDatabase.addMysql(mval,connection);

			String result = null;
			while(rs.next()) {
				

				result = rs.getString("url");
				// ������

		System.out.println(result);	
			}
			// �رս����
			rs.close();
			// �ر�����
			connection.close();
		} catch(ClassNotFoundException e) {
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
