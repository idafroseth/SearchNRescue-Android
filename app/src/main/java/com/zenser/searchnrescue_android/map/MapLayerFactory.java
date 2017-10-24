package com.zenser.searchnrescue_android.map;

import android.content.Context;
import android.util.Log;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;

/**
 * MapLayerFactory for different map sources from Kartverket and Geodata and layers.
 */
public class MapLayerFactory {

    private static final String LOG_TAG = "MapLayerFactory";

    public static final String NORGESKART_SOURCE_NAME= "Norgeskart";
    public static final String GEODATA_TILE_SOURCE_NAME = "Geodata "+NORGESKART_SOURCE_NAME;//"GeodataOnlinorne";
    public static final String GEODATA_BILDER_TILE_SOURCE_NAME = "Norge i bilder";
    public static final String NORGESKART_SOURCE_GRUNNKART_NAME= NORGESKART_SOURCE_NAME+" Grunnkart";
    public static final String NORGESKART_SOURCE_TOPO2_NAME= NORGESKART_SOURCE_NAME+" Topografisk";

    private static final String GEO_SERVER_BASE_URL = "http://services.geodataonline.no/arcgis/rest/services/Geocache_WMAS_WGS84/";
    private static final String GEO_SERVER_SERVICE_MAP_BASIC = "GeocacheBasis/MapServer/tile/";
    private static final String GEO_SERVER_SERVICE_MAP_PHOTO = "GeocacheBilder/MapServer/tile/";


    public static final XYTileSource GEODATA_TILE_SOURCE = new XYTileSource(GEODATA_TILE_SOURCE_NAME, 0, 18, 256, ".png", new String[]{"http://services.geodataonline.no/arcgis/rest/services/Geocache_WMAS_WGS84/GeocacheBasis/MapServer/tile/"}) {
        @Override
        public String getTileURLString(MapTile mapTile) {
            String s = String.format(GEO_SERVER_BASE_URL+GEO_SERVER_SERVICE_MAP_BASIC+"%d/%d/%d",
                    mapTile.getZoomLevel(), mapTile.getY(),mapTile.getX());
            Log.d(LOG_TAG,s);
            return s;
        }

    };
    public static final XYTileSource GEODATA_BILDER_SOURCE = new XYTileSource(GEODATA_BILDER_TILE_SOURCE_NAME, 0, 18, 256, ".png", new String[]{"http://services.geodataonline.no/arcgis/rest/services/Geocache_WMAS_WGS84/GeocacheBilder/MapServer/tile/"}) {
        @Override
        public String getTileURLString(MapTile mapTile) {
            String s = String.format(GEO_SERVER_BASE_URL+GEO_SERVER_SERVICE_MAP_PHOTO+"%d/%d/%d",
                    mapTile.getZoomLevel(), mapTile.getY(),mapTile.getX());
            return s;
        }

    };

    public static final OnlineTileSourceBase KARTVERKET_NORGESKART_TOPO2 = new XYTileSource(NORGESKART_SOURCE_TOPO2_NAME, 0, 18, 256, ".png", new String[]{""}){
        @Override
        public String getTileURLString(MapTile mapTile){

            String tileUrl = String.format(" http://opencache.statkart.no/gatekeeper/gk/gk.open_gmaps?layers=topo2&transparent=true&zoom=%d&x=%d&y=%d",
                    mapTile.getZoomLevel(), mapTile.getX(),  mapTile.getY());
            Log.d("FFI URL ", tileUrl);
            return tileUrl;
        }
    };


    public static final OnlineTileSourceBase KARTVERKET_NORGESKART_TOPO_TERRENG = new XYTileSource(NORGESKART_SOURCE_TOPO2_NAME +"terreng", 0, 18, 256, ".png", new String[]{""}){
        @Override
        public String getTileURLString(MapTile mapTile){

            String tileUrl = String.format(" http://opencache.statkart.no/gatekeeper/gk/gk.open_gmaps?layers=terreng_norgeskart&transparent=true&zoom=%d&x=%d&y=%d",
                    mapTile.getZoomLevel(), mapTile.getX(),  mapTile.getY());
            Log.d("FFI URL ", tileUrl);
            return tileUrl;
        }
    };

    public static final OnlineTileSourceBase KARTVERKET_NORGESKART_GRUNNKART = new XYTileSource(NORGESKART_SOURCE_GRUNNKART_NAME, 0, 18, 256, ".png", new String[]{""}){
        @Override
        public String getTileURLString(MapTile mapTile){

            String tileUrl = String.format(" http://opencache.statkart.no/gatekeeper/gk/gk.open_gmaps?layers=norges_grunnkart&transparent=true&zoom=%d&x=%d&y=%d",
                    mapTile.getZoomLevel(), mapTile.getX(),  mapTile.getY());
            Log.d("FFI URL ", tileUrl);
            return tileUrl;
        }
    };


    public static final XYTileSource FORSVARET_AVALANCHE_SOURCE= new XYTileSource("Skredkart", 0, 18, 256, ".png", new String[]{"http://wms3.nve.no/map/rest/services/SkredSnoForsvaret/MapServer/1"}) {
        @Override
        public String getTileURLString(MapTile mapTile) {
            String s = String.format("http://services.geodataonline.no/arcgis/rest/services/Geocache_WMAS_WGS84/GeocacheBilder/MapServer/tile/%d/%d/%d",
                    mapTile.getZoomLevel(), mapTile.getY(),mapTile.getX());
            return s;
        }

    };

    public static final XYTileSource SMART_TILE_SERVER= new XYTileSource("smart", 0, 18, 256, ".png", new String[]{"http://192.168.3.226/mapserver/"}) {
        @Override
        public String getTileURLString(MapTile mapTile) {
            String s = String.format("http://192.168.3.226/mapserver/%d/%d/%d"+".png",
                    mapTile.getZoomLevel(), mapTile.getX(),mapTile.getY());
            return s;
        }
    };

    /*
    public static final WMSTileProvider MGRS_GRID_LINES_US_NAVY = new WMSTileProvider("MGRS_US_NAVY","http://egeoint.nrlssc.navy.mil/arcgis/rest/services/usng/USNG_93/MapServer/export?",0,22);

    public static final TilesMapLayer createMGRS100KMLayer(Context context) {
        return new TilesMapLayer(new MapTileProviderBasic(context, MGRS_100KM_GRID_PROVIDER), context);
    }
    public static final TilesMapLayer createUTM10KMLayer(Context context) {
        return new TilesMapLayer(new MapTileProviderBasic(context, UTM_10KM_GRID_LINES_PROVIDER), context);
    }
    public static final TilesMapLayer createUTM1KMLayer(Context context) {
        return new TilesMapLayer(new MapTileProviderBasic(context, UTM_1KM_GRID_LINES_PROVIDER), context);
    }
    public static final TilesMapLayer createUTMLayer(Context context) {
        return new TilesMapLayer(new MapTileProviderBasic(context, UTM_GRID_PROVIDER), context);
    }*/
}
