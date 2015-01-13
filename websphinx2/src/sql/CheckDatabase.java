package sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class CheckDatabase {
	public static int check(String s,Connection conn){
		int a=0;
		//数据库中查询网站
		String sql = "select * from urls";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pstmt.setString(1, s);
			
			ResultSet rs = pstmt.executeQuery();
			//统计该网站在数据库中的数量，实质是检查该网站是否已经出现在数据库中，即a的值最多为1
			while(rs.next()) {
				a++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

}
