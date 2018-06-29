package com.atlantis.mobileapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.atlantis.mobileapp.R;
import com.atlantis.mobileapp.objects.Device;
import com.atlantis.mobileapp.objects.SensorType;
import com.atlantis.mobileapp.utilities.CustomAdapter;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class DevicesActivity extends AppCompatActivity {

    public static final String KEY_USER = "KEY_USER";
    public static final String KEY_DEVICE = "KEY_DEVICE";
    public static final String KEY_CODE = "KEY_CODE";

    //UI
    ListView listView_devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        Intent i = getIntent();
        String token = i.getStringExtra(KEY_CODE);

        Log.d("access_token",":" + token);
        try {
            JSONObject jsonObject = new JSONObject(token);
            String idToken = jsonObject.getString("id_token");
            String refreshToken = jsonObject.getString("refresh_token");
            String sub = decoded(idToken);
            if(sub != null) {
                int indexSub = sub.indexOf("\"sub\":") + 7;
                sub = sub.substring(indexSub, indexSub + 36);
            }
        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }
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


    public static String decoded(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
            return getJson(split[1]);
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }
    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    private List<Device> getUserDevices(String user){
        //TODO fonction getUserDevices
        return null;
    }
}
