package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import api.CityInput;
import api.CityOutput;
import api.JsonDataReader;
import api.WeatherForecastService;
import api.WeatherForecastServiceImplementation;

public class WeatherForecastTests {
	
	private static String cityName = "";
	
	@BeforeClass
	public static void setUpCityName() throws IOException {
		CityInput ci = new CityInput();
		String city = ci.getCityName();
		cityName = city;
	}
	
	@AfterClass
	public static void write() throws IOException {
    	CityOutput output = new CityOutput();
    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	Date date = new Date();
    	//System.out.println(dateFormat.format(date));
    	output.writeOutputToFile(dateFormat.format(date) + "\r\n" + JsonDataReader.getTextFromUrl1() + "\r\n" + "-------------" + "\r\n");
    	output.writeOutputToFile(dateFormat.format(date) + "\r\n" + JsonDataReader.getTextFromUrl2() + "\r\n" + "-------------" + "\r\n");
    }
		
	@Test
    public void isCityReplyCorrect() throws JSONException, IOException {
        WeatherForecastService wfs = new WeatherForecastServiceImplementation();
        JSONObject reply = wfs.getWeatherForecast(cityName);
        assertEquals(cityName, (String) reply.get("name"));
    }
	
	@Test
    public void isReplyValid() throws JSONException, IOException {
		WeatherForecastService wfs = new WeatherForecastServiceImplementation();
        JSONObject reply = wfs.getWeatherForecast(cityName);
        assertTrue(reply.get("cod").toString().equals("200"));
    }
	
	@Test
    public void isCurrentTemperatureReal() throws JSONException, IOException {
		WeatherForecastService wfs = new WeatherForecastServiceImplementation();
        JSONObject reply = wfs.getWeatherForecast(cityName);
        if(((JSONObject) reply.get("main")).get("temp") instanceof Integer) {
        	double currentTemperature = (double) ((Integer)((JSONObject) reply.get("main")).get("temp")).intValue();
        	assertTrue(currentTemperature > -90 && currentTemperature < 60);
        }else {
        	Double currentTemperature = (Double)((JSONObject) reply.get("main")).get("temp");
        	assertTrue(currentTemperature > -90 && currentTemperature < 60);
        }       
    }
	
	//fixed
	@Test
    public void areAllThreeDayWeatherForecastTemperaturesReal() throws JSONException, IOException {
		WeatherForecastService wfs = new WeatherForecastServiceImplementation();
        boolean complexBoolean =  true;
        JSONObject reply = wfs.getThreeDayWeatherForecast(cityName);
        JSONArray nextFiveDays = (JSONArray) reply.get("list");
        //System.out.println(nextFiveDays.length());
        for (int i = 0; i < nextFiveDays.length(); ++i) {
            JSONObject info = nextFiveDays.getJSONObject(i);
            if ((((JSONObject) ((JSONObject) info).get("main"))).get("temp") instanceof Integer) {
            	double hourTemperature = (double) ((Integer)(((JSONObject) ((JSONObject) info).get("main"))).get("temp")).intValue();
            	//System.out.println(hourTemperature);
            	if(hourTemperature < -90 || hourTemperature > 60) {
                    complexBoolean = false;
                    break;
                }
            }else {
            	Double hourTemperature = (Double) (((JSONObject) ((JSONObject) info).get("main"))).get("temp");
               // System.out.println(hourTemperature);
                if(hourTemperature < -90 || hourTemperature > 60) {
                    complexBoolean = false;
                    break;
                }
            }                       
        }
        assertTrue(complexBoolean);
    }
	
	@Test
    public void isCoordinateFormatSuitable() throws JSONException, IOException {
		WeatherForecastService wfs = new WeatherForecastServiceImplementation();
		JSONObject reply = wfs.getWeatherForecast(cityName);        
        assertTrue(((JSONObject) reply.get("coord")).get("lon") instanceof Double && ((JSONObject) reply.get("coord")).get("lat") instanceof Double);
    }
	
