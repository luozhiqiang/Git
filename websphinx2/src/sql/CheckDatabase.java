package sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class CheckDatabase {
	public static int check(String s,Connection conn){
		int a=0;
		//���ݿ��в�ѯ��վ
		String sql = "select * from urls";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pstmt.setString(1, s);
			
			ResultSet rs = pstmt.executeQuery();
			//ͳ�Ƹ���վ�����ݿ��е�������ʵ���Ǽ�����վ�Ƿ��Ѿ����������ݿ��У���a��ֵ���Ϊ1
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
