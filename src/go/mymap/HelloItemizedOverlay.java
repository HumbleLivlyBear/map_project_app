package go.mymap;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

/** 
 *  The HelloItemizedOverlay is writed for pup up alert in the google map
 *  In the alert, it will contain three functions:
 * 	1.Nevigation
 *  2.Phone
 *  3.Street View
 * 
 * */

public class HelloItemizedOverlay extends ItemizedOverlay {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	Context mContext;
	//TextView mTextView1;
	private Handler mHandler = new Handler();	
	private GeoPoint mapCenter;
	private GeoPoint destination;
	private String phoneNumber;
	
	
	public HelloItemizedOverlay(Drawable defaultMarker) {
		  super(boundCenterBottom(defaultMarker));
	}
	
	public HelloItemizedOverlay(Drawable defaultMarker, Context context) {
		  super(boundCenterBottom(defaultMarker));
		  mContext = context;		  
	}	
			
	@Override
	protected OverlayItem createItem(int i) {
	  return mOverlays.get(i);
	}

	@Override
	public int size() {
	  return mOverlays.size();
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}	
	
	public void setMapCenter(MapView mapView){
		mapCenter = mapView.getMapCenter();
	}
	
	public void setDestAndPhone (GeoPoint itemDestination, String itemPhoneNumber){
		destination = itemDestination;
		phoneNumber = itemPhoneNumber;
	}
	
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);	  
//	  final GeoPoint destination = item.getPoint();
//	  final String phoneNumber = item.getSnippet();
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);		  	  
	  dialog.setPositiveButton("確認",
			    new DialogInterface.OnClickListener(){
	        	public void onClick(
	            DialogInterface dialoginterface, int i){
	        		//Blank content
	            }
	  });
	  final String options[] = new String[]{"導航至此","撥電話: "+phoneNumber, "Google街景"};	 
	  dialog.setItems(options, new DialogInterface.OnClickListener(){
          
          public void onClick(DialogInterface dialog, int index){
        	  
        	  if(index ==0){//Route to the location
        		  clickOnAndroidRoute(mapCenter.getLatitudeE6()/1E6, mapCenter.getLongitudeE6()/1E6, destination.getLatitudeE6()/1E6, destination.getLongitudeE6()/1E6);
        	  }
        	  else if(index ==1){//make a call
        		  clickOnAndroidPhone(phoneNumber);
        	  }
        	  else{
        		  showStreetView(destination.getLatitudeE6()/1E6, destination.getLongitudeE6()/1E6);
        	  }
          }
       });
	  
	  dialog.setTitle(item.getTitle());
	  //dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}
	
    public void clickOnAndroidRoute(double startLat, double startLng, double endLat, double endLng) {        	
    	final String routeUrl = "http://maps.google.com/maps?f=d&saddr="+startLat+","+startLng+
								"&daddr="+endLat+","+endLng+"&hl=tw";        	
        mHandler.post(new Runnable() {
            public void run() {                	
            	Uri uri = Uri.parse(routeUrl);  
            	Intent it = new Intent(Intent.ACTION_VIEW, uri);  
            	mContext.startActivity(it);            	                      
            }
        });
    } 	
    
    public void clickOnAndroidPhone(String phonenumber) {
    	final String myPhoneNumber = phonenumber;
        mHandler.post(new Runnable() {
            public void run() {
            	Uri uri = Uri.parse("tel:"+myPhoneNumber);  
            	Intent it = new Intent(Intent.ACTION_DIAL, uri);  
            	mContext.startActivity(it);                      
            }
        });
    }   
    
    public void showStreetView(double lat, double lng) {
    	final String StreetViewMsg = "google.streetview:cbll=" + lat +"," + lng + "&cbp=1,30,,0,1.0";
    	
        mHandler.post(new Runnable() {
            public void run() {
            	Uri uri = Uri.parse(StreetViewMsg);  
            	Intent it = new Intent(Intent.ACTION_VIEW, uri);  
            	mContext.startActivity(it);                      
            }
        });
    }    
}

