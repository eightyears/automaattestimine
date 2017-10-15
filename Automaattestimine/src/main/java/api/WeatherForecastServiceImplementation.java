package api;

import org.json.JSONObject;

public class WeatherForecastServiceImplementation implements WeatherForecastService {
	
	private static final String CURRENT_WEATHER_SERVICE_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=%s";
    private static final String FIVE_DAYS_WEATHER_SERVICE_URL = "http://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s&units=metric";
    private static final String API_KEY = "851fac72358f38f2ec2656f734ae4982";

	@Override
	public JSONObject getWeatherForecast(String cityName) {
		String url = String.format(CURRENT_WEATHER_SERVICE_URL, cityName, API_KEY);
        //System.out.println(url);
        return getResponseFromService(url);
	}

	@Override
	public JSONObject getThreeDayWeatherForecast(String cityName) {
		String url = String.format(FIVE_DAYS_WEATHER_SERVICE_URL, cityName, API_KEY);
        //System.out.println(url);
        return getResponseFromService(url);
	}

	private JSONObject getResponseFromService(String url) {
		
		try {
            return JsonDataReader.readJsonFromUrl(url);
        } catch (Exception e) {
            return new JSONObject();
        }
	}
	
}
