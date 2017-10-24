package com.zenser.searchnrescue_android.map;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.zenser.searchnrescue_android.R;
import com.zenser.searchnrescue_android.common.Constants;
import com.zenser.searchnrescue_android.wrapper.Toaster;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.util.LinkedList;


/**
 * Created by Ida Marie Frøseth on 06/07/2017.
 *
 * This Fragments holds the map and the buttons related to to the map.
 *
 * @Todo add a IMyLocationProvider (E.G GPS provider) Read the OSMDroid wiki for how this works.
 */
public class MapFragment extends Fragment
        implements View.OnClickListener,
        MapEventsReceiver,
        IMyLocationConsumer,
        MapListener {

    private static final String LOG_TAG = "MapFragment";

    private MapView mMapView;
    private IMapController mMapController;
    private View mView;
    private Location currentLocation;

    public MapFragment() {
    }

    /**
     * Initiate the MapFragment.
     * @return
     */
    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d(LOG_TAG, "onCreate");
        configureOfflineMapDirectory();
        //enableDebugging();
    }

    /**
     * Configure directories where offline maps should be stored. For simplsity the download_dir/map is used.
     */
    private void configureOfflineMapDirectory() {
        //Zip files should be put in the /map folder
        File mapFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/map");
        //Sqllite files should be put in the /map/tiles folder
        File tileFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/map/tiles");
        if (mapFolder != null) {
            Log.d(LOG_TAG, "OfflineMapPath: " + mapFolder);
            Configuration.getInstance().setOsmdroidBasePath(mapFolder);
            Configuration.getInstance().setOsmdroidTileCache(tileFolder);

        }
    }

    /**
     * Method to enable debugging on the OSMDroid framework
     */
    private void enableDebugging() {
        //  Configuration.getInstance().setDebugMapTileDownloader(true);
        Configuration.getInstance().setDebugTileProviders(true);
        mMapView.setUseDataConnection(false);
    }

    /**
     * Callbackmethod for onCreateView which is called by the Android system when the fragment view is created.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(LOG_TAG, "onCreateView");
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        mView.findViewById(R.id.map_fab_center_location).setOnClickListener(this);
        mView.findViewById(R.id.map_fab_map_menu).setOnClickListener(this);

        mMapView = (MapView) mView.findViewById(R.id.map);
        mMapController = mMapView.getController();
        initMapView();

        return mView;
    }

    /**
     * Method to init the map view. This include enabling multitouch gestures, configuring the map source to fetch maps from kartverket,
     * add maplisteners and enable hardware acceleration.
     */
    private void initMapView() {
        setBackgroundMap(MapLayerFactory.KARTVERKET_NORGESKART_TOPO2);

        mMapView.setBuiltInZoomControls(false);
        mMapView.setMultiTouchControls(true);
        mMapView.setUseDataConnection(true);
        mMapView.setTilesScaledToDpi(true);
        mMapView.setMapListener(this);

        //Turn on hardware acceleration
        mMapView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        mMapController.setCenter(Constants.GEOPOINT_LILLEHAMMER);
        mMapController.setZoom(9);

        mMapView.getOverlays().add(getCompassOverlay());
        mMapView.invalidate();
    }

    /**
     * Set the active background map in the MapView
     * @param backgroundMap
     */
    public void setBackgroundMap(ITileSource backgroundMap){
        if(mMapView != null) {
            mMapView.setTileSource(backgroundMap);
            mMapView.setMaxZoomLevel(backgroundMap.getMaximumZoomLevel());
        }
    }


    /**
     * Add a OSMDroid specific compassOverlay.
     * @Todo check if the InternalCompassProvider is working. If not, create one.
     * @return
     */
    private CompassOverlay getCompassOverlay(){
        CompassOverlay mCompassOverlay = new CompassOverlay(getContext(), new InternalCompassOrientationProvider(getContext()),mMapView);
        mCompassOverlay.enableCompass();
        return mCompassOverlay;

    }


    /**
     * The onResume method which is called by the Android system whenever the app is resumed.
     * @Todo here we should add a LocationOverlay and initAutomaticUpdates from GPS
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
        //initLocationOverlay();
        //initAutomaticLocationUpdates();

    }

    /**
     * The onStop method which is called by the Android system whenever the app is going into stop state.
     */
    @Override
    public void onStop() {
        Log.d(LOG_TAG, "onStop");
        super.onStop();
        //removeMyLocationOverlay();
        //initLazyUpdates();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "onDetach");

        if (mMapView != null) {
            mMapView.onDetach();
        }
        destroyCache();
    }


    public void destroyCache() {
        if (mMapView != null) {
            mMapView.destroyDrawingCache();
            mMapView.getOverlays().clear();
            mMapView.invalidate();
        }

        if (mView != null) {
            mView.destroyDrawingCache();
            mView.invalidate();
        }

    }

    /**
     * Callbackmethod when the user click a point in the map
     * @param tappedLocation
     * @return
     */
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint tappedLocation) {
        return false;
    }

    /**
     * Callbackmethod when the user perform a longpress in the map.
     * @TODO in this method we should open a menu which enables the user to register an observation
     * @param p the location the user clicked
     * @return
     */
    @Override
    public boolean longPressHelper(GeoPoint p) {
        vibrate();
        if (mMapController != null) {
            mMapController.animateTo(p);
        }
        return true;
    }


    /**
     * Callback method for the IMyLocationProvider interface from OSMDroid. Update the current location.
     *
     * @param location users new location
     * @param source which source did update the location
     */
    @Override
    public void onLocationChanged(Location location, IMyLocationProvider source) {
        Log.d(LOG_TAG, "onLocationChanged");

        if (location != null) {
            currentLocation = location;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_fab_center_location:
                centerMapToCurrentLocation();
                break;
            case R.id.map_fab_map_menu:
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_around_center);
                v.startAnimation(animation);
                break;

        }
    }


    /**
     * Method which will zoom the map to the last registered location.
     */
    private void centerMapToCurrentLocation() {
        if (currentLocation != null ) {
            mMapController.animateTo(new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()));
            //@Todo show a menu here.
        } else {
            Toaster.showInfo(getContext(), "Søker fortsatt etter GPS..", Toast.LENGTH_SHORT);
        }
    }


    /**
     * Callback when the user scroll the map
     * @param event
     * @return
     */
    @Override
    public boolean onScroll(ScrollEvent event) {
        return false;
    }

    /**
     * Callback when the user zoom the map
     * @param event
     * @return
     */
    @Override
    public boolean onZoom(ZoomEvent event) {
        return false;
    }


    /**
     * Method to trigger {@link #vibrate(int)} with '20' as default value
     */
    public void vibrate() {
        vibrate(20);
    }

    /**
     * Triggers a vibration on the device, by fetching getting the system service {@link Context#VIBRATOR_SERVICE}.
     *
     * @param milliseconds duration of the vibration
     */
    public void vibrate(int milliseconds) {
        ((Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(milliseconds);
    }
}
