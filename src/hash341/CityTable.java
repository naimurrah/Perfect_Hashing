package hash341;
/* File: CityTable.java
* Name: Naimur Rahman
* Implementation of CityTable using PerfectHashing through SecondaryTable class
*/
import java.io.*;

import java.util.*;
public class CityTable implements Serializable{
	private static final long serialVersionUID = -8011227600562464363L;
	private SecondaryTable[] PrimaryTable;
	private Hash24 firstHash;
	private int size;
	private int capacity;
	private int[] numJ;
	private int[] numI;
	
	public CityTable(String fname, int tsize) {
		boolean debug = false; // for Debugging
		
		capacity = tsize;
		size = 0;
		firstHash = new Hash24();
		PrimaryTable = new SecondaryTable[capacity];
		
		numI = new int[25];
		numJ = new int[21];
		
		// initializing table - first pass
		for (int i = 0; i < capacity; i++) {
			PrimaryTable[i] = new SecondaryTable();
		}
		
		// Hash dump
		firstHash.dump();
		System.out.println();
		
		// file reading
		Scanner file = null;
		try
		{
			file = new Scanner(new FileReader(fname));
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("File not found");
			System.exit(0);
		}
		// second pass of table - adding items in and hashing accordingly
		while (file.hasNextLine()) {
			String cityName = file.nextLine();
			
			String line = file.nextLine();
			StringTokenizer tokenizer = new StringTokenizer(line);
			float latitude = Float.parseFloat(tokenizer.nextToken());
			float longitude = Float.parseFloat(tokenizer.nextToken());
			
			if (debug) {
				System.out.println("Name: " + cityName);
				System.out.println("Lat: " + latitude);
				System.out.println("Long: " + longitude);
			}
			
			int index = firstHash.hash(cityName) % capacity;
			PrimaryTable[index].add(new City(cityName, latitude, longitude));
			size++;
		}
		file.close();
		
		// Statistics
		// Primary Table Statistics
		System.out.println();
		System.out.println("Primary Table Statistics:");
		System.out.println("Number of cities = " + size);
		System.out.println("TableSize: " + capacity);
		// max number of collisions
		System.out.println("Max Collisions = " + maxCollisions());
		System.out.println();
		
		// i collisions between between 0 and 24
		calcIandJ();
		for (int i = 0; i < 25; i++) {
			System.out.println("# of primary slots with " + i + " cities = "+ numI[i]);
		}
		System.out.println();
		
		System.out.println("*** Cities in the slot with most collisions ***");
		System.out.print(maxSecondTable());
		System.out.println();
		
		// Secondary Table Statistics
		System.out.println("Secondary Table Statistics:");
		// j hash functions between 1 and 20 inclusive
		for (int i = 1; i < 21; i++) {
			System.out.println("# of secondary hash tables trying " + i + " hash functions = " + numJ[i]);
		}
		System.out.println();
		
		// Counting number of secondary Hash Tables with multiple items
		int numSec = 0;
		for (int i = 2; i < 25; i++) {
			numSec += numI[i];
		}
		System.out.println("Number of Secondary Hash Tables with More than 1 Item = " + numSec);
		// Average Number of Hash Functions tried
		System.out.println("Average Number of Hash Functions Tried: " + averageNumHash());
	}
	
	public int maxCollisions() {
		// searches for table with most collisions and returns the amount of collisions
		int max = PrimaryTable[0].getSize();
		for (int i = 1; i < capacity; i++) {
			if (max < PrimaryTable[i].getSize()) {
				max = PrimaryTable[i].getSize();
			}
		}
		return max;
	}
	
	public SecondaryTable maxSecondTable() {
		// searches for secondary table with most collisions
		int max = PrimaryTable[0].getSize();
		SecondaryTable table = PrimaryTable[0];
		for (int i = 1; i < capacity; i++) {
			if (max < PrimaryTable[i].getSize()) {
				max = PrimaryTable[i].getSize();
				table = PrimaryTable[i];
			}
		}
		return table;
	}
	
	public void printTable() {
		// used for debugging and looking at table
		for (int i = 0; i < capacity; i++) {
			if (PrimaryTable[i] != null) {
				for (int j = 0; j < PrimaryTable[i].getCapacity(); j++)  {
					if (PrimaryTable[i].get(j) != null) {
						System.out.println(i + ", " + j + ": " + PrimaryTable[i].get(j));
					}
				}
			}
		}
	}
	
	
	
	
	public float averageNumHash() {
		// calculates the average number of hash functions for tables with multiple items
		float sum = 0;
		float times = 0;
		for (int i = 0; i < capacity; i++) {
			if (PrimaryTable[i].getSize() > 1) {
				sum += PrimaryTable[i].getNumHashes();
				times += 1;
			}
		}
		
		if (times == 0) {
			return 0;
		}
		else {
			return sum / times;
		}
	}
	
	private void calcIandJ() {
		// for statistics
		// calculates i and j values respectively at same time to save time
		for (int i = 0; i < capacity; i++) {
			if (PrimaryTable[i].getSize() >= 0 && PrimaryTable[i].getSize() <= 24) {
				//System.out.println("Collisions: " + PrimaryTable[i].getNumCollisions());
				numI[PrimaryTable[i].getSize()]++;
			}
			if (PrimaryTable[i].getNumHashes() >= 1 && PrimaryTable[i].getNumHashes() <= 20 && PrimaryTable[i].getSize() > 1) {
				//System.out.println("Hashes: " + PrimaryTable[i].getNumHashes());
				numJ[PrimaryTable[i].getNumHashes()]++;
			}
		}
	}
	
	public City find(String cName) {
		int index1 = firstHash.hash(cName) % capacity;
		int index2 =  PrimaryTable[index1].hashVal(cName);
		if (PrimaryTable[index1].get(index2) != null && PrimaryTable[index1].get(index2).getName().equals(cName)) {
			return PrimaryTable[index1].get(index2);
		}
		else {
			// not in table
			return null;
		}
	}
	
	public void writeToFile(String fName) {
		try {
			FileOutputStream fout = new FileOutputStream(fName, true); // overwrites file if already exists
	        ObjectOutputStream oout = new ObjectOutputStream(fout);
	        oout.writeObject(this); // writes table to fName
	        oout.close();
		}
		catch (Exception e) {
			// there is an exception thrown
			e.printStackTrace();
		}

	}
	
	public static CityTable readFromFile(String fName) {
		CityTable ct = null;
		try {
			FileInputStream fout = new FileInputStream(fName);
	        ObjectInputStream oin = new ObjectInputStream(fout);
	        ct = (CityTable) oin.readObject();
	        oin.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ct;
	}
}
