package raul.datc;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Polygon heatMap1;
    private Polygon heatMap2;
    private Polygon heatMap3;
    private Polygon heatMap4;
    private Button getTemp;
    int[] tempArray = new int[]{ -15,12,30,0 };
    private Integer t1= -15;
    private Integer t2=12;
    private Integer t3=30;
    private Integer t4=0;


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

        LatLng Tm = new LatLng(45.745356,  21.228479);
        mMap.addMarker(new MarkerOptions().position(Tm).title("Timisoara"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Tm));
        getTemp = findViewById(R.id.button1);
        drawHeatMaps();

        getTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (t1 < 0)
                {
                    heatMap1.setFillColor(0x0000FF);
                }
            }
        });

    }

    private void drawHeatMaps() {
        heatMap1 = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(45.742692, 21.246249), new LatLng(45.742907, 21.247095), new LatLng(45.742095, 21.247567),new LatLng(45.741875, 21.246719))
                .strokeColor(0x00FFFFFF)
                .fillColor(0x330000FF));
        heatMap2 = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(45.741678, 21.246721), new LatLng(45.741869, 21.247625), new LatLng(45.740912, 21.248116),new LatLng(45.740723, 21.247209))
                .strokeColor(0x00FFFFFF)
                .fillColor(0x330000FF));
        heatMap3 = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(45.742696, 21.247421), new LatLng(45.742836, 21.247923), new LatLng(45.742117,  21.248317),new LatLng(45.741978, 21.247823))
                .strokeColor(0x00FFFFFF)
                .fillColor(0x330000FF));
        heatMap4 = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(45.741860, 21.248976), new LatLng(45.742058, 21.249710), new LatLng(45.741167, 21.250198),new LatLng(45.740984, 21.249447))
                .strokeColor(0x00FFFFFF)
                .fillColor(0x330000FF));
    }
}
