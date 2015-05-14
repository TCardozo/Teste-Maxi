package com.maxipago.model;

public class CityDistance {

	private String originCity;
	private String destinationCity;
	private String distance;

	public CityDistance(String originCity, String destinationCity,
			String distance) {
		this.originCity = originCity;
		this.destinationCity = destinationCity;
		this.distance = distance;
	}

	public String getOriginCity() {
		return originCity;
	}

	public void setOriginCity(String originCity) {
		this.originCity = originCity;
	}

	public String getDestinationCity() {
		return destinationCity;
	}

	public void setDestinationCity(String destinationCity) {
		this.destinationCity = destinationCity;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "CityDistance [originCity=" + originCity + ", destinationCity="
				+ destinationCity + ", distance=" + distance + "]";
	}

}
