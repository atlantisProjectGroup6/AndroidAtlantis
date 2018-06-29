package com.atlantis.mobileapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atlantis.mobileapp.R;
import com.atlantis.mobileapp.dataaccess.ClientWSCallBack;
import com.atlantis.mobileapp.dataaccess.ClientWSSingleton;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;


public class DeviceDetailsActivity extends AppCompatActivity implements ClientWSCallBack{

    public static final String KEY_DEVICE = "KEY_DEVICE";
    private ClientWSSingleton clientWS = null;

    //UI
    private LineChart graphMetrics;
    //GraphView graphCalculated;
    private BarChart barChart;
    private Button buttonWeek;
    private Button buttonDay;
    private Button buttonMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        Intent intent = getIntent();
        String deviceId = intent.getStringExtra(KEY_DEVICE);

        //WS
        clientWS = ClientWSSingleton.getInstance("api.openweathermap.org/data/2.5", DeviceDetailsActivity.this);
        clientWS.callback = this;
        //, -0.577657
        clientWS.getWeather("Bordeaux");
        graphMetrics = (LineChart) findViewById(R.id.graphView_graph);
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
        barChart = (BarChart)findViewById(R.id.graphView_graph2);
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


        buttonDay = (Button)findViewById(R.id.button_day);
        buttonWeek = (Button)findViewById(R.id.button_week);
        buttonMonth = (Button)findViewById(R.id.button_month);

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
    public void endGetWeather(String name, String main, String description) {
        TextView textView = (TextView) findViewById(R.id.textView_temp);
        textView.setText(String.format("%s : %s (%s).", name, main, description));
    }

    @Override
    public void endGetError(String error) {
        Toast.makeText(DeviceDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
    }
}
