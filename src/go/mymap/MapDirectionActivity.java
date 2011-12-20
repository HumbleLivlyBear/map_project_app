package go.mymap;



import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapDirectionActivity extends MapActivity {
	
	MapView mapview;
	MapRouteOverlay mapoverlay;
	Context _context;
	List<Overlay> maplistoverlay;
	Drawable drawable, drawable2;
	MapOverlay mapoverlay2, mapoverlay3;
	GeoPoint srcpoint, destpoint;
	
	List<GeoPoint> listGeopoints = new ArrayList<GeoPoint>();
	
	Overlay overlayitem;
	private MapController mapController;
	private MyLocationOverlay maplayer;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.direction);
        
       // RegisterActivities.registerActivity(this);

        mapview=(MapView)this.findViewById(R.id.mapview);
        
        setGeoPoints();
        callMap();

    }
    
    private void setGeoPoints() {
    	String[] dest = (String[])getIntent().getStringArrayExtra("test");
    	
    	// start point
    	Data.src_lat_data=Double.parseDouble(dest[2]);
    	Data.src_long_data=Double.parseDouble(dest[3]);
    	// dest point
    	
    	Data.dest_lat_data=Double.parseDouble(dest[0]);
    	Data.dest_long_data=Double.parseDouble(dest[1]);
    	
    	//Data.dest_lat_data=25.043187;
    	//Data.dest_long_data=121.564794;
		
	}

	private void callMap() {
		
		//get mapview overlays
		maplistoverlay =mapview.getOverlays();
		
		//get blue_pin and red_pin
    	drawable = this.getResources().getDrawable(R.drawable.blue_pin);
    	drawable2 = this.getResources().getDrawable(R.drawable.red_pin);
    	
    	
    	mapController = mapview.getController();    	
		mapController.setZoom(16);
		mapview.setBuiltInZoomControls(true);
		//定位點
		maplayer = new MyLocationOverlay(this, mapview);
		
		
		//mapController.animateTo(maplayer.getMyLocation());
		srcpoint = new GeoPoint((int)(Data.src_lat_data),(int)(Data.src_long_data));
    	mapoverlay2=new MapOverlay(drawable);
    	OverlayItem overlayitem = new OverlayItem(srcpoint, "","");
    	mapoverlay2.addOverlay(overlayitem);
    	maplistoverlay.add(mapoverlay2);
    	
    	destpoint = new GeoPoint((int)(Data.dest_lat_data*1E6),(int)(Data.dest_long_data*1E6));
    	mapoverlay3 = new MapOverlay(drawable2);
    	OverlayItem overlayitem3 = new OverlayItem(destpoint, "", "");
    	mapoverlay3.addOverlay(overlayitem3);
    	maplistoverlay.add(mapoverlay3);
    	
    	
    	listGeopoints.add(srcpoint);
    	listGeopoints.add(destpoint);
    	
    	fitPoints(mapController, listGeopoints);
    	
    	
    	Drawable drawable = this.getResources().getDrawable(R.drawable.red_pin);
        HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, this);
        itemizedoverlay.setDestAndPhone (destpoint, "04-23444413");
        itemizedoverlay.setMapCenter(mapview);
        OverlayItem overlayitem1 = new OverlayItem(destpoint, "", "");                       
        itemizedoverlay.addOverlay(overlayitem1);  
        maplistoverlay.add(itemizedoverlay);  
    	
    	
//    	HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, this);
//    	itemizedoverlay.setDestAndPhone (destpoint, "04-23444413");
//    	itemizedoverlay.setMapCenter(mapview);
//    	OverlayItem overlayitem1 = new OverlayItem(srcpoint, "目前所在位置", "");
//    	itemizedoverlay.addOverlay(overlayitem1); 
//    	maplistoverlay.add(itemizedoverlay); 
    	//Temporary Not Draw Path
    	//DrawPath(srcpoint, destpoint, Color.GREEN, mapview);
		
		
		//顯示羅盤
		//maplayer.enableCompass();
		//啟動更新(如果坐標有變動會跟著移動)
		//maplayer.enableMyLocation();
//		maplayer.runOnFirstFix(new Runnable()
//		{
//
//			public void run()
//			{   
//				maplistoverlay.clear();
//				mapController.animateTo(maplayer.getMyLocation());
//	
//				int CurrentLat = maplayer.getMyLocation().getLatitudeE6();
//				int CurrentLong = maplayer.getMyLocation().getLongitudeE6();
//				
//				srcpoint = new GeoPoint(CurrentLat,CurrentLong);
//		    	mapoverlay2=new MapOverlay(drawable);
//		    	OverlayItem overlayitem = new OverlayItem(srcpoint, "","");
//		    	mapoverlay2.addOverlay(overlayitem);
//		    	maplistoverlay.add(mapoverlay2);
//		    	
//		    	destpoint = new GeoPoint((int)(Data.dest_lat_data*1E6),(int)(Data.dest_long_data*1E6));
//		    	mapoverlay3 = new MapOverlay(drawable2);
//		    	OverlayItem overlayitem3 = new OverlayItem(destpoint, "", "");
//		    	mapoverlay3.addOverlay(overlayitem3);
//		    	maplistoverlay.add(mapoverlay3);
//
//		    	//DrawPath(srcpoint, destpoint, Color.GREEN, mapview);
//				
//			}
//		});
		maplistoverlay.add(maplayer);
		mapview.invalidate();

		
	}
    
