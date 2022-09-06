package hash341;
/* File: City.java
* Name: Naimur Rahman
* City class implementation
*/
import java.io.Serializable;

public class City implements Serializable {
	private static final long serialVersionUID = 6086008662786938109L;
	public String name;
	public float latitude;
	public float longitude;
	
	
	public City(String name, float latitude, float longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return name + " (" + latitude + ", " + longitude + ")";
	}
	
	// getters and setters
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	
}
