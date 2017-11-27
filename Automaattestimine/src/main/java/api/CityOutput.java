package api;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CityOutput {
	
	public void writeOutputToFile(String responseInfo) throws IOException {
		PrintWriter output = new PrintWriter(new FileWriter(
				"C:/Users/Robert/Documents/GitHub/automaattestimine/Automaattestimine/output.txt", true), true);
	    output.write(responseInfo);
	    output.close();
	}
}