	@Test
    public void isWeatherForecastTemperatureGivenPerEveryThreeHours() throws JSONException, IOException {
        WeatherForecastService wfs = new WeatherForecastServiceImplementation();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        boolean complexBoolean = true;
        JSONObject reply = wfs.getThreeDayWeatherForecast(cityName);
        JSONArray nextFiveDays = (JSONArray) reply.get("list");
        String previousTime = "";
        String currentTime = "";
        for (int i = 0; i < nextFiveDays.length(); ++i) {
            JSONObject info = nextFiveDays.getJSONObject(i);
            currentTime = (String) ((JSONObject) info).get("dt_txt");
            //System.out.println(currentTime);
            if (i != 0) {
                LocalDateTime  current = LocalDateTime .parse(currentTime, formatter);
                LocalDateTime  previous = LocalDateTime.parse(previousTime, formatter);
                long hoursPerStep = ChronoUnit.HOURS.between(previous, current);
                complexBoolean = hoursPerStep == 3;

                if (!complexBoolean) {
                    break;
                }
            } else {
                i = 0;
            }
            previousTime = currentTime;
        }
        
        assertTrue(complexBoolean);
    }
	
	@Test
    public void isEveryDayForecastHasMinMaxTemp() throws JSONException, IOException {
		WeatherForecastService wfs = new WeatherForecastServiceImplementation();
        boolean complexBoolean = true;
        JSONObject reply = wfs.getThreeDayWeatherForecast(cityName);
        JSONArray nextFiveDays = (JSONArray) reply.get("list");
        for (int i = 0; i < nextFiveDays.length(); ++i) {
            JSONObject info = nextFiveDays.getJSONObject(i);
            JSONObject nextThreeHoursForecast = (JSONObject) info;
            JSONObject nextThreeHoursForecastMain = (JSONObject) nextThreeHoursForecast.get("main");
            complexBoolean = nextThreeHoursForecastMain.has("temp_min") && nextThreeHoursForecastMain.has("temp_max");
            if(!complexBoolean) {
                break;
            }
        }
        assertTrue(complexBoolean);
    }
	
	@Test
    public void areAllMinMaxTemperaturesCorrect() throws JSONException, IOException {
		WeatherForecastService wfs = new WeatherForecastServiceImplementation();
        boolean complexBoolean = true;
        JSONObject reply = wfs.getThreeDayWeatherForecast(cityName);
        JSONArray nextFiveDays = (JSONArray) reply.get("list");
        for (int i = 0; i < nextFiveDays.length(); ++i) {
            JSONObject info = nextFiveDays.getJSONObject(i);
            JSONObject nextThreeHoursForecast = (JSONObject) info;
            if (((JSONObject) nextThreeHoursForecast.get("main")).get("temp_min") instanceof Integer) {
            	double minTemperature = (double) ((Integer)(((JSONObject) ((JSONObject) nextThreeHoursForecast).get("main"))).get("temp_min")).intValue();
            	if (((JSONObject) nextThreeHoursForecast.get("main")).get("temp_max") instanceof Double) {
            		Double maxTemperature = (Double) ((JSONObject) nextThreeHoursForecast.get("main")).get("temp_max");
            		//System.out.println("MIN: " + minTemperature + " MAX: " + maxTemperature);
            		if(minTemperature < -90 || minTemperature > 60 || maxTemperature < -90 || maxTemperature > 60) {
                        complexBoolean = false;
                        break;
                    }
            	}           	
            }else if (((JSONObject) nextThreeHoursForecast.get("main")).get("temp_max") instanceof Integer) {
            	double maxTemperature = (double) ((Integer)(((JSONObject) ((JSONObject) nextThreeHoursForecast).get("main"))).get("temp_max")).intValue();
            	if (((JSONObject) nextThreeHoursForecast.get("main")).get("temp_min") instanceof Double) {
            		Double minTemperature = (Double) ((JSONObject) nextThreeHoursForecast.get("main")).get("temp_min");
            		//System.out.println("MIN: " + minTemperature + " MAX: " + maxTemperature);
            		if(minTemperature < -90 || minTemperature > 60 || maxTemperature < -90 || maxTemperature > 60) {
                        complexBoolean = false;
                        break;
                    }
            	}     
            }else if (((JSONObject) nextThreeHoursForecast.get("main")).get("temp_min") instanceof Integer &&
            		((JSONObject) nextThreeHoursForecast.get("main")).get("temp_max") instanceof Integer) {
            	double minTemperature = (double) ((Integer)(((JSONObject) ((JSONObject) nextThreeHoursForecast).get("main"))).get("temp_min")).intValue();
            	double maxTemperature = (double) ((Integer)(((JSONObject) ((JSONObject) nextThreeHoursForecast).get("main"))).get("temp_max")).intValue();
            	//System.out.println("MIN: " + minTemperature + " MAX: " + maxTemperature);
            	if(minTemperature < -90 || minTemperature > 60 || maxTemperature < -90 || maxTemperature > 60) {
            		complexBoolean = false;
                    break;
            	}
            }else {
            	Double minTemperature = (Double) ((JSONObject) nextThreeHoursForecast.get("main")).get("temp_min");
                Double maxTemperature = (Double) ((JSONObject) nextThreeHoursForecast.get("main")).get("temp_max");
                //System.out.println("MIN: " + minTemperature + " MAX: " + maxTemperature);
                if(minTemperature < -90 || minTemperature > 60 || maxTemperature < -90 || maxTemperature > 60) {
                    complexBoolean = false;
                    break;
                }
            }            
        }
        assertTrue(complexBoolean);
    }
	
