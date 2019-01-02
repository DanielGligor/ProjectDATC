package raul.datc;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.function.IntBinaryOperator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Polygon heatMap1;
    private Polygon heatMap2;
    private Polygon heatMap3;
    private Polygon heatMap4;
    private Polygon fullMap;
    private Button goToHum;
    private Button goToTemp;
    private int darkRed=0xBFcc0000;
    private int lightRed=0xBFff3300;
    private int darkOrange=0xBFff6600;
    private int lightOrange=0xBFffcc00;
    private int darkYellow=0xBFffcc00;
    private int darkGreen=0xBF00b300;
    private int darkBlue=0xBF003366;
    private int lightBlue=0xBF1a8cff;
    private int lightGreen=0xBF99ff33;
    private TextView label1;
    private Integer t1= -15;
    private Integer t2=20;
    private Integer t3=30;
    private Integer t4=0;
    private Marker m1;
    private Marker m2;
    private Marker m3;
    private Marker m4;
     LatLng ll1 = new LatLng(45.742692, 21.246249);
     LatLng ll2 = new LatLng(45.741869, 21.247625);
   LatLng ll3 = new LatLng(45.742696, 21.247421);
    LatLng ll4 = new LatLng(45.741860, 21.248976);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoomLevel = 16.0f;



        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll1, zoomLevel));
        goToHum = findViewById(R.id.button2);
        goToTemp = findViewById(R.id.button1);
        label1 = findViewById(R.id.textview);
        fullMap = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(45.742872, 21.245585), new LatLng(45.743080, 21.250034), new LatLng(45.740961, 21.250924),new LatLng(45.740624, 21.246504))
                .strokeColor(0x00FFFFFF)
                .fillColor(0x0D0066cc));
        drawHeatMaps();


        goToTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMarkers();
                removeHeatMaps();
                drawHeatMaps();
                getTemperature();
            }
        });
        goToHum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeMarkers();
                removeHeatMaps();
                drawHumMaps();
                getHumidity();
            }
        });

    }

    private void removeMarkers() {
        m1.remove();
        m2.remove();
        m3.remove();
        m4.remove();
    }

    private void createMarkers() {
        final IconGenerator iconFactory = new IconGenerator(this);
        MarkerOptions markerOptions1 = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("Field #1: "+t1.toString()+"C"))).
                position(ll1).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        m1 = mMap.addMarker(markerOptions1);

        MarkerOptions markerOptions2 = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("Field #2: "+t2.toString()+"C"))).
                position(ll2).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        m2 = mMap.addMarker(markerOptions2);

        MarkerOptions markerOptions3 = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("Field #3: "+t3.toString()+"C"))).
                position(ll3).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        m3 = mMap.addMarker(markerOptions3);

        MarkerOptions markerOptions4 = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("Field #4: "+t4.toString()+"C"))).
                position(ll4).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        m4 = mMap.addMarker(markerOptions4);
    }


    private void getTemperature() {
        if(t1 <= -3)
            heatMap1.setFillColor(darkBlue);
        else if(t1 > -3 && t1 < 1)
            heatMap1.setFillColor(lightBlue);
        else if(t1 > 0 && t1 < 3)
            heatMap1.setFillColor(lightGreen);
        else if(t1 >= 3 && t1 < 7)
            heatMap1.setFillColor(darkGreen);
        else if(t1 >=7 && t1 < 11)
            heatMap1.setFillColor(darkYellow);
        else if(t1 >=11 && t1 < 15)
            heatMap1.setFillColor(lightOrange);
        else if(t1 >= 15 && t1 < 20)
            heatMap1.setFillColor(darkOrange);
        else if(t1 >=20 && t1 < 25)
            heatMap1.setFillColor(lightRed);
        else if(t1 >=25 )
            heatMap1.setFillColor(darkRed);

        if(t2 <= -3)
            heatMap2.setFillColor(darkBlue);
        else if(t2 > -3 && t2 < 1)
            heatMap2.setFillColor(lightBlue);
        else if(t2 > 0 && t2 < 3)
            heatMap2.setFillColor(lightGreen);
        else if(t2 >= 3 && t2 < 7)
            heatMap2.setFillColor(darkGreen);
        else if(t2 >=7 && t2 < 11)
            heatMap2.setFillColor(darkYellow);
        else if(t2 >=11 && t2 < 15)
            heatMap2.setFillColor(lightOrange);
        else if(t2 >= 15 && t2 < 20)
            heatMap2.setFillColor(darkOrange);
        else if(t2 >=20 && t2 < 25)
            heatMap2.setFillColor(lightRed);
        else if(t2 >=25 )
            heatMap2.setFillColor(darkRed);

        if(t3 <= -3)
            heatMap3.setFillColor(darkBlue);
        else if(t3 > -3 && t3 < 1)
            heatMap3.setFillColor(lightBlue);
        else if(t3 > 0 && t3 < 3)
            heatMap3.setFillColor(lightGreen);
        else if(t3 >= 3 && t3 < 7)
            heatMap3.setFillColor(darkGreen);
        else if(t3 >=7 && t3 < 11)
            heatMap3.setFillColor(darkYellow);
        else if(t3 >=11 && t3 < 15)
            heatMap3.setFillColor(lightOrange);
        else if(t3 >= 15 && t3 < 20)
            heatMap3.setFillColor(darkOrange);
        else if(t3 >=20 && t3 < 25)
            heatMap3.setFillColor(lightRed);
        else if(t3 >=25 )
            heatMap3.setFillColor(darkRed);

        if(t4 <= -3)
            heatMap4.setFillColor(darkBlue);
        else if(t4 > -3 && t4 < 1)
            heatMap4.setFillColor(lightBlue);
        else if(t4 > 0 && t4 < 3)
            heatMap4.setFillColor(lightGreen);
        else if(t4 >= 3 && t4 < 7)
            heatMap4.setFillColor(darkGreen);
        else if(t4 >=7 && t4 < 11)
            heatMap4.setFillColor(darkYellow);
        else if(t4 >=11 && t4 < 15)
            heatMap4.setFillColor(lightOrange);
        else if(t4 >= 15 && t4 < 20)
            heatMap4.setFillColor(darkOrange);
        else if(t4 >=20 && t4 < 25)
            heatMap1.setFillColor(lightRed);
        else if(t4 >=25 )
            heatMap4.setFillColor(darkRed);
    }

    private void getHumidity() {
        heatMap1.setFillColor(darkGreen);
        heatMap2.setFillColor(darkRed);
        heatMap3.setFillColor(darkOrange);
        heatMap4.setFillColor(darkYellow);
    }

    private void drawHumMaps() {
        drawHeatMaps();
    }

    private void removeHeatMaps() {
        heatMap1.remove();
        heatMap2.remove();
        heatMap3.remove();
        heatMap4.remove();
    }
    private void drawHeatMaps() {
        heatMap1 = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(45.742692, 21.246249), new LatLng(45.742907, 21.247095), new LatLng(45.742095, 21.247567),new LatLng(45.741875, 21.246719))
                .strokeColor(0x00FFFFFF)
                .fillColor(0x00FFFFFF));
        heatMap2 = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(45.741678, 21.246721), new LatLng(45.741869, 21.247625), new LatLng(45.740912, 21.248116),new LatLng(45.740723, 21.247209))
                .strokeColor(0x00FFFFFF)
                .fillColor(0x00FFFFFF));
        heatMap3 = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(45.742696, 21.247421), new LatLng(45.742836, 21.247923), new LatLng(45.742117,  21.248317),new LatLng(45.741978, 21.247823))
                .strokeColor(0x00FFFFFF)
                .fillColor(0x00FFFFFF));
        heatMap4 = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(45.741860, 21.248976), new LatLng(45.742058, 21.249710), new LatLng(45.741167, 21.250198),new LatLng(45.740984, 21.249447))
                .strokeColor(0x00FFFFFF)
                .fillColor(0x00FFFFFF));

    }
}
