package com.atlantis.mobileapp.utilities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlantis.mobileapp.R;
import com.atlantis.mobileapp.objects.Device;
import com.atlantis.mobileapp.objects.SensorType;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private List<Device> devices;

    public CustomAdapter(Context ctxt, List<Device> objects) {
        this.context = ctxt;
        this.devices = objects;
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int i) {
        return devices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return devices.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.custom_list_view_devices,null);
        TextView textViewName = (TextView)v.findViewById(R.id.textView_deviceName);
        TextView textViewMac = (TextView)v.findViewById(R.id.textView_deviceMac);
        ImageView imageViewIcon = (ImageView)v.findViewById(R.id.imageView_deviceIcon);
        Device d = devices.get(i);
        textViewName.setText(d.getName());
        textViewMac.setText(d.getMac());
        SensorType type = d.getType();
        switch (type){
            case LED:
                imageViewIcon.setImageResource(R.drawable.ic_led);
                break;
            case Beeper:
                imageViewIcon.setImageResource(R.drawable.ic_beeper);
                break;
            case GPSSensor:
                imageViewIcon.setImageResource(R.drawable.ic_gpssensor);
                break;
            case LightSensor:
                imageViewIcon.setImageResource(R.drawable.ic_lightsensor);
                break;
            case CO2LevelSensor:
                imageViewIcon.setImageResource(R.drawable.ic_co2levelsensor);
                break;
            case HumiditySensor:
                imageViewIcon.setImageResource(R.drawable.ic_humiditysensor);
                break;
            case PresenceSensor:
                imageViewIcon.setImageResource(R.drawable.ic_presencesensor);
                break;
            case SoundLevelSensor:
                imageViewIcon.setImageResource(R.drawable.ic_soundlevelsensor);
                break;
            case TemperatureSensor:
                imageViewIcon.setImageResource(R.drawable.ic_temperaturesensor);
                break;
            case AtmosphericPressureSensor:
                imageViewIcon.setImageResource(R.drawable.ic_pressuremetter);
                break;
            default:
                imageViewIcon.setImageResource(R.drawable.ic_launcher_foreground);
                break;
        }
        v.setTag(d.getId());
        return v;
    }
}
