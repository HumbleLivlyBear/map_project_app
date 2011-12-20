package go.mymap;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MapOverlay extends ItemizedOverlay {
	  private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	  public MapOverlay(Drawable _defaultMarker) {

	    super(boundCenterBottom(_defaultMarker));
	  }

	  @Override
	  protected OverlayItem createItem(int i) {
	    return mOverlays.get(i);
	  } 
	  public void addOverlay(OverlayItem overlay) {
	      mOverlays.add(overlay);
	      populate();
	  }

	  @Override
	  public int size() {
	    return mOverlays.size();
	  }

	}

