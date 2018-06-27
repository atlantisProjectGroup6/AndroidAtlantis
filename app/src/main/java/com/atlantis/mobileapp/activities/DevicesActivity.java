package com.atlantis.mobileapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.atlantis.mobileapp.R;
import com.atlantis.mobileapp.objects.Device;
import com.atlantis.mobileapp.objects.SensorType;
import com.atlantis.mobileapp.utilities.CustomAdapter;

import java.util.ArrayList;
import java.util.List;

public class DevicesActivity extends AppCompatActivity {

    public static final String KEY_USER = "KEY_USER";
    public static final String KEY_DEVICE = "KEY_DEVICE";

    //UI
    ListView listView_devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        Intent i = getIntent();
        String userId = i.getStringExtra(KEY_USER);

        listView_devices = (ListView)findViewById(R.id.listView_devices);

        //TODO get user devices

        ArrayList<Device> array = new ArrayList<>();
        array.add(new Device(1,"AA:BB:CC:DD:EE:FF","Pressure captor", SensorType.AtmosphericPressureSensor));
        array.add(new Device(2,"11:22:33:44:55:66","CO2 Sensor", SensorType.CO2LevelSensor));
        array.add(new Device(3,"ZZ:BB:ZZ:BB:ZZ:BB","Humidity classroom", SensorType.HumiditySensor));
        array.add(new Device(4,"OO:BB:OO:OO:EE:OO","Sound level", SensorType.SoundLevelSensor));
        CustomAdapter customAdapter = new CustomAdapter(this.getApplicationContext(),array);
        listView_devices.setAdapter(customAdapter);

        listView_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DevicesActivity.this,DeviceDetailsActivity.class);
                intent.putExtra(KEY_DEVICE,"");//TODO RECUP ID DEVICE from devicestab (tab.get(i))
                startActivity(intent);
            }
        });
    }

    private List<Device> getUserDevices(String user){
        //TODO fonction getUserDevices
        return null;
    }
}
