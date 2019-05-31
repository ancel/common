package com.work.common.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

public class DBTest {
	@Test
	public void testDB(){
		String url = "jdbc:mysql://w03.test.yulore.com/yule?useUnicode=true&amp;characterEncoding=utf-8";
		String user = "bigdata";
		String password = "bigdb@2015qwe";
		try {
			Connection conn = DriverManager.getConnection(url, user, password);
			String sql = "SELECT * FROM dict_country";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(DriverManager.class.getClassLoader());
		System.out.println(DBTest.class.getClassLoader());
		
	}
}
