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
			// 加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");
			// URL指向要访问的数据库名test
			String url = "jdbc:mysql://127.0.0.1:3306/test";

			// MySQL配置时的用户名
			String user = "root";
			// MySQL配置时的密码
			String password = "root";
			// 连续数据库
			Connection connection = DriverManager.getConnection(url, user, password);
			if(!connection.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			// statement用来执行SQL语句
			Statement statement = connection.createStatement();
			// 要执行的SQL语句
			String sql = "select * from urls";
//String sql2 = "insert into urls(url) values('"+new LinkEvent(new Crawler(),1,new Link("http://baidu.com")).toString()+"')";
//String sql2 = "insert into urls(url) values('http:www.sina.com')";
			// 执行SQL语句并返回结果集
			ResultSet rs = statement.executeQuery(sql);
//int rows = statement.executeUpdate(sql2);
//System.out.println(rows);	
			String mval= new Link("http://alnur.com.cn").toDescription();
			AddToDatabase.addMysql(mval,connection);

			String result = null;
			while(rs.next()) {
				

				result = rs.getString("url");
				// 输出结果

		System.out.println(result);	
			}
			// 关闭结果集
			rs.close();
			// 关闭连接
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
