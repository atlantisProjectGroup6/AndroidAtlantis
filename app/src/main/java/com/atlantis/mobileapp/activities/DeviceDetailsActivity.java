package com.atlantis.mobileapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DeviceDetailsActivity extends AppCompatActivity implements ClientWSCallBack, OnMapReadyCallback {

    public static final String KEY_DEVICEMAC = "KEY_DEVICEMAC";
    public static final String KEY_DEVICENAME = "KEY_DEVICENAME";
    public static final String KEY_DEVICETYPE = "KEY_DEVICETYPE";

    //UI
    private LineChart graphMetrics;
    private BarChart barChart;
    private MapView mapView;
    private ImageView imageView;
    private Switch aSwitch;
    private TextView textView;

    private ArrayList<Metrics> metrics;
    private ClientWSSingleton clientWS;
    private String deviceName;
    private String deviceMac;
    private int deviceType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        Intent intent = getIntent();
        deviceMac = intent.getStringExtra(KEY_DEVICEMAC);
        deviceName = intent.getStringExtra(KEY_DEVICENAME);
        deviceType = intent.getIntExtra(KEY_DEVICETYPE, 0);
        setTitle(deviceName);

        //UI
        graphMetrics = (LineChart) findViewById(R.id.graphView_graph);
        barChart = (BarChart) findViewById(R.id.graphView_graph2);
        mapView = (MapView) findViewById(R.id.mapView_deviceGps);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        imageView = (ImageView)findViewById(R.id.imageView_commandIcon);
        aSwitch = (Switch)findViewById(R.id.switch_lightLed);
        textView = (TextView)findViewById(R.id.textView_presence);

        //WS
        clientWS = ClientWSSingleton.getInstance(Consts.serverUrlJee, Consts.serverUrlNet, DeviceDetailsActivity.this);
        clientWS.callback = this;
        metrics = new ArrayList<Metrics>();

        switch (deviceType){
            case 3:
            case 8:
            case 5:
            case 6:
            case 2:
            case 4:
                graphMetrics.setVisibility(View.VISIBLE);
                barChart.setVisibility(View.VISIBLE);
                mapView.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                aSwitch.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                break;
            case 9://LED
            case 10://BEEPER
                graphMetrics.setVisibility(View.GONE);
                barChart.setVisibility(View.GONE);
                mapView.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                aSwitch.setVisibility(View.VISIBLE);
                aSwitch.setEnabled(false);
                if(deviceType == 9)
                    imageView.setImageResource(R.drawable.ic_led);
                if(deviceType == 10)
                    imageView.setImageResource(R.drawable.ic_beeper);
                clientWS.getCommand(deviceMac);
                break;
            case 1://Presence
                graphMetrics.setVisibility(View.GONE);
                barChart.setVisibility(View.GONE);
                mapView.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                aSwitch.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.ic_presencesensor);
                textView.setVisibility(View.VISIBLE);
                break;
            case 7://GPS
                graphMetrics.setVisibility(View.GONE);
                barChart.setVisibility(View.GONE);
                mapView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                aSwitch.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        if(deviceType < 9)
            clientWS.getLatestMetrics(deviceMac,0);

    }

    private void configureGraphMetrics(ArrayList<Metrics> mets){
        Description description = new Description();
        String desc = "Raw data";
        switch (deviceType){
            case 3:
                desc += " (in lux)";
                break;
            case 8:
                desc += " (in ppm)";
                break;
            case 5:
                desc += " (in %)";
                break;
            case 6:
                desc += " (in dB)";
                break;
            case 2:
                desc += " (in °C)";
                break;
            case 4:
                desc += " (in hPa)";
                break;
            default:
                break;
        }
        graphMetrics.setDescription(description);
        graphMetrics.setDragXEnabled(true);
        graphMetrics.setDragYEnabled(false);
        graphMetrics.setScaleXEnabled(true);
        graphMetrics.setScaleYEnabled(false);

        mets.sort(new Comparator<Metrics>() {
            @Override
            public int compare(Metrics metrics, Metrics t1) {
                return metrics.getDate() == t1.getDate() ? 1:0;
            }
        });

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
        final List<Entry> entries = new ArrayList<>();
        final List<String> labels = new ArrayList<>();
        for(int i = 0; i < mets.size(); i++){
            entries.add(new Entry(i,Float.parseFloat(mets.get(i).getValue())));
            labels.add(formatter.format(new Date(mets.get(i).getDate())));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Latest metrics recovered");
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(Color.CYAN);
        lineDataSet.setColors(Color.BLUE);
        lineDataSet.setValueTextSize(12f);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setCircleColor(Color.BLUE);
        LineData lineData = new LineData(lineDataSet);


        graphMetrics.setData(lineData);
        XAxis xAxis = graphMetrics.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                for (Entry e:entries) {
                    if(e.getX() == value)
                        return labels.get((int)value);
                }
                return "";
            }
        });
        xAxis.setGranularity(1f);
        graphMetrics.getAxisRight().setEnabled(false);

        graphMetrics.animateY(2000);
        graphMetrics.invalidate();
    }

    private void configureGraphCalculated(ArrayList<Metrics> mets){

        Description description = new Description();
        String desc = "Calculated data";
        switch (deviceType){
            case 3:
                desc += " (in lux)";
                break;
            case 8:
                desc += " (in ppm)";
                break;
            case 5:
                desc += " (in %)";
                break;
            case 6:
                desc += " (in dB)";
                break;
            case 2:
                desc += " (in °C)";
                break;
            case 4:
                desc += " (in hPa)";
                break;
            default:
                break;
        }
        barChart.setDescription(description);
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        //DATA
        final List<BarEntry> entries7Days = new ArrayList<>();
        entries7Days.add(new BarEntry(1,4));
        entries7Days.add(new BarEntry(4, 8));
        entries7Days.add(new BarEntry(7, 10));
        final List<BarEntry> entries14Days = new ArrayList<>();
        entries14Days.add(new BarEntry(2,3));
        entries14Days.add(new BarEntry(5, 9));
        entries14Days.add(new BarEntry(8, 12));
        final List<BarEntry> entries31Days = new ArrayList<>();
        entries31Days.add(new BarEntry(3,0));
        entries31Days.add(new BarEntry(6, 7));
        entries31Days.add(new BarEntry(9, 12));

        //X AXIS
        final List<String> labels = new ArrayList<>();
        //TODO FILL LABELS WITH DATE
        labels.add("Minimums");
        labels.add("Averages");
        labels.add("Maximums");
        BarDataSet barDataSet = new BarDataSet(entries7Days, "Last 7 days");
        barDataSet.setColors(Color.rgb(0,240,0));
        BarDataSet barDataSet2 = new BarDataSet(entries14Days, "Last two weeks");
        barDataSet2.setColors(Color.rgb(240,240,0));
        BarDataSet barDataSet3 = new BarDataSet(entries31Days, "Last month");
        barDataSet3.setColors(Color.rgb(240,0,0));


        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);
        BarData barData = new BarData(dataSets);

        barChart.setData(barData);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                for (Entry e:entries7Days) {
                    if(value > 1.4 && value < 1.6)
                        return labels.get(0);
                    if(value > 4.7 && value < 4.8)
                        return labels.get(1);
                    if(value > 7.9 && value < 8.1)
                        return labels.get(2);
                }
                return "";
            }
        });
        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(0);
        xAxis.setLabelCount(21,true);
        barChart.getAxisLeft().setSpaceBottom(0);
        barChart.getAxisRight().setEnabled(false);



        barChart.getBarData().setValueTextSize(12f);
        barChart.getBarData().setValueTextColor(Color.BLACK);
        barChart.groupBars(0,0.45f,0.05f);
        barChart.setTouchEnabled(false);
        barChart.animateY(2000);
        barChart.invalidate();
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
        if(deviceType != 1)
            configureGraphMetrics(mets);
        else
            configurePresenceData(mets);
    }

    private void configurePresenceData(ArrayList<Metrics> mets) {
        if(mets.get(mets.size()-1).getValue().equals("true")) {
            imageView.setColorFilter(Color.GREEN);
            textView.setText(R.string.presence_true);
        }
        else {
            imageView.setColorFilter(Color.RED);
            textView.setText(R.string.presence_false);
        }
    }

    @Override
    public void endGetCalculatedMetrics(ArrayList<Metrics> metrics) {
        Log.d("getCalculatedMetrics", "Done");
        //TODO CALL CONFIGURECALCGRAPH
    }

    @Override
    public void endSendCommand(String response) {
        Log.d("sendCommand", "Done");
        if(response.equals("true")) {
            if(aSwitch.isChecked())
                imageView.setColorFilter(Color.GREEN);
            else
                imageView.setColorFilter(Color.RED);
        }
        aSwitch.setEnabled(true);
    }

    @Override
    public void endGetCommand(String resp) {
        Log.d("getCommand", "Done");
        if(resp.equals("true")) {
            aSwitch.setChecked(true);
            imageView.setColorFilter(Color.GREEN);
        } else {
            aSwitch.setChecked(false);
            imageView.setColorFilter(Color.RED);
        }
        aSwitch.setEnabled(true);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                clientWS.sendCommand(deviceMac, (b ? "true":"false"));
                aSwitch.setEnabled(false);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Metrics current;
        if(metrics == null || metrics.size() == 0){
            current = new Metrics(1,"4451540612N,0034387912W",1532659887);
        }else {
            current = metrics.get(metrics.size() - 1);
        }
        double[] lats = convert(current.getValue());
        googleMap.addMarker(new MarkerOptions().position(new LatLng(lats[0], lats[1])).title(deviceName));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lats[0], lats[1]),15));
    }
    public double[] convert(String latlon){
        String[] parts = latlon.split(",");

        double[] latlong = new double[2];
        latlong[0] = convertPart(parts[0]);
        latlong[1] = convertPart(parts[1]);
        return latlong;
    }
    private double convertPart(String angle){
        while (angle.length() < 10)
            angle = new StringBuffer(angle).insert(0, "0").toString();
        int deg = Integer.parseInt( angle.substring(0,2) );
        int min = Integer.parseInt( angle.substring(2,4) );
        int sec = Integer.parseInt( angle.substring(4,6) );
        int sub = Integer.parseInt( angle.substring(6,10) );
        String hem = angle.substring(10);

        double minD = min / 60.000f;
        double secD = sec / 3600.000f;
        double subD = sub / 360000.000f / 100;
        double value = deg + minD + secD + subD;
        double sign;
        switch(hem){
            case "S":
            case "W":
                sign = -1.0;
                break;
            default:
                sign = 1.0;
        }
        return sign * value;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
