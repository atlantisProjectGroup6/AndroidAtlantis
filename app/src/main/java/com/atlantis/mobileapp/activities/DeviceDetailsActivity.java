package com.atlantis.mobileapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atlantis.mobileapp.R;
import com.atlantis.mobileapp.dataaccess.ClientWSCallBack;
import com.atlantis.mobileapp.dataaccess.ClientWSSingleton;
import com.atlantis.mobileapp.objects.Device;
import com.atlantis.mobileapp.objects.Metrics;
import com.atlantis.mobileapp.utilities.Consts;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.jjoe64.graphview.series.DataPoint;


import java.util.ArrayList;


public class DeviceDetailsActivity extends AppCompatActivity implements ClientWSCallBack, OnMapReadyCallback {

    public static final String KEY_DEVICEMAC = "KEY_DEVICEMAC";
    public static final String KEY_DEVICENAME = "KEY_DEVICENAME";
    public static final String KEY_DEVICETYPE = "KEY_DEVICETYPE";
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    //UI
    private LineChart graphMetrics;
    //GraphView graphCalculated;
    private BarChart barChart;
    private Button buttonWeek;
    private Button buttonDay;
    private Button buttonMonth;
    private MapView mapView;
    GoogleMap gmap;

    private ClientWSSingleton clientWS = null;
    private ArrayList<Metrics> metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        Intent intent = getIntent();
        String deviceMac = intent.getStringExtra(KEY_DEVICEMAC);
        String deviceName = intent.getStringExtra(KEY_DEVICENAME);
        int deviceType = intent.getIntExtra(KEY_DEVICETYPE,0);
        setTitle(deviceName);

        //UI
        graphMetrics = (LineChart) findViewById(R.id.graphView_graph);
        barChart = (BarChart)findViewById(R.id.graphView_graph2);
        buttonDay = (Button)findViewById(R.id.button_day);
        buttonWeek = (Button)findViewById(R.id.button_week);
        buttonMonth = (Button)findViewById(R.id.button_month);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = (MapView)findViewById(R.id.mapView_deviceGps);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        //WS
        clientWS = ClientWSSingleton.getInstance(Consts.serverUrl, DeviceDetailsActivity.this);
        clientWS.callback = this;

        if(deviceType > 0 && deviceType < 9)
        {
            if(deviceType != 7){

                graphMetrics.setVisibility(View.VISIBLE);
                barChart.setVisibility(View.VISIBLE);
                buttonDay.setVisibility(View.VISIBLE);
                buttonWeek.setVisibility(View.VISIBLE);
                buttonMonth.setVisibility(View.VISIBLE);
                mapView.setVisibility(View.GONE);
                setupGraphs();
            }else{
                graphMetrics.setVisibility(View.GONE);
                barChart.setVisibility(View.GONE);
                buttonDay.setVisibility(View.GONE);
                buttonWeek.setVisibility(View.GONE);
                buttonMonth.setVisibility(View.GONE);
                mapView.setVisibility(View.VISIBLE);
                setupMap();
            }
        }

        clientWS.getLatestMetrics(deviceMac,0);

        ///region button listener
        buttonDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO change graphMetrics scale/dataset for only day's metrics
                DataPoint[] calc = new DataPoint[3];
                calc[0] = new DataPoint(1,18);
                calc[1] = new DataPoint(2,22);
                calc[2] = new DataPoint(3,30);
                configureGraphCalculated(calc);
            }
        });
        buttonWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO change graphMetrics scale/dataset for only week's metrics
                DataPoint[] calc = new DataPoint[3];
                calc[0] = new DataPoint(1,15);
                calc[1] = new DataPoint(2,20);
                calc[2] = new DataPoint(3,36);
                configureGraphCalculated(calc);
            }
        });
        buttonMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO change graphMetrics scale/dataset for only month's metrics
                DataPoint[] calc = new DataPoint[3];
                calc[0] = new DataPoint(1,05);
                calc[1] = new DataPoint(2,13);
                calc[2] = new DataPoint(3,36);
                configureGraphCalculated(calc);
            }
        });
    }

    private void setupMap() {
        //TODO CODE MAP LOGIC
    }

    private void setupGraphs() {
        //TODO get device metrics to draw graphMetrics

        DataPoint[] points = new DataPoint[100];
        for (int i = 0; i < points.length; i++) {
            points[i] = new DataPoint(i, Math.random());
        }
        configureGraphMetrics(points);

        //graphCalculated = (GraphView)findViewById(R.id.graphView_graph2);
        //TODO get device calculation to draw graphCalculated
        //        DataPoint[] calc = new DataPoint[3];
        //        calc[0] = new DataPoint(1,18);
        //        calc[1] = new DataPoint(2,25);
        //        calc[2] = new DataPoint(3,36);
        //        configureGraphCalculated(calc);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0,18f));
        entries.add(new BarEntry(1,25f));
        entries.add(new BarEntry(2,36f));
        BarDataSet set = new BarDataSet(entries,"Temperature");
        ArrayList<String> dates = new ArrayList<>();
        dates.add("Minimum");
        dates.add("Average");
        dates.add("Maximum");
        BarDataSet set2 = new BarDataSet(entries,"Dates");

        BarData data = new BarData(set);
        BarData d = new BarData(set,set2);
        barChart.setData(d);
        barChart.setTouchEnabled(false);
    }

    private void configureGraphMetrics(DataPoint[] arr){
    }

    private void configureGraphCalculated(DataPoint[] arr){
        /*
        graphCalculated.removeAllSeries();
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(arr);
        series.setSpacing(50);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);
        graphCalculated.addSeries(series);
        graphCalculated.getViewport().setYAxisBoundsManual(true);
        graphCalculated.getViewport().setMinY(0);
        graphCalculated.getViewport();
        graphCalculated.getViewport().setXAxisBoundsManual(true);
        graphCalculated.getViewport().setMinX(0);
        graphCalculated.getViewport().setMaxX(4);
        */
    }

    @Override
    public void endGetError(String error) {
        Toast.makeText(DeviceDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void endSendUserId(String s) {

    }

    @Override
    public void endSendUserName(String response) {

    }

    @Override
    public void endGetUserDevices(ArrayList<Device> devices) {

    }

    @Override
    public void endGetLatestMetrics(ArrayList<Metrics> mets) {
        Log.d("getLatestMetrics", "Done");
        //TODO USE METRICS FOR GRAPHS
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(12);
        //TODO ADD MARKER TO LOCATION
        //LatLng ny = new LatLng(40.7143528, -74.0059731);
        //gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }
}
