package bbx;

import java.util.HashMap;
import java.util.Map;

public class Location {
	
	public static int lastId = -1;
	
	public int id;
	public String name;
	public String address;
	public int openTime;
	public int closeTime;
	public double latitude;
	public double longitude;
	public Map<Integer,Integer> books; //key: book ID, value: num available
	
	
	public Location(String name, String address, int openTime, int closeTime, double latitude, double longitude) {

		lastId++;
		id = lastId;
		
		this.name = name;
		this.address = address;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.latitude = latitude;
		this.longitude = longitude;
		this.books = new HashMap<Integer, Integer>();
	}
	
	/** Adds a book of the specified ID. */
	public void addBook(Integer bookId) {
		Integer numBooks = books.get(bookId);
		if (numBooks == null)
			books.put(bookId, new Integer(1));
		else
			books.put(bookId, new Integer(numBooks++));
	}
	
	public boolean hasBook(Integer bookId) {
		return books.containsKey(bookId);
	}
	
	/** Gets out one book of the specified book ID. */
	public boolean takeOutBook(Integer bookId) {
		Integer numBooks = books.get(bookId);
		if (numBooks == null || numBooks <= 0)
			return false; //there were no books available so failure
		books.put(bookId, new Integer(numBooks--));
		return true;
	}
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getOpenTime() {
		return openTime;
	}
	public void setOpenTime(int openTime) {
		this.openTime = openTime;
	}
	public int getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(int closeTime) {
		this.closeTime = closeTime;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public Map<Integer, Integer> getBooks() {
		return books;
	}
	
	
	
	/** Serialization only! */
	public Location(int id, String name, String address, int openTime, int closeTime, double latitude, double longitude,
			Map<Integer, Integer> books) {

		this.id = id;
		this.name = name;
		this.address = address;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.latitude = latitude;
		this.longitude = longitude;
		this.books = books;
	}
	/** Deserialization only! */
	public void setId(int id) {
		this.id = id;
	}
	/** Deserialization only! */
	public void setBooks(Map<Integer, Integer> books) {
		this.books = books;
	}
	
	
}
