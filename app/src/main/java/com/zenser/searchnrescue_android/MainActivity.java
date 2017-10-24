package com.zenser.searchnrescue_android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zenser.searchnrescue_android.map.MapFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestApplicationPermissions();
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 10: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startFragment(R.id.container_map, MapFragment.newInstance(), getIntent().getExtras());
                } else {
                    Log.i(LOG_TAG, "User did not accept all permissions!");
                    startFragment(R.id.container_map, MapFragment.newInstance(), getIntent().getExtras());
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * Adding a fragment to a transaction fetched from the Support Fragment Manager,
     * committing the fragment to start it's lifecycle.
     *
     * @param viewContainer Reference to a container in the parent activity
     * @param fragment      Instance that should be inflated in the container
     * @param bundle        Bundle with arguments to send in to fragment, can be null
     */
    protected void startFragment(int viewContainer, Fragment fragment, Bundle bundle) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (null == bundle) {
            bundle = new Bundle();
        }
        fragment.setArguments(bundle);

        ft.replace(viewContainer, fragment);
        ft.commit();
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
            } else {
                startFragment(R.id.container_map, MapFragment.newInstance(), getIntent().getExtras());
            }
        } else {
            startFragment(R.id.container_map, MapFragment.newInstance(), getIntent().getExtras());
        }
    }
}
