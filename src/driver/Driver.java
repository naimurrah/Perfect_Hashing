package driver;
/* File: Driver.java
* Name: Naimur Rahman
* Reads US_Cities_LL.txt and makes a table of all the cities in it
* Second Program gets input for a city and pulls up the info as well as a google maps url
*/
import hash341.*;
import java.util.Scanner;
public class Driver{

	public static void main(String[] args)  {
		System.out.println("First Program\n");
		firstProg();
		System.out.println();
		System.out.println("Second Program\n");
		secondProg();
	}
	
	// first program
	public static void firstProg() {
		// read program and print statistics
		CityTable Pt = new CityTable("US_Cities_LL.txt", 16000);
		Pt.writeToFile("US_Cities_LL.ser");
	}
	
	// second program
	public static void secondProg() {
		// reads file and then gives for url
		CityTable table = CityTable.readFromFile("US_Cities_LL.ser");
		Scanner in = new Scanner(System.in);
		System.out.print("Enter City, State (or 'quit'): ");
		String name = in.nextLine();
		while (!name.equals("quit")) {
			City city = table.find(name);
			if (city == null || !city.getName().equals(name)) {
				// city not in table
				System.out.println("Could not find \'" + name + "\'");
				System.out.print("Enter City, State (or 'quit'): ");
				name = in.nextLine();
				continue;
			}
			
			String url = "http://www.google.com/maps?z=10&q=" + city.getLatitude() + "," + city.getLongitude();
			System.out.println("Found " + city);
			System.out.println(url);
			
			System.out.print("Enter City, State (or 'quit'): ");
			name = in.nextLine();
		}
		in.close();
		System.out.println("Program  Done");
		
	}

}
