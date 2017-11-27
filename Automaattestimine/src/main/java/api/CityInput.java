package api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class CityInput {
	
	public String getCityName() throws IOException {
		String city = "";
		System.out.println("Do you want to enter city name from console or from file?");		
		Scanner read = new Scanner(System.in);
		String answer = read.nextLine();
		if(answer.equals("console")) {
			city = getCityNameFromConsole();
		}else if(answer.equals("file")) {
			city = getCityNameFromFile();
		}else {
			System.out.println("You should choose 'console' or 'file'!");
		}
		read.close();
		return city;
	}
	
	public String getCityNameFromConsole() {
    	System.out.println("Enter a city: ");
    	Scanner read = new Scanner(System.in);
		String city = read.nextLine();
		read.close();
		return city;
    }
	
	public String getCityNameFromFile() throws IOException {
		try(BufferedReader br = new BufferedReader(new FileReader(
				"C:/Users/Robert/Documents/GitHub/automaattestimine/Automaattestimine/input.txt"))) {
			StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String city = sb.toString().trim();
		    return city;
		}
    }
	
}
