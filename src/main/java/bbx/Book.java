package bbx;

public class Book {
	private String isbn;
	private String title;
	private String summary;
	private String userEmail;
	public Book(String isbn, String title, String summary, String userEmail){
		this.isbn = isbn;
		this.title = title;
		this.summary = summary;
		this.userEmail = userEmail;
	}
	
	public void setUserEmail(String userEmail){
		this.userEmail = userEmail;
	}
	public String getUserEmail(){
		return userEmail;
	}
	
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
}
