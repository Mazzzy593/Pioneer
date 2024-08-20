package com.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

//��װ��JDBC���ݿ�������
public class JDBCUtils {
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	static {
		//��������ʽ��������getClassLoader�������
		InputStream is = JDBCUtils.class.getClassLoader().getResourceAsStream("db.properties");
		//����property �Ķ���
		Properties p = new Properties();
		//�������ļ�
		try {
			p.load(is);
			driver = p.getProperty("driver");
			url = p.getProperty("url");
			username = p.getProperty("username");
			password = p.getProperty("password");
			//����MySql����
			Class.forName(driver);
			System.out.println("�������سɹ�");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace() ;
		}
	}
//������Ӷ���

	public static Connection getConnection() {
		try {
			System.out.println("���ݿ����ӳɹ�");
			return DriverManager.getConnection(url, username, password);			
		}catch(SQLException e) {
			System.out.println("���ݿ�����ʧ��");
			e.printStackTrace();
		}
		return null;
		
	}
//�ͷ���Դ
	public static void close(Connection conn,Statement statement,ResultSet result) {
		try {
			if(result!=null) {
				result.close();
				result=null;
			}
			if(statement!=null) {
				statement.close();
				statement=null;
			}
			if(conn!=null) {
				conn.close();
				conn=null;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
		
}

