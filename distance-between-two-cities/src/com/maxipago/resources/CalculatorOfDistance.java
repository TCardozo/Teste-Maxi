package com.maxipago.resources;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONException;
import org.json.JSONObject;

import com.maxipago.dao.CityDao;
import com.maxipago.model.City;
import com.maxipago.model.CityDistance;
import com.thoughtworks.xstream.XStream;

@Path("/calculatorOfDistance")
public class CalculatorOfDistance {

	private CityDao cityDao = new CityDao();

	@Path("/{originCity}/{destinationCity}/{resultType}/{units}")
	@GET
	@Produces("text/plain")
	public String getCities(@PathParam("originCity") String originCity,
			@PathParam("destinationCity") String destinationCity,
			@PathParam("resultType") String resultType,
			@PathParam("units") String units) throws Exception {

		City origin = cityDao.searchCity(originCity);
		City destination = cityDao.searchCity(destinationCity);

		if (origin != null && destination != null) {
			// Origin
			float originLatitude = origin.getLatitude();
			float originLongitude = origin.getLongitude();
			// Destination
			float destinationLatitude = destination.getLatitude();
			float destinationLongitude = destination.getLongitude();

			try {
				URL url = new URL(
						"http://maps.googleapis.com/maps/api/distancematrix/"
								+ resultType + "?origins=" + originLatitude
								+ "," + originLongitude + "&destinations="
								+ destinationLatitude + ","
								+ destinationLongitude
								+ "&language=pt-BR&sensor=false&units=" + units
								+ "");
				
				if (resultType.equals("json")) {
					return createJson(url, originCity, destinationCity);
				}

				else {
					return transformXml(url, originCity, destinationCity);
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return "City is invalid!";
		}

	}

	public String transformXml(URL url, String originCity,
			String destinationCity) throws IOException {
		XStream xstream = new XStream();
		xstream.alias("DistanceOfCities", CityDistance.class);
		CityDistance distance = null;
		String xml = null;
		try {
			Document document = getDocumento(url);
			xstream.aliasField("origin_city", CityDistance.class, "originCity");
			xstream.aliasField("destination_city", CityDistance.class,
					"destinationCity");
			distance = new CityDistance(originCity, destinationCity,
					fillDistance(document));
			xml = xstream.toXML(distance);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		return xml;
	}

	public Document getDocumento(URL url) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(url);
		return document;
	}

	@SuppressWarnings("rawtypes")
	public static String fillDistance(Document document) {
		List list = document
				.selectNodes("//DistanceMatrixResponse/row/element/distance/text");

		Element element = (Element) list.get(list.size() - 1);

		return element.getText();
	}

	private String createJson(URL url, String originCity, String destinationCity) {
		CityDistance cityDistance = new CityDistance(originCity,
				destinationCity, fillDistanceJson(url));
		JSONObject distanceJson = new JSONObject();
		try {
			distanceJson.put("origin_city", cityDistance.getOriginCity());
			distanceJson.put("destination_city",
					cityDistance.getDestinationCity());
			distanceJson.put("distance", cityDistance.getDistance());
			return distanceJson.toString();
		} catch (JSONException e1) {
			e1.printStackTrace();
			return null;
		}
	}

	public String fillDistanceJson(URL url) {
		Scanner scan = null;
		String str = null;
		try {
			scan = new Scanner(url.openStream());
			str = new String();
			while (scan.hasNext())
				str += scan.nextLine();
			scan.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject obj;
		try {
			obj = new JSONObject(str);
			org.json.JSONObject res = obj.getJSONArray("rows").getJSONObject(0);
			org.json.JSONObject res1 = res.getJSONArray("elements")
					.getJSONObject(0);
			org.json.JSONObject loc = res1.getJSONObject("distance");
			return loc.get("text").toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}

}
