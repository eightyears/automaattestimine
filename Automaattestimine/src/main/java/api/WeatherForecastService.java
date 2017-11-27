package api;

import java.io.IOException;

import org.json.JSONObject;

public interface WeatherForecastService {
	
	public JSONObject getWeatherForecast(String cityName) throws IOException;
	
    public JSONObject getThreeDayWeatherForecast(String cityName) throws IOException;
    
}
