package com.atlantis.mobileapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.atlantis.mobileapp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class DeviceDetailsActivity extends AppCompatActivity {

    public static final String KEY_DEVICE = "KEY_DEVICE";
    GraphView graph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        Intent intent = getIntent();
        String deviceId = intent.getStringExtra(KEY_DEVICE);

        graph = (GraphView)findViewById(R.id.graphView_graph);
        //TODO get device metrics to draw graph
        DataPoint[] points = new DataPoint[100];
        for (int i = 0; i < points.length; i++) {
            points[i] = new DataPoint(i, Math.sin(i*0.5) * 20*(Math.random()*10+1));
        }
        configureGraph(points);

    }

    private void configureGraph(DataPoint[] arr){

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(arr);
        series.setTitle("Last metrics");
        series.setDrawBackground(true);
        series.setDrawDataPoints(true);
        series.setAnimated(true);
        /*
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-200);
        graph.getViewport().setMaxY(200);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(50);*/

        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        graph.addSeries(series);
    }
}
