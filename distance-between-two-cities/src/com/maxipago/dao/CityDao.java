package com.maxipago.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.maxipago.connection.ConnectionFactory;
import com.maxipago.model.City;

public class CityDao {

	public List<City> getListCities() {
		Connection connection = new ConnectionFactory().getConnection();
		List<City> cities = new ArrayList<City>();
		City city = null;
		String sql = "SELECT * FROM City";
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				city = new City();
				city.setName(resultSet.getString("name"));
				city.setLatitude(resultSet.getFloat("latitude"));
				city.setLongitude(resultSet.getFloat("longitude"));
				cities.add(city);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cities;
	}

	public City searchCity(String name) {
		Connection connection = new ConnectionFactory().getConnection();
		City city = null;
		try {
			String sql = "SELECT * FROM City c WHERE c.name = ?";
			PreparedStatement preparedStatement = connection
					.prepareStatement(sql);
			preparedStatement.setString(1, name);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				city = new City();
				city.setLatitude(resultSet.getFloat("latitude"));
				city.setLongitude(resultSet.getFloat("longitude"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return city;
	}

}
