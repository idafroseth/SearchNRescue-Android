package com.zenser.searchnrescue_android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.zenser.searchnrescue_android.common.Constants;
import com.zenser.searchnrescue_android.map.MapLayerFactory;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MapView map;
    private IMapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestApplicationPermissions();
        setContentView(R.layout.activity_map);
        configureMapView();
    }


    private void configureMapView(){
        setBackgroundMap(MapLayerFactory.KARTVERKET_NORGESKART_TOPO2);
        getMap().setMultiTouchControls(true);
        getMapController().setZoom(9);
        getMapController().setCenter(Constants.GEOPOINT_LILLEHAMMER);
        getMap().getOverlays().add(getCompassOverlay());
    }

    private CompassOverlay getCompassOverlay(){
        CompassOverlay mCompassOverlay = new CompassOverlay(getApplicationContext(), new InternalCompassOrientationProvider(getApplicationContext()), getMap());
        mCompassOverlay.enableCompass();
        return mCompassOverlay;

    }
    /**
     * Set the active background map in the MapView
     * @param backgroundMap
     */
    public void setBackgroundMap(ITileSource backgroundMap){
        getMap().setTileSource(backgroundMap);
        getMap().setMaxZoomLevel(backgroundMap.getMaximumZoomLevel());
    }

    public MapView getMap(){
        if(map == null){
            map = (MapView) findViewById(R.id.map);
        }
        return map;
    }

    public IMapController getMapController(){
        if (mapController == null){
            mapController = map.getController();
        }
        return mapController;
    }


    public void requestApplicationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> arrayList = new ArrayList<>();

            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                arrayList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                arrayList.add(Manifest.permission.RECORD_AUDIO);
            }
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                arrayList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.MANAGE_DOCUMENTS) != PackageManager.PERMISSION_GRANTED) {
                arrayList.add(Manifest.permission.MANAGE_DOCUMENTS);
            }
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                arrayList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if (arrayList.size() > 0) {
                requestPermissions(arrayList.toArray(new String[arrayList.size()]), 10);
            }
        }
    }
}
