package go.mymap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MineMapActivity extends MapActivity {
    /** Called when the activity is first created. */
	
	//set layout, view or button variables
	private LinearLayout enterLayout;
	private LinearLayout homeLayout;
	private LinearLayout searchLayout;
	private LinearLayout infoLayout;
	private LinearLayout addDataLayout;
	private LinearLayout positionLayout;
	private Button enterButton;
	private Button homeButton;
	private Button searchButton;
	private Button infoButton;
	private Button addButton;
	private EditText editAdd;
	private EditText editSearch;
	
	
	
	
	// Use Enum to Switch LayoutType
	public enum LAYOUTTYPE
    {
        HOME, Search, Info, Add
    };
    LAYOUTTYPE type=LAYOUTTYPE.HOME;
    
    // Set Google Map Variables
    private MapView mapView;
	private MapController mapController;
	private MyLocationOverlay maplayer;
	
	private int CurrentLat, CurrentLong;
	
	// Item List
	List<ItemDataStruc> ItemList= new ArrayList<ItemDataStruc>();
	// Item Location Name List
	List<String> LocationNames = new ArrayList<String>();
	// Different Location Classes String Array
	String[] LocationClasses = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /** Read Datas From Heroku First */
        String readTwitterFeed = readTwitterFeed();
        JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(readTwitterFeed);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				
				String name = jsonObject.getString("name");
				String lat=jsonObject.getString("lat");
				String lng=jsonObject.getString("lng");
				ItemDataStruc Item = new ItemDataStruc(name, lat, lng);
				ItemList.add(Item);
				LocationNames.add(name);
			}
	        
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String[] locationNamesArray = new String[LocationNames.size()];
		locationNamesArray = LocationNames.toArray(locationNamesArray);
		
		
		ArrayAdapter<String> arrayData = new ArrayAdapter<String>(
                this
                , android.R.layout.simple_list_item_1
                , locationNamesArray
                );
        
        ListView lv =(ListView)findViewById(R.id.search_list);
        lv.setAdapter( arrayData );
        lv.setOnItemClickListener( new OnItemClickListener(){
            
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub
            	
            	try {
            		
            		
            		
        			ItemDataStruc item = ItemList.get(arg2);
	            	
	                setTitle( "name = "+item.getName()+","+"lat = "+item.getLat()+","+"lng = "+item.getLng());
	                
	                Intent intent = new Intent();
	                intent.setClass( MineMapActivity.this, MapDirectionActivity.class);
	                
	                //Need to Plus PhoneNumber
	                String[] dest={item.getLat(), item.getLng(), String.valueOf(CurrentLat),String.valueOf(CurrentLong)};
	                
	                Bundle mBundle = new Bundle();
	                mBundle.putStringArray("test", dest);
	                intent.putExtras(mBundle); 
	                startActivity(intent);
            	} catch (Exception e) {
        			e.printStackTrace();
        		}
            }
        });
        
       
        findAllLayoutID();
        setButtonToPageView();
        
    }
    
    /** Demo Taipei 101, Read Json from Heroku*/
    public String readTwitterFeed() {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"http://severe-robot-8423.herokuapp.com/locations/get.json");
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				//Log.e(ParseJSON.class.toString(), "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

	private void findAllLayoutID() {
		// Find LayoutIDs 
		enterLayout = (LinearLayout)findViewById(R.id.enter_view);
		homeLayout = (LinearLayout)findViewById(R.id.home_view);
		searchLayout = (LinearLayout)findViewById(R.id.search_view);
		infoLayout = (LinearLayout)findViewById(R.id.info_view);
		addDataLayout = (LinearLayout)findViewById(R.id.add_data_view);

	
		// Find Button IDs
		enterButton = (Button)findViewById(R.id.enterButton);
		homeButton = (Button)findViewById(R.id.homeButton);
		searchButton = (Button)findViewById(R.id.searchButton);
		infoButton = (Button)findViewById(R.id.infoButton);
		addButton = (Button)findViewById(R.id.addButton);
		
	}

	private void setButtonToPageView() {
		
		enterButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type=LAYOUTTYPE.HOME;
				setLayoutType();
			};
		});
		
		homeButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type=LAYOUTTYPE.HOME;
				setLayoutType();
			};
		});
	  
		searchButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type=LAYOUTTYPE.Search;
				setLayoutType();
			};
		});
		
		infoButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type=LAYOUTTYPE.Info;
				setLayoutType();
			};
		});
		
		addButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				type=LAYOUTTYPE.Add;
				setLayoutType();
			};
		});

		
	}

	protected void setLayoutType() {
		
		switch(type){
		
		case HOME:
			enterLayout.setVisibility(View.GONE);
			homeLayout.setVisibility(View.VISIBLE);
			searchLayout.setVisibility(View.INVISIBLE);
			infoLayout.setVisibility(View.INVISIBLE);;
			addDataLayout.setVisibility(View.INVISIBLE);;
			
			
			setGoogleMapView();
			break;
		
		case Search:
			homeLayout.setVisibility(View.INVISIBLE);
			searchLayout.setVisibility(View.VISIBLE);
			infoLayout.setVisibility(View.INVISIBLE);;
			addDataLayout.setVisibility(View.INVISIBLE);;
			
			break;
		case Info:
			homeLayout.setVisibility(View.INVISIBLE);
			searchLayout.setVisibility(View.INVISIBLE);
			infoLayout.setVisibility(View.VISIBLE);;
			addDataLayout.setVisibility(View.INVISIBLE);;
			
			break;
			
		case Add:
			homeLayout.setVisibility(View.INVISIBLE);
			searchLayout.setVisibility(View.INVISIBLE);
			infoLayout.setVisibility(View.INVISIBLE);;
			addDataLayout.setVisibility(View.VISIBLE);;
			
			break;
			
		
		}
		
	}
	
	private void setGoogleMapView() {
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		
		mapController = mapView.getController();
		mapController.setZoom(16);
		
		//定位點
		List<Overlay> overlays = mapView.getOverlays();
		maplayer = new MyLocationOverlay(this, mapView);
		
		//顯示羅盤
		//maplayer.enableCompass();
		//啟動更新(如果坐標有變動會跟著移動)
		maplayer.enableMyLocation();
		maplayer.runOnFirstFix(new Runnable()
		{

			public void run()
			{
				mapController.animateTo(maplayer.getMyLocation());
				CurrentLat = maplayer.getMyLocation().getLatitudeE6();
				CurrentLong = maplayer.getMyLocation().getLongitudeE6();
				
			}
		});
		overlays.add(maplayer);
		
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		// TODO Auto-generated method stub
		return false;
	}
}