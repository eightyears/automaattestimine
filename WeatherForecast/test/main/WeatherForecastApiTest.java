package main;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WeatherForecastApiTest {
	
	WeatherForecastApi api;

	@Before
	public void setUp() throws Exception {
		api = new WeatherForecastApi();
	}
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsCurrentTemperaturePossible() {
		assertTrue("Temperature must be higher than -90 degrees", api.getCurrentTemperature()>-90);
		assertTrue("Temperature must be lower than 60 degrees", api.getCurrentTemperature()<60);
		assertNotNull("Temperature value must not be null", api.getCurrentTemperature());
	}
	
	@Test
	public void testIsHighestTemperaturePossible() {
		assertTrue("Temperature must be higher than -90 degrees", api.getHighestTemperature()>-90);
		assertTrue("Temperature must be lower than 60 degrees", api.getHighestTemperature()<60);
		assertNotNull("Temperature value must not be null", api.getHighestTemperature());
	}
	
	@Test
	public void testIsLowestTemperaturePossible() {
		assertTrue("Temperature must be higher than -90 degrees", api.getLowestTemperature()>-90);
		assertTrue("Temperature must be lower than 60 degrees", api.getLowestTemperature()<60);
		assertNotNull("Temperature value must not be null", api.getLowestTemperature());
	}
	
	@Test
	public void testIsHigestTemperatureHigherThanLowest(){
		assertTrue("Highest temperature must be higher than lowest temperature", 
			api.getHighestTemperature()>api.getLowestTemperature());
	}
	
	@Test
	public void testIsThreeNextDaysWeatherResponseFormatJSON(){		
		assertTrue("getNextThreeDaysTemperature method returned value must start with '{' and end with '}'",
				api.getNextThreeDaysTemperature().startsWith("{") &&
				api.getNextThreeDaysTemperature().endsWith("}"));
		assertNotNull("getNextThreeDaysTemperature method returned value must not be null", 
				api.getNextThreeDaysTemperature());
	}
	
	@Test
	public void testGetCoordinates(){		
		assertTrue("Coordinates length must be seven", 
				api.getCoordinates().length()==7);
		assertTrue("Symbols indexed as 0 to 2 must be digits", 
				api.getCoordinates().substring(0, 2).matches("\\d+"));
		assertTrue("Coordinates 'x' and 'y' must be separated by colon", 
				api.getCoordinates().indexOf(":") == 3);
		assertTrue("Symbols indexed as 4 to 6 must be digits", 
				api.getCoordinates().substring(4, 6).matches("\\d+"));
		assertNotNull("getCoordinates method returned value must not be null", 
				api.getCoordinates());
	}

}
