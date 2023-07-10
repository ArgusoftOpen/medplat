package com.argusoft.sewa.android.app.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;

@EActivity
public class MapsMarkerActivity extends MenuActivity
        implements OnMapReadyCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        ArrayList<LatLng> markersArray = new ArrayList<>();
        LatLng shela = new LatLng(23.006960, 72.457320);
        LatLng argusoft = new LatLng(23.223350, 72.647710);
        markersArray.add(shela);
        markersArray.add(argusoft);
        for (int i = 0; i < markersArray.size(); i++) {
            createMarker(googleMap, markersArray.get(i).latitude, markersArray.get(i).longitude, "name", "");
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(shela));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.include);
        setSupportActionBar(toolbar);
        setTitle(UtilBean.getTitleText(LabelConstants.GEO_LOCATORS));
    }

    private void createMarker(GoogleMap googleMap,double latitude, double longitude, String title, String snippet) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
