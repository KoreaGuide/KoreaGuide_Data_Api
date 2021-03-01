package place_api;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Test {
	public static void main(String[] args) {
		
		DBConnector.connectDB();
		
		String areaResult = null;
		
		try {
			areaResult = APICaller.callAPI(URLBuilder.getAreaListURL());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int areaCount = JSONParser.getTotalCount(areaResult); //17
		JSONArray areaItems = JSONParser.parseItems(areaResult);
		
		for(int i = 0; i < areaItems.size(); i++) {
			JSONObject object = (JSONObject)areaItems.get(i);
			
			String areaCode = object.get("code").toString();
			String areaName = object.get("name").toString();
			
			System.out.println("Area total count : " + areaCount);
			System.out.println(areaCode + " " + areaName);
			
			
			String sigunguResult = null;
			
			try {
				sigunguResult = APICaller.callAPI(URLBuilder.getSigunguListURL("1", areaCode));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			int sigunguCount = JSONParser.getTotalCount(sigunguResult);
			
			if(sigunguCount > 1) {
				JSONArray sigunguItems = JSONParser.parseItems(sigunguResult);
				
				for(int j = 0; j < sigunguItems.size(); j++) {
					JSONObject obj = (JSONObject)sigunguItems.get(j);
					
					String sigunguCode = obj.get("code").toString();
					String sigunguName = obj.get("name").toString();
					
					String listResult = null;
					try {
						listResult = APICaller.callAPI(URLBuilder.getPlaceListURL("1", areaCode, sigunguCode));
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					int listCount = JSONParser.getTotalCount(listResult);
					
					System.out.println(sigunguCode + " " + sigunguName + " " + listCount);
					
					DBConnector.insert(areaCode, areaName, sigunguCode, sigunguName, listCount);
				}
				
				for(int page = 2; page <= sigunguCount/21 + 1; page++) {
					try {
						sigunguResult = APICaller.callAPI(URLBuilder.getSigunguListURL("" + page, areaCode));
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					sigunguItems = JSONParser.parseItems(sigunguResult);
					
					for(int k = 0; k < sigunguItems.size(); k++) {
						JSONObject obj = (JSONObject)sigunguItems.get(k);
						
						String sigunguCode = obj.get("code").toString();
						String sigunguName = obj.get("name").toString();
						
						String listResult = null;
						try {
							listResult = APICaller.callAPI(URLBuilder.getPlaceListURL("1", areaCode, sigunguCode));
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						int listCount = JSONParser.getTotalCount(listResult);
						
						System.out.println(sigunguCode + " " + sigunguName + " " + listCount);
						
						DBConnector.insert(areaCode, areaName, sigunguCode, sigunguName, listCount);
					}
				}
			}
			else {
				JSONObject sigunguItem = JSONParser.parseItem(sigunguResult);
				
				String sigunguCode = sigunguItem.get("code").toString();
				String sigunguName = sigunguItem.get("name").toString();
				
				String listResult = null;
				try {
					listResult = APICaller.callAPI(URLBuilder.getPlaceListURL("1", areaCode, sigunguCode));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				int listCount = JSONParser.getTotalCount(listResult);
				System.out.println(sigunguCode + " " + sigunguName + " " + listCount);
				DBConnector.insert(areaCode, areaName, sigunguCode, sigunguName, listCount);
				
			}
		}
		
		DBConnector.disconnectDB();
		
		/*
		String jsonResult = null;
		
		System.out.println("-------");
		
		
		
		System.out.println("---");
		
		try {
			jsonResult = APICaller.callAPI(URLBuilder.getSigunguListURL("1", ""));
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
				e.printStackTrace();
			}
			
			//JSONArray detailParsedItems = (JSONArray) (JSONParser.parseItem(jsonDetailResult) ); ////exception!
			
			JSONObject detailParsedItem = JSONParser.parseItem(jsonDetailResult);
			Place place = convertObject(detailParsedItem); 
			System.out.println(++count);
			DBConnector.insert(place);
			
			
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
					e.printStackTrace();
				}
				
				JSONObject detailParsedItem = JSONParser.parseItem(jsonDetailResult);
				
				Place place = convertObject(detailParsedItem); 
				System.out.println(++count);
				DBConnector.insert(place);
				
			}
		}
		*/
		
		
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




