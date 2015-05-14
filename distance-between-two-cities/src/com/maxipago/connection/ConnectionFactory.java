package com.maxipago.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Driver;

public class ConnectionFactory {

	private String url = "jdbc:mysql://localhost/test_maxipago";
	private String user = "root";
	private String password = "admin";
	
	public static void main(String[] args) {
		ConnectionFactory c = new ConnectionFactory();
		c.getConnection();
	}

	@SuppressWarnings("static-access")
	public Connection getConnection() {
		Connection connection = null;
		try {
			Driver.class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Erro to load database:");
			e.printStackTrace();
		}
		return connection;
	}

}
