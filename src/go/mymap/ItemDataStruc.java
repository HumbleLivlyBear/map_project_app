package go.mymap;

/** 
 *  Item Data Structure. Should Including:
 *  1.ClassName
 *  2.ItemName
 *  3.PhoneNumber
 * 	4.Lat
 *  5.Lng
 * 
 * */


public class ItemDataStruc {
	
	private String className;
	private String itemName;
	private String phoneNumber;
	private String lat;
	private String lng;
	
	// A Array For All Data
	// allDataInArray(0) : className,
	// allDataInArray(1) : itemName,
	// allDataInArray(2) : phoneNumber,
	// allDataInArray(3) : lat,
	// allDataInArray(4) : lng.
	
	private String[] allDataInArray = new String[5];
	
	public ItemDataStruc(String Itemname, String Itemlat, String Itemlng){
		
		itemName = Itemname;
		lat = Itemlat;
		lng = Itemlng;
		allDataInArray[0] = "";
		allDataInArray[1] = itemName;
		allDataInArray[2] = "";
		allDataInArray[3] = lat;
		allDataInArray[4] = lng;
		
	}
	
	public ItemDataStruc(String Itemname,String ItemPhone, String Itemlat, String Itemlng){
		
		itemName = Itemname;
		lat = Itemlat;
		lng = Itemlng;
		phoneNumber = ItemPhone;
		allDataInArray[0] = "";
		allDataInArray[1] = Itemname;
		allDataInArray[2] = ItemPhone;
		allDataInArray[3] = lat;
		allDataInArray[4] = lng;
	}
	
	public ItemDataStruc(String ItemClassName, String Itemname,String ItemPhone, String Itemlat, String Itemlng){
		className = ItemClassName;
		itemName = Itemname;
		lat = Itemlat;
		lng = Itemlng;
		phoneNumber = ItemPhone;
		allDataInArray[0] = ItemClassName;
		allDataInArray[1] = Itemname;
		allDataInArray[2] = ItemPhone;
		allDataInArray[3] = lat;
		allDataInArray[4] = lng;
	}
	
	public String getName(){
		return itemName;
	}
	
	public String getLat(){
		return lat;
	}
	
	public String getLng(){
		return lng;
	}
	
	public String getPhoneNumber(){
		return phoneNumber;
	}
	
	public String getClassName(){
		return className;
	}
	
	public String[] getAllData(){
		return allDataInArray;
	}
}
