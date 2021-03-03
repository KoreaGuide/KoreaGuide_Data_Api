package place_api;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Test {
	public static void main(String[] args) {
		
		//String tmp = "fefejife'ennfneil'efkn";
		//System.out.println(tmp.replaceAll("'", "---"));
		
		placeSaver();
		
		
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
		
		for(int i = 2; i <= totalCount/21 + 1; i++) {
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
	
	
	public static void placeSaver() {
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
					
					////////
					placeDetailSaver(areaCode, sigunguCode);
				}
				
				for(int page = 2; page <= sigunguCount/21 + 1; page++) {
					try {
						sigunguResult = APICaller.callAPI(URLBuilder.getSigunguListURL("" + page, areaCode));
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					if(sigunguCount % 20 == 1) {
						JSONObject sigunguItem = JSONParser.parseItem(sigunguResult);

						String sigunguCode = sigunguItem.get("code").toString();
						String sigunguName = sigunguItem.get("name").toString();
						
						placeDetailSaver(areaCode, sigunguCode);
					}
					else {
						sigunguItems = JSONParser.parseItems(sigunguResult);
						
						for(int k = 0; k < sigunguItems.size(); k++) {
							JSONObject sigunguItem = (JSONObject)sigunguItems.get(k);
							
							String sigunguCode = sigunguItem.get("code").toString();
							String sigunguName = sigunguItem.get("name").toString();
							
							//////
							placeDetailSaver(areaCode, sigunguCode);
						
						}
					}
					
					
					
					
				}
			}
			else {
				JSONObject sigunguItem = JSONParser.parseItem(sigunguResult);
				
				String sigunguCode = sigunguItem.get("code").toString();
				String sigunguName = sigunguItem.get("name").toString();
				
				////////
				placeDetailSaver(areaCode, sigunguCode);
			}
		}
		DBConnector.disconnectDB();
	}
	
	public static void placeDetailSaver(String areaCode, String sigunguCode) {
		String listResult = null;
		try {
			listResult = APICaller.callAPI(URLBuilder.getPlaceListURL("1", areaCode, sigunguCode));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int listCount = JSONParser.getTotalCount(listResult);
		
		if(listCount > 1) {
			JSONArray placeItems = JSONParser.parseItems(listResult);
			
			for(int i = 0; i < placeItems.size(); i++) {
				JSONObject placeItem = (JSONObject)placeItems.get(i);
				Place place = convertObject(placeItem);
				
				//detail search
				String detailResult = null;
				try {
					detailResult = APICaller.callAPI(URLBuilder.getPlaceDetailURL(place.contentid));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				int detailCount = JSONParser.getTotalCount(detailResult);
				if(detailCount > 1) {
					JSONArray detailItems = JSONParser.parseItems(detailResult);
					System.out.println("..... detailItems .....");
				}
				else {
					JSONObject detailItem = JSONParser.parseItem(detailResult);
					
					place.overview = detailItem.get("overview").toString().replaceAll("'", "''").replaceAll("<be>", "").replaceAll("<br >", "\n").replaceAll("<BR>", "\n").replaceAll("<br>", "\n").replaceAll("<Br>", "\n").replaceAll("&nbsp;", " ").replaceAll("<b>", "").replaceAll("</b>", ""); //'
					
					place.cat1 = detailItem.get("cat1").toString();
					place.cat2 = detailItem.get("cat2").toString();
					place.cat3 = detailItem.get("cat3").toString();
					
					place.areaCode = Integer.parseInt(areaCode);
					place.sigunguCode = Integer.parseInt(sigunguCode);
					
					DBConnector.insert(place);
				}
				//detailResult -> get overview
			}
			
			for(int page2 = 2; page2 <= listCount/21 + 1; page2++) {
				try {
					listResult = APICaller.callAPI(URLBuilder.getPlaceListURL(page2 + "", areaCode, sigunguCode));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if(listCount % 20 == 1) {

					JSONObject placeItem = JSONParser.parseItem(listResult);
					Place place = convertObject(placeItem);
					
					//detail search
					String detailResult = null;
					try {
						detailResult = APICaller.callAPI(URLBuilder.getPlaceDetailURL(place.contentid));
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					int detailCount = JSONParser.getTotalCount(detailResult);
					if(detailCount > 1) {
						JSONArray detailItems = JSONParser.parseItems(detailResult);
						System.out.println(".....detailItems.....");
					}
					else {
						JSONObject detailItem = JSONParser.parseItem(detailResult);
						place.overview = detailItem.get("overview").toString().replaceAll("'", "''").replaceAll("<be>", "").replaceAll("<br >", "\n").replaceAll("<BR>", "\n").replaceAll("<br>", "\n").replaceAll("<Br>", "\n").replaceAll("&nbsp;", " ").replaceAll("<b>", "").replaceAll("</b>", ""); //'
						
						place.cat1 = detailItem.get("cat1").toString();
						place.cat2 = detailItem.get("cat2").toString();
						place.cat3 = detailItem.get("cat3").toString();
						
						place.areaCode = Integer.parseInt(areaCode);
						place.sigunguCode = Integer.parseInt(sigunguCode);
						
						DBConnector.insert(place);
					}
					//detailResult -> get overview
				}
				else {
					placeItems = JSONParser.parseItems(listResult); // 여기서 문제~ 여기서 하나가 나온듯?????? //
					
					for(int i = 0; i < placeItems.size(); i++) {
						JSONObject placeItem = (JSONObject)placeItems.get(i);
						Place place = convertObject(placeItem);
						
						//detail search
						
						String detailResult = null;
						try {
							detailResult = APICaller.callAPI(URLBuilder.getPlaceDetailURL(place.contentid));
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						int detailCount = JSONParser.getTotalCount(detailResult);
						if(detailCount > 1) {
							JSONArray detailItems = JSONParser.parseItems(detailResult);	
							System.out.println("..... detailItems .....");
						}
						else {
							JSONObject detailItem = JSONParser.parseItem(detailResult);
							

							place.overview = detailItem.get("overview").toString().replaceAll("'", "''").replaceAll("<be>", "").replaceAll("<br >", "\n").replaceAll("<BR>", "\n").replaceAll("<br>", "\n").replaceAll("<Br>", "\n").replaceAll("&nbsp;", " ").replaceAll("<b>", "").replaceAll("</b>", ""); //'
							
							place.cat1 = detailItem.get("cat1").toString();
							place.cat2 = detailItem.get("cat2").toString();
							place.cat3 = detailItem.get("cat3").toString();
							
							place.areaCode = Integer.parseInt(areaCode);
							place.sigunguCode = Integer.parseInt(sigunguCode);
							
							DBConnector.insert(place);
						}
						//detailResult -> get overview
					}
				}
				
				
			}
		}
		else if(listCount == 0) {
			
		}
		else {
			JSONObject placeItem = JSONParser.parseItem(listResult);
			Place place = convertObject(placeItem);
			
			//detail search
			String detailResult = null;
			try {
				detailResult = APICaller.callAPI(URLBuilder.getPlaceDetailURL(place.contentid));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			int detailCount = JSONParser.getTotalCount(detailResult);
			if(detailCount > 1) {
				JSONArray detailItems = JSONParser.parseItems(detailResult);
				System.out.println(".....detailItems.....");
			}
			else {
				JSONObject detailItem = JSONParser.parseItem(detailResult);
				

				place.overview = detailItem.get("overview").toString().replaceAll("'", "''").replaceAll("<be>", "").replaceAll("<br >", "\n").replaceAll("<BR>", "\n").replaceAll("<br>", "\n").replaceAll("<Br>", "\n").replaceAll("&nbsp;", " ").replaceAll("<b>", "").replaceAll("</b>", ""); //'
				
				place.cat1 = detailItem.get("cat1").toString();
				place.cat2 = detailItem.get("cat2").toString();
				place.cat3 = detailItem.get("cat3").toString();
				
				place.areaCode = Integer.parseInt(areaCode);
				place.sigunguCode = Integer.parseInt(sigunguCode);
				
				DBConnector.insert(place);
			}
			//detailResult -> get overview
		}
		
	}
	
	public static void codeSaver() {
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
	}
	
	public static Place convertObject(JSONObject object) {
		Place place = new Place();
		place.title = object.get("title").toString().replaceAll("'", "''");
		if(object.get("addr1") != null) {
			place.addr1 = object.get("addr1").toString();
		}
		if(object.get("addr2") != null) {
			place.addr2 = object.get("addr2").toString();
		}
		
		place.contentid = object.get("contentid").toString();
		place.contenttypeid = object.get("contenttypeid").toString();
		
		if(object.get("areacode") != null) {
			place.areaCode = Integer.parseInt(object.get("areacode").toString());
		}
		
		if(object.get("sigungucode") != null) {
			place.sigunguCode = Integer.parseInt(object.get("sigungucode").toString());
		}
		
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
		
		if(object.get("cat1") != null) {
			place.cat1 = object.get("cat1").toString();
		}
		if(object.get("cat2") != null) {
			place.cat2 = object.get("cat2").toString();
		}
		if(object.get("cat3") != null) {
			place.cat3 = object.get("cat3").toString();
		}
		
		
		return place;
	}
	
	
}




