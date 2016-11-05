package bbx;

import java.util.HashMap;
import java.util.Map;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class Main {
	
	public static String directory = "C:\\Users\\devma\\Desktop\\Hackathon\\site";
	
	public static Map<String, User> users = new HashMap<String, User>();
	public static Map<String, Book> booksMap = new HashMap<String , Book>();
	
	public static void populateBooksMap(){
		booksMap.put("9780849303159:abc@gmail.com", new Book("9780849303159", "Principles of solid mechanics", "Evolving from more than 30 years of research and teaching experience", "abc@gmail.com"));
		booksMap.put("9781858051642:abc@gmail.com", new Book("9781858051642", "Computing: Computer Science", "CS book", "abc@gmail.com"));
		
	}
	
	public static void main(String[] args) {
		
		System.out.println("Starting up...");
		populateBooksMap();
		
		users.put("devmas@gmail.com", new User("Colin", "Morgan", "devmas@gmail.com", "123 Fake St.", "no", 10032,100));
		users.put("john@gmail.com", new User("John", "Smith", "john@gmail.com", "20 Nice Pl.", "no", 10023,100));
		


        Spark.staticFiles.externalLocation(directory);
        Spark.get("/hello", (req, res) -> "Hello World");
        
        Spark.get("/getUsers", new Route() {

			public Object handle(Request arg0, Response arg1) throws Exception {
	        	Gson gson = new Gson();
	        	String u = gson.toJson(users);
	        	return u;
			}
        });
        
        Spark.post("/logout", new Route() {
			public Object handle(Request req, Response resp) throws Exception {
				req.session().attribute("user", null);
				return "0 user logged out successfully";
			}
        });
        
        Spark.post("/login", new Route() {

			public Object handle(Request req, Response resp) throws Exception {
				
				if (req.session().attribute("user") != null) return "1 error logging in; already logged in as "+req.session().attribute("user");
				
				Map<String,String> m = null;
				try {
					m = Logic.parseParams(req.body());
				} catch (Exception e) {
					e.printStackTrace();
					return "1 error logging in; bad programmer";
				}
				
				System.out.println(m);
				
				String email = m.get("email");
				String password = m.get("password");
				
				User u = users.get(email);
				if (u == null) return "1 error logging in; user "+email+" not found";
				if (!u.password.equals(password)) return "1 error logging in; wrong password";
				

				req.session().attribute("user", u.email);

				return "0 user logged in successfully";
			}
        });
        
        
        
        
        Spark.post("/newUser", new Route() {

			@Override
			public Object handle(Request req, Response resp) {
				
				//if (users.containsKey(email)) return "0 user exists";
				//users.put(email, new User(name, email, address, password, zipCode));
				//return "1 user successfully created";
				
				Map<String,String> m = null;
				try {
					m = Logic.parseParams(req.body());
				} catch (Exception e) {
					e.printStackTrace();
					return "1 error in creating user; bad programmer";
				}
				
				System.out.println(m);
				
				String firstName = m.get("firstname");
				String lastName = m.get("lastname");
				String password = m.get("password");
				String email = m.get("email");
				int zipCode;
				try {
					zipCode = Integer.parseInt(m.get("zipcode"));
				} catch(NumberFormatException e) {
					return "1 error in creating user; zip code should be a number";
				}

				if (users.containsKey(email)) {
					return "1 error in creating user; user already exists";
				}
				
				User u = new User(firstName,lastName,email,"",password,zipCode);
				users.put(email, u);
				
				req.session().attribute("user", u.email);

				return "0 user created successfully";
			}
        	
        });
        

//        Spark.get("/addBook", new Route() {
//
//			public Object handle(Request req, Response resp) throws Exception {
//				return "<form action=\"/addBook\" method=\"post\">Go to post: <input type=\"submit\"></input></form>";
//			}
//        	
//        });
        
        
        addBookPostReq();
        usersSubmittedBookGetReq();
        generalBookInfoGetReq();
		
	}
	
	//get request for user submitted book
	public static void usersSubmittedBookGetReq(){
		Spark.get("/userSubmittedBook", new Route() {

			public Object handle(Request req, Response resp) throws Exception {
				
				Gson objGson = new GsonBuilder().setPrettyPrinting().create();
				Type listType = new TypeToken<Map<String, Book>>() {
				}.getType();
				String mapToJson = objGson.toJson(booksMap);
				
				
				return mapToJson;
			}
        	
        });
	}
	
	
	//getting from isbn db
	public static void generalBookInfoGetReq(){
		Spark.get("/generalBookInfo/:isbn", new Route() {

			public Object handle(Request req, Response resp) throws Exception {
				String isbn = req.params(":isbn");
				
				Map<String, String> book = Logic.getBookInfo(isbn);
				
				Gson objGson = new GsonBuilder().setPrettyPrinting().create();
				Type listType = new TypeToken<Map<String, Book>>() {
				}.getType();
				String mapToJson = objGson.toJson(book);
				
				
				return mapToJson;
			}
        	
        });
	}
	
	//Post request for adding book 
	public static void addBookPostReq(){
		Spark.post("/addBook", new Route() {
			public Object handle(Request req, Response resp) throws Exception {
				
				Map<String,String> m = null;
				try {
					m = Logic.parseParams(req.body());
				} catch (Exception e) {
					e.printStackTrace();
					return "1 error in adding book";
				}
				
				System.out.println(m);
				
				String user_email = m.get("user_email");
				String isbn = m.get("isbn");
				String title = m.get("title");
				String summary = m.get("summary");
				
				Book book = new Book(isbn, title, summary, user_email);
				booksMap.put(isbn+":"+user_email, book);

				return "0 : Book added successfully";
			
			}
        	
        });
	}
	
	
}
