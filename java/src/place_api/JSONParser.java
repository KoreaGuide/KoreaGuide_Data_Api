package place_api;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class JSONParser {
	public static JSONArray parseItems(String input) {
		org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
		JSONObject object = null;
		try {
			object = (JSONObject)(parser.parse(input));
		}
		catch(ParseException e) {
			e.printStackTrace();
		}
		JSONObject parse_response = (JSONObject) object.get("response"); 
		JSONObject parse_body = (JSONObject) parse_response.get("body");
		JSONObject parse_items = (JSONObject) parse_body.get("items");
		JSONArray parse_item = (JSONArray) parse_items.get("item"); //
		
		return parse_item;
	}
	
	public static JSONObject parseItem(String input) {
		org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
		JSONObject object = null;
		try {
			object = (JSONObject)(parser.parse(input));
		}
		catch(ParseException e) {
			e.printStackTrace();
		}
		JSONObject parse_response = (JSONObject) object.get("response"); 
		JSONObject parse_body = (JSONObject) parse_response.get("body");
		JSONObject parse_items = (JSONObject) parse_body.get("items");
		JSONObject parse_item = (JSONObject) parse_items.get("item");
		//JSONArray parse_item = (JSONArray) parse_items.get("item"); //
		
		return parse_item;
	}
	
	public static int getTotalCount(String input) {
		org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
		JSONObject object = null;
		try {
			object = (JSONObject)parser.parse(input);
		}
		catch(ParseException e) {
			e.printStackTrace();
		}
		JSONObject parse_response = (JSONObject) object.get("response"); 
		JSONObject parse_body = (JSONObject) parse_response.get("body");
		int totalCount = Integer.parseInt(parse_body.get("totalCount").toString());
		return totalCount;
	}
}