//    private void DrawPath(GeoPoint src, GeoPoint dest, int color, MapView mMapView01){
//    	
//    	// connect to map web service
//        StringBuilder urlString = new StringBuilder();
//        urlString.append("http://maps.google.com/maps?f=d&hl=en");
//        urlString.append("&saddr=");//from
//        urlString.append( Double.toString((double)src.getLatitudeE6()/1.0E6 ));
//        urlString.append(",");
//        urlString.append( Double.toString((double)src.getLongitudeE6()/1.0E6 ));
//        urlString.append("&daddr=");//to
//        urlString.append( Double.toString((double)dest.getLatitudeE6()/1.0E6 ));
//        urlString.append(",");
//        urlString.append( Double.toString((double)dest.getLongitudeE6()/1.0E6 ));
//        urlString.append("&ie=UTF8&0&om=0&output=kml");
//        Log.d("xxx","URL="+urlString.toString());
//        
//        // get the kml (XML) doc. And parse it to get the coordinates(direction route)
//        Document doc = null;
//        HttpURLConnection urlConnection= null;
//        URL url = null;
//        try
//        {
//        	url = new URL(urlString.toString());
//        	urlConnection=(HttpURLConnection)url.openConnection();
//        	urlConnection.setRequestMethod("GET");
//            urlConnection.setDoOutput(true);
//            urlConnection.setDoInput(true);
//            urlConnection.connect();
//            
//            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//            DocumentBuilder db = dbf.newDocumentBuilder();
//            doc = db.parse(urlConnection.getInputStream());
//            
//            if(doc.getElementsByTagName("GeometryCollection").getLength()>0)
//            {
//              //String path = doc.getElementsByTagName("GeometryCollection").item(0).getFirstChild().getFirstChild().getNodeName();
//              String path = doc.getElementsByTagName("GeometryCollection").item(0).getFirstChild().getFirstChild().getFirstChild().getNodeValue() ;
//              Log.d("xxx","path="+ path);
//              String [] pairs = path.split(" ");
//              String[] lngLat = pairs[0].split(","); // lngLat[0]=longitude lngLat[1]=latitude lngLat[2]=height
//              // src
//              GeoPoint startGP = new GeoPoint((int)(Double.parseDouble(lngLat[1])*1E6),(int)(Double.parseDouble(lngLat[0])*1E6));
//              //mMapView01.getOverlays().add(overlayitem);
//              GeoPoint gp1;
//              GeoPoint gp2 = startGP;
//              for(int i=1;i<pairs.length;i++) // the last one would be crash
//              {
//                lngLat = pairs[i].split(",");
//                gp1 = gp2;
//                // watch out! For GeoPoint, first:latitude, second:longitude
//                gp2 = new GeoPoint((int)(Double.parseDouble(lngLat[1])*1E6),(int)(Double.parseDouble(lngLat[0])*1E6));
//                mMapView01.getOverlays().add(new MapRouteOverlay(gp1,gp2,2,color));
//                Log.d("xxx","pair:" + pairs[i]);
//              }
//              //mMapView01.getOverlays().add(new MapRouteOverlay(dest,dest, 3)); // use the default color
//            }
//
//
//        }catch (MalformedURLException e)
//        {
//            e.printStackTrace();
//          }
//          catch (IOException e)
//          {
//            e.printStackTrace();
//          }
//          catch (ParserConfigurationException e)
//          {
//            e.printStackTrace();
//          }
//          catch (SAXException e)
//          {
//            e.printStackTrace();
//          }
//
//
//    }
    
    /**
     * Fits the map with the passed in points so all points are visible.
     * @param mapController MapView controller
     * @param points list of points you want the map to contain
     */
    private static void fitPoints(MapController mapController, List<GeoPoint> points) {
        // set min and max for two points
        int nwLat = -90 * 1000000;
        int nwLng = 180 * 1000000;
        int seLat = 90 * 1000000;
        int seLng = -180 * 1000000;
        // find bounding lats and lngs
        for (GeoPoint point : points) {
    	nwLat = Math.max(nwLat, point.getLatitudeE6()); 
    	nwLng = Math.min(nwLng, point.getLongitudeE6());
    	seLat = Math.min(seLat, point.getLatitudeE6());
            seLng = Math.max(seLng, point.getLongitudeE6());
        }
        GeoPoint center = new GeoPoint((nwLat + seLat) / 2, (nwLng + seLng) / 2);
        // add padding in each direction
        int spanLatDelta = (int) (Math.abs(nwLat - seLat) * 2);
        int spanLngDelta = (int) (Math.abs(seLng - nwLng) * 2);
     
        // fit map to points
        mapController.animateTo(center);
        mapController.zoomToSpan(spanLatDelta, spanLngDelta);
    } 


	@Override
    protected boolean isRouteDisplayed() {
      // TODO Auto-generated method stub
      return false;
    }

}