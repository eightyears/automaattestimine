package api;

import org.json.JSONObject;

public interface WeatherForecastService {
	
	public JSONObject getWeatherForecast(String cityName);
	
    public JSONObject getThreeDayWeatherForecast(String cityName);
    
}
