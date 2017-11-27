package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

//Simple way to read json
//Source>-- https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java

public class JsonDataReader {
	
	private static String textFromUrl1 = "";
	private static String textFromUrl2 = "";
	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }

	  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      if(url.contains("/weather")) {
	    	  textFromUrl1 = jsonText;
	      }else if(url.contains("/forecast")) {
	    	  textFromUrl2 = jsonText;
	      }	      
	      JSONObject json = new JSONObject(jsonText);
	      return json;
	    } finally {
	      is.close();
	    }
	  }

	public static String getTextFromUrl1() {
		return textFromUrl1;
	}

	public static String getTextFromUrl2() {
		return textFromUrl2;
	}
}
