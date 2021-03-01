package place_api;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Test {
	public static void main(String[] args) {
		String jsonResult = null;
		
		System.out.println("---");
		
		try {
			jsonResult = APICaller.callAPI(URLBuilder.getAreaListURL());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("---");
		
		try {
			jsonResult = APICaller.callAPI(URLBuilder.getSigunguListURL("1", "1"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("---");
		
		try {
			jsonResult = APICaller.callAPI(URLBuilder.getPlaceListURL("1", "A01", "A0101", "A01010100"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		int totalCount = JSONParser.getTotalCount(jsonResult);
		JSONArray parsedItems = JSONParser.parseItems(jsonResult);
		
		int count = 0;
		
		for(int j = 0; j < parsedItems.size(); j++) {
			JSONObject object = (JSONObject)parsedItems.get(j);
			String jsonDetailResult = null;
			try {
				jsonDetailResult = APICaller.callAPI(URLBuilder.getPlaceDetailURL(object.get("contentid").toString()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//JSONArray detailParsedItems = (JSONArray) (JSONParser.parseItem(jsonDetailResult) ); ////exception!
			
			JSONObject detailParsedItem = JSONParser.parseItem(jsonDetailResult);
			Place place = convertObject(detailParsedItem); 
			System.out.println(++count);
			DBConnector.insert(place);
			
			/*
			for(int k = 0; k < detailParsedItems.size(); k++) {
				JSONObject detailObject = (JSONObject)detailParsedItems.get(k);
				Place place = convertObject(detailObject); //convert //
				System.out.println(++count);
				DBConnector.insert(place);
			}*/
		}
		
		for(int i = 2; i <= totalCount/10 + 1; i++) {
			try {
				jsonResult = APICaller.callAPI(URLBuilder.getPlaceListURL(i + "", "A01", "A0101", "A01010100"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			parsedItems = JSONParser.parseItems(jsonResult);
			
			for(int j = 0; j < parsedItems.size(); j++) {
				JSONObject object = (JSONObject)parsedItems.get(j);
				String jsonDetailResult = null;
				try {
					jsonDetailResult = APICaller.callAPI(URLBuilder.getPlaceDetailURL(object.get("contentid").toString()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				JSONObject detailParsedItem = JSONParser.parseItem(jsonDetailResult);
				
				Place place = convertObject(detailParsedItem); 
				System.out.println(++count);
				DBConnector.insert(place);
				/*
				for(int k = 0; k < detailParsedItem.size(); k++) {
					JSONObject detailObject = (JSONObject)detailParsedItem.get(k);
					Place place = convertObject(detailObject); //convert //
					System.out.println(++count);
					DBConnector.insert(place);
				}*/
			}
		}
	}
	
	
	
	
	public static Place convertObject(JSONObject object) {
		Place place = new Place();
		place.title = object.get("title").toString();
		place.addr1 = object.get("addr1").toString();
		place.contentid = object.get("contentid").toString();
		place.contenttypeid = object.get("contenttypeid").toString();
		if(object.get("firstimage") != null) {
			place.firstimage = object.get("firstimage").toString(); 
		}
		
		if(object.get("firstimage2") != null) {
			place.firstimage2 = object.get("firstimage2").toString(); 
		}
		
		if(object.get("mapx") != null) {
			place.mapx = object.get("mapx").toString();
		}
		
		if(object.get("mapy") != null) {
			place.mapy = object.get("mapy").toString();
		}
		
		if(object.get("overview") != null) {
			place.overview = object.get("overview").toString();
		}
		
		
		return place;
	}
	
	
}