	@Test
    public void areAllMinTemperaturesLessThanMaxTemperatures() throws JSONException, IOException {
		WeatherForecastService wfs = new WeatherForecastServiceImplementation();
        boolean complexBoolean = true;
        JSONObject reply = wfs.getThreeDayWeatherForecast(cityName);
        JSONArray nextFiveDays = (JSONArray) reply.get("list");
        for (int i = 0; i < nextFiveDays.length(); ++i) {
            JSONObject info = nextFiveDays.getJSONObject(i);
            JSONObject nextThreeHoursForecast = (JSONObject) info;
            if (((JSONObject) nextThreeHoursForecast.get("main")).get("temp_min") instanceof Integer) {
            	double minTemperature = (double) ((Integer)(((JSONObject) ((JSONObject) nextThreeHoursForecast).get("main"))).get("temp_min")).intValue();
            	if (((JSONObject) nextThreeHoursForecast.get("main")).get("temp_max") instanceof Double) {
            		Double maxTemperature = (Double) ((JSONObject) nextThreeHoursForecast.get("main")).get("temp_max");
            		//System.out.println("MIN: " + minTemperature + " MAX: " + maxTemperature);
            		if(minTemperature > maxTemperature) {
                        complexBoolean = false;
                        break;
                    }
            	}           	
            }else if (((JSONObject) nextThreeHoursForecast.get("main")).get("temp_max") instanceof Integer) {
            	double maxTemperature = (double) ((Integer)(((JSONObject) ((JSONObject) nextThreeHoursForecast).get("main"))).get("temp_max")).intValue();
            	if (((JSONObject) nextThreeHoursForecast.get("main")).get("temp_min") instanceof Double) {
            		Double minTemperature = (Double) ((JSONObject) nextThreeHoursForecast.get("main")).get("temp_min");
            		//System.out.println("MIN: " + minTemperature + " MAX: " + maxTemperature);
            		if(minTemperature > maxTemperature) {
                        complexBoolean = false;
                        break;
                    }
            	}     
            }else if (((JSONObject) nextThreeHoursForecast.get("main")).get("temp_min") instanceof Integer &&
            		((JSONObject) nextThreeHoursForecast.get("main")).get("temp_max") instanceof Integer) {
            	double minTemperature = (double) ((Integer)(((JSONObject) ((JSONObject) nextThreeHoursForecast).get("main"))).get("temp_min")).intValue();
            	double maxTemperature = (double) ((Integer)(((JSONObject) ((JSONObject) nextThreeHoursForecast).get("main"))).get("temp_max")).intValue();
            	//System.out.println("MIN: " + minTemperature + " MAX: " + maxTemperature);
            	if(minTemperature > maxTemperature) {
            		complexBoolean = false;
                    break;
            	}
            }else {
            	Double minTemperature = (Double) ((JSONObject) nextThreeHoursForecast.get("main")).get("temp_min");
                Double maxTemperature = (Double) ((JSONObject) nextThreeHoursForecast.get("main")).get("temp_max");
                System.out.println("MIN: " + minTemperature + " MAX: " + maxTemperature);
                if(minTemperature > maxTemperature) {
                    complexBoolean = false;
                    break;
                }
            }            
        }
        assertTrue(complexBoolean);
    }


}
