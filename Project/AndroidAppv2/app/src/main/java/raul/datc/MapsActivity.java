package raul.datc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.function.IntBinaryOperator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Button[] buttons= new Button[9];
    private TextView scaleTempNumbers;
    private TextView scaleTempTitle;
    private TextView title;
    private GoogleMap mMap;
    private Polygon[] heatMaps = new Polygon[4];
    private Polygon fullMap;
    private Button goToHum;
    private Button goToTemp;
    private Button goBack;
    private Button[] fieldButtons = new Button[4];
    private Button cancel;
    private Button irrigate;
    private int darkRed=0xBFcc0000;
    private int lightRed=0xBFff0000;
    private int darkOrange=0xBFff6600;
    private int lightOrange=0xBFff9933;
    private int darkYellow=0xBFffcc00;
    private int darkGreen=0xBF00b300;
    private int darkBlue=0xBF003366;
    private int lightBlue=0xBF1a8cff;
    private int lightGreen=0xBF99ff33;
    private Integer[] tempValues = {-15, 21, 30, 0};
    private Integer[] humValues= {10, 12 ,30 ,80};
    private Marker[] markers =  new Marker[4];
    private LatLng[] locations = {new LatLng(45.742692, 21.246249),new LatLng(45.741678, 21.246721),new LatLng(45.742836, 21.247923),new LatLng(45.741860, 21.248976)};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoomLevel = 16.0f;
        WeightedLatLng ll;
        int[] colors = {
                Color.rgb(102, 225, 0), // green
                Color.rgb(255, 0, 0)
                // red
        };

        float[] startPoints = {
                0.2f, 1f
        };

        final Gradient gradient = new Gradient(colors, startPoints);
        buttons[0]= findViewById(R.id.button6);
        buttons[1]=findViewById(R.id.button7);
        buttons[2]=findViewById(R.id.button8);
        buttons[3]=findViewById(R.id.button9);
        buttons[4]=findViewById(R.id.button10);
        buttons[5]=findViewById(R.id.button11);
        buttons[6]=findViewById(R.id.button12);
        buttons[7]=findViewById(R.id.button13);
        buttons[8]=findViewById(R.id.button14);
        fieldButtons[0]=findViewById(R.id.button18);
        fieldButtons[1]=findViewById(R.id.button19);
        fieldButtons[2]=findViewById(R.id.button20);
        fieldButtons[3]=findViewById(R.id.button21);
        irrigate= findViewById(R.id.button16);
        cancel= findViewById(R.id.button22);
        title = findViewById(R.id.textview);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.741767,21.247939), zoomLevel));
        goToHum = findViewById(R.id.button2);
        goToTemp = findViewById(R.id.button1);
        goBack = findViewById(R.id.button3);
        scaleTempNumbers = findViewById(R.id.textView);
        scaleTempTitle = findViewById(R.id.textView2);

        fullMap = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(45.742872, 21.245585), new LatLng(45.743080, 21.250034), new LatLng(45.740961, 21.250924),new LatLng(45.740624, 21.246504))
                .strokeColor(0x00FFFFFF)
                .fillColor(0x0D0066cc));
        disableTempScale();
        drawHeatMaps();
        createMarkers();
        disableFields();

        for(int i=0; i<4; i++)
        {   final Integer j=i+1;

            fieldButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toast = Toast.makeText(MapsActivity.this, "Irrigating field #"+j.toString()+"...", Toast.LENGTH_LONG);
                    toast.show();
                    disableFields();
                }
            });
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableFields();
            }
        });
        irrigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFields();
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewTitle("Temperature / Humidity");
                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        goToTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewTitle("Temperature");
                removeMarkers();
                createTempMarkers();
                removeHeatMaps();
                drawHeatMaps();
                getTemperature();
                enableTempScale();
            }
        });
        goToHum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewTitle("Humidity");
                removeMarkers();
                createHumMarkers();
                removeHeatMaps();
                drawHumMaps();
                getHumidity();
                enableHumScale();
            }
        });

    }


    private void enableHumScale() {
        for (int i=0; i<9; i++)
        {
            buttons[i].setVisibility(View.VISIBLE);
        }
        scaleTempNumbers.setVisibility(View.VISIBLE);
        scaleTempTitle.setVisibility(View.VISIBLE);
        irrigate.setVisibility(View.VISIBLE);
        scaleTempTitle.setText("Humidity scale(%)");
        scaleTempNumbers.setText(" 90  80  70  60 40 30 20 10");
    }

    private void disableFields() {
        for (int i=0; i<4; i++)
        {
            fieldButtons[i].setVisibility(View.GONE);
        }
        cancel.setVisibility(View.GONE);
    }
    private void enableFields() {
        for (int i=0; i<4; i++)
        {
            fieldButtons[i].setVisibility(View.VISIBLE);
        }
        cancel.setVisibility(View.VISIBLE);
    }

    private void enableTempScale() {
        for (int i=0; i<9; i++)
        {
            buttons[i].setVisibility(View.VISIBLE);
        }
        scaleTempNumbers.setVisibility(View.VISIBLE);
        scaleTempTitle.setVisibility(View.VISIBLE);
        irrigate.setVisibility(View.VISIBLE);
        scaleTempTitle.setText("Deegres scale(°C)");
        scaleTempNumbers.setText("   -3   1   3   7   11 15  20  25");

    }

    private void disableTempScale() {
        for (int i=0; i<9; i++)
        {
            buttons[i].setVisibility(View.GONE);
        }
        scaleTempNumbers.setVisibility(View.GONE);
        scaleTempTitle.setVisibility(View.GONE);
        irrigate.setVisibility(View.GONE);
    }

    private void setNewTitle(String newTitle) {
        title.setText(newTitle);
    }

    private void createHumMarkers() {
        String hum="%";
        String two=": ";
        for (Integer i=0; i<4; i++){
            createMarker(i+1, locations[i], two + humValues[i].toString() + hum);
        }
    }

    private void removeMarkers() {
       for(int i=0; i<4;i ++)
       {
           markers[i].remove();
       }
    }

    private void createTempMarkers() {
        String temp="°C";
        String two=": ";
        for (Integer i=0; i<4; i++){
            createMarker(i+1, locations[i], two + tempValues[i].toString() + temp);
        }
    }

    private void createMarkers() {
        String empty="";
        for (Integer i=0; i<4; i++){
            createMarker(i+1, locations[i], empty);
        }
    }

    private void createMarker(Integer i, LatLng location, String value) {
        final IconGenerator iconFactory = new IconGenerator(this);

        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("Field #"+i.toString() + value))).
                position(location).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        markers[i-1] = mMap.addMarker(markerOptions);
    }


    private void getHumidity() {

        for(int i=0; i<4; i++){
            if(humValues[i] >= 90){
                heatMaps[i].setFillColor(darkBlue);
                heatMaps[i].setStrokeWidth(5);
                heatMaps[i].setStrokeColor(0x99003366);
            }
            else if(humValues[i] >= 80 && humValues[i] < 90){
                heatMaps[i].setFillColor(lightBlue);
                heatMaps[i].setStrokeWidth(5);
                heatMaps[i].setStrokeColor(0x991a8cff);
            }
            else if(humValues[i] >= 70 && humValues[i] < 80) {
                heatMaps[i].setFillColor(lightGreen);
                heatMaps[i].setStrokeWidth(5);
                heatMaps[i].setStrokeColor(0x9999ff33);
            }
            else if(humValues[i] >=60 && humValues[i] < 70) {
                heatMaps[i].setFillColor(darkGreen);
                heatMaps[i].setStrokeWidth(5);
                heatMaps[i].setStrokeColor(0x9900b300);
            }
            else if(humValues[i] >=40 && humValues[i] < 60) {
                heatMaps[i].setFillColor(darkYellow);
                heatMaps[i].setStrokeWidth(5);
                heatMaps[i].setStrokeColor(0x99ffcc00);
            }
            else if(humValues[i] >=30 && humValues[i] < 40) {
                heatMaps[i].setFillColor(lightOrange);
                heatMaps[i].setStrokeWidth(5);
                heatMaps[i].setStrokeColor(0x99ff9933);
            }
            else if(humValues[i] >= 20 && humValues[i] < 30) {
                heatMaps[i].setFillColor(darkOrange);
                heatMaps[i].setStrokeWidth(5);
                heatMaps[i].setStrokeColor(0x99ff6600);
            }
            else if(humValues[i] >=10 && humValues[i] < 20) {
                heatMaps[i].setFillColor(lightRed);
                heatMaps[i].setStrokeWidth(5);
                heatMaps[i].setStrokeColor(0x99ff0000);
            }
            else if(humValues[i] <=10 ) {
                heatMaps[i].setFillColor(darkRed);
                heatMaps[i].setStrokeWidth(5);
                heatMaps[i].setStrokeColor(0x99cc0000);
            }
        }
    }

    private void getTemperature() {
        for(int i=0; i<4; i++){
            if(tempValues[i] <= -3){
                heatMaps[i].setFillColor(darkBlue);
                heatMaps[i].setStrokeWidth(5);
                heatMaps[i].setStrokeColor(0x99003366);
            }
            else if(tempValues[i] > -3 && tempValues[i] < 1){
                heatMaps[i].setFillColor(lightBlue);
                heatMaps[i].setStrokeWidth(5);
                heatMaps[i].setStrokeColor(0x991a8cff);
            }
            else if(tempValues[i] > 0 && tempValues[i] < 3) {
                heatMaps[i].setFillColor(lightGreen);
                heatMaps[i].setStrokeWidth(5);
                heatMaps[i].setStrokeColor(0x9999ff33);
            }
            else if(tempValues[i] >= 3 && tempValues[i] < 7) {
                heatMaps[i].setFillColor(darkGreen);
                heatMaps[i].setStrokeWidth(5);
                heatMaps[i].setStrokeColor(0x9900b300);
            }
            else if(tempValues[i] >=7 && tempValues[i] < 11) {
                heatMaps[i].setFillColor(darkYellow);
                heatMaps[i].setStrokeWidth(5);
                heatMaps[i].setStrokeColor(0x99ffcc00);
            }
            else if(tempValues[i] >=11 && tempValues[i] < 15) {
                heatMaps[i].setFillColor(lightOrange);
                heatMaps[i].setStrokeWidth(5);
                heatMaps[i].setStrokeColor(0x99ff9933);
            }
            else if(tempValues[i] >= 15 && tempValues[i] < 20) {
                heatMaps[i].setFillColor(darkOrange);
                heatMaps[i].setStrokeWidth(5);
                heatMaps[i].setStrokeColor(0x99ff6600);
            }
            else if(tempValues[i] >=20 && tempValues[i] < 25) {
                heatMaps[i].setFillColor(lightRed);
                heatMaps[i].setStrokeWidth(5);
                heatMaps[i].setStrokeColor(0x99ff0000);
            }
            else if(tempValues[i] >=25 ) {
                heatMaps[i].setFillColor(darkRed);
                heatMaps[i].setStrokeWidth(5);
                heatMaps[i].setStrokeColor(0x99cc0000);
            }
        }

    }

    private void drawHumMaps() {
        drawHeatMaps();
    }

    private void removeHeatMaps() {
      for(int i=0; i<4; i++)
      {
          heatMaps[i].remove();
      }
    }
    private void drawHeatMaps() {
        heatMaps[0] = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(45.742692, 21.246249), new LatLng(45.742907, 21.247095), new LatLng(45.742095, 21.247567),new LatLng(45.741875, 21.246719))
                .strokeColor(0x00FFFFFF)
                .fillColor(0x00FFFFFF));
        heatMaps[1] = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(45.741678, 21.246721), new LatLng(45.741869, 21.247625), new LatLng(45.740912, 21.248116),new LatLng(45.740723, 21.247209))
                .strokeColor(0x00FFFFFF)
                .fillColor(0x00FFFFFF));
        heatMaps[2] = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(45.742696, 21.247421), new LatLng(45.742836, 21.247923), new LatLng(45.742117,  21.248317),new LatLng(45.741978, 21.247823))
                .strokeColor(0x00FFFFFF)
                .fillColor(0x00FFFFFF));
        heatMaps[3] = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(45.741860, 21.248976), new LatLng(45.742058, 21.249710), new LatLng(45.741167, 21.250198),new LatLng(45.740984, 21.249447))
                .strokeColor(0x00FFFFFF)
                .fillColor(0x00FFFFFF));

    }
}
