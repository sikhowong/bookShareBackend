package bbx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class Logic {
	private static final String USER_AGENT = "Mozilla/5.0";
	private static final String ISBN_GET_URL = "http://isbndb.com/api/v2/json/0D2YAWB1/book/";

	public static void main(String[] args) {
		try {
			Logic.getBookInfo("9780849303159");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** Parses params into a map, even changing special characters like %20. Very bad code. For example,
	if the %20 is at the end it won't work! */
	public static Map<String,String> parseParams(String params) throws Exception {
		Map<String,String> ret = new HashMap<String, String>();
		
		String[] ps = params.split("&");
		
		for (int i = 0; i < ps.length; i++) {
			String[] kv = ps[i].split("=",2);
			
			for (int j = 0; j < kv[1].length()-2; j++) {
				if (kv[1].charAt(j) == '%') {
					char newChar = (char)(Integer.parseInt(kv[1].substring(j+1, j+3),16));
					kv[1] = kv[1].substring(0, j)+newChar+kv[1].substring(j+3, kv[1].length());
				}
			}
			
			ret.put(kv[0], kv[1]);
		}
		return ret;
	}

	/** Gets a map containing title, ISBN 13, and summary from an ISBN 13. */
	public static Map<String, String> getBookInfo(String isbn) throws IOException {
		Map<String, String> bookMap = new HashMap<String, String>();
		URL obj = new URL(ISBN_GET_URL+isbn);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			JsonParser parser = new JsonParser();
			JsonElement jsonTree = parser.parse(response.toString());
			JsonObject jsonObject = jsonTree.getAsJsonObject();
			JsonElement jsonElement  = jsonObject.get("data");
			JsonArray innerObj = jsonElement.getAsJsonArray();
			JsonObject bookObj = innerObj.get(0).getAsJsonObject();

			bookMap.put("title", bookObj.get("title").toString().replace("\"", ""));
			bookMap.put("isbn13", bookObj.get("isbn13").toString().replace("\"", ""));
			bookMap.put("summary", bookObj.get("summary").toString().replace("\"", ""));
			
			System.out.println(response.toString());
		} else {
			System.out.println("GET request not worked");
		}
		return bookMap;
	}
}
