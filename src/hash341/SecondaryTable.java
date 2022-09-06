package hash341;

/* File: SecondaryTable.java
* Name: Naimur Rahman
* implementation of secondary table that goes in each primary slot - changes hash function if run into collisions
*/

import java.io.Serializable;

public class SecondaryTable implements Serializable {
	private static final long serialVersionUID = 1840163062984879208L;
	private City[] cities;
	private int size;
	private int capacity;
	private Hash24 secondHash;
	private int numHashes;
	
	public SecondaryTable() {
		size = 0;
		capacity = 1;
		cities = new City[1];
		secondHash = new Hash24();
		numHashes = 1;
		
	}
	
	public int getSize() {
		return size;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public int getNumHashes() {
		return numHashes;
	}
	
	public int getNumCollisions() {
		return size-1;
	}
	
	public int hashVal(String cName) {
		// returns hash index that cName would have - used in find
		int index = secondHash.hash(cName) % capacity;
		return index;
	}
	
	private void rehash() {
		boolean collision = false;
		City[] newCities = new City[capacity];
		for (int i = 0; i < capacity; i++) {
			if (cities[i] != null) {
				int index = secondHash.hash(cities[i].getName()) % capacity;
				if (newCities[index] == null) {
					newCities[index] = cities[i];
				}
				else {
					// current hash does not work must change
					collision = true;
					changeHash();
					break;
				}
			}
		}
		
		if (!collision) {
			// current hash still works
			cities = newCities;
		}
	}
	
	private void changeHash() {
		// changes hash function to perfect one
		boolean collision;
		City[] newCities;
		
		do {
			collision = false;
			newCities = new City[capacity];
			numHashes++;
			secondHash = new Hash24();
			
			//  System.out.println("HASH");
			
			for (int i = 0; i < capacity; i++) {
				if (cities[i] != null) {
					int index = secondHash.hash(cities[i].getName()) % capacity;
					
					if (newCities[index] == null) {
						newCities[index] = cities[i];
					}
					else {
						collision = true;
						break;
					}
				}
			}
		} while (collision);
		
		cities = newCities;
	}
	
	private void resize(int n) {
		// resizes table to size n
		City[] newCities = new City[n];
		for (int i = 0; i < capacity; i++) {
			if (cities[i] != null) {
				newCities[i] = cities[i];
			}
		}
		cities = newCities;
		capacity = n;
		
	}
	
	public void add(City c) {
		if (size == 0) {
			cities[0] = c;
			
		}
		else {
			int newCapacity = (size+1)*(size+1);
			resize(newCapacity);
			//System.out.println("Done1");
			cities[capacity-1] = c;
			rehash();
			//System.out.println("Done2");
		}
		size++;
	}
	
	public City get(String cName) {
		// gets object at hash of cName
		int index = secondHash.hash(cName) % capacity;
		City item = cities[index];
		return item;
	}
	
	public City get(int i) {
		// gets object at index i of table - used in find, will check in CityTable if object is correct
		return cities[i];
	}
	
	@Override
	public String toString() {
		String arr = "";
		for (int i = 0; i < capacity; i++) {
			if (cities[i] != null) {
				arr += cities[i].toString() + "\n";
			}
		}
		return arr;
	}
	
}
