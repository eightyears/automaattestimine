package tests;

import static org.junit.Assert.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import api.WeatherForecastService;
import api.WeatherForecastServiceImplementation;

public class WeatherForecastTests {
	
	private static final String CITY_NAME = "Tallinn";		
	
	@Test
    public void isReplyValid() throws JSONException {
		WeatherForecastService wfs = new WeatherForecastServiceImplementation();
        JSONObject reply = wfs.getWeatherForecast(CITY_NAME);
        assertTrue(reply.get("cod").toString().equals("200"));
    }
	
	@Test
    public void isCityReplyCorrect() throws JSONException {
        WeatherForecastService wfs = new WeatherForecastServiceImplementation();
        JSONObject reply = wfs.getWeatherForecast(CITY_NAME);
        assertEquals(CITY_NAME, (String) reply.get("name"));
    }
	
	@Test
    public void isCurrentTemperatureReal() throws JSONException {
		WeatherForecastService wfs = new WeatherForecastServiceImplementation();
        JSONObject reply = wfs.getWeatherForecast(CITY_NAME);
        Integer currentTemperature = (Integer) ((JSONObject) reply.get("main")).get("temp");
        assertTrue(currentTemperature > -90 && currentTemperature < 60);
    }
	
	//this one needs correction
	@Test
    public void isThreeDayWeatherForecastTemperatureReal() throws JSONException {
		WeatherForecastService wfs = new WeatherForecastServiceImplementation();
        boolean complexBoolean =  true;
        JSONObject reply = wfs.getThreeDayWeatherForecast(CITY_NAME);
        JSONArray nextFiveDays = (JSONArray) reply.get("list");
        System.out.println(nextFiveDays.length());
        for (int i = 0; i < nextFiveDays.length(); ++i) {
            JSONObject info = nextFiveDays.getJSONObject(i);
            Double hourTemperature = (Double) (((JSONObject) ((JSONObject) info).get("main"))).get("temp");
            System.out.println(hourTemperature);
            if(hourTemperature < -90 || hourTemperature > 60) {
                complexBoolean = false;
                break;
            }
        }
        assertTrue(complexBoolean);
    }
	
	@Test
    public void isCoordinateFormatSuitable() throws JSONException {
		WeatherForecastService wfs = new WeatherForecastServiceImplementation();
		JSONObject reply = wfs.getWeatherForecast(CITY_NAME);        
        assertTrue(((JSONObject) reply.get("coord")).get("lon") instanceof Double && ((JSONObject) reply.get("coord")).get("lat") instanceof Double);
    }
	
	//soon here will come some new tests

}
