package com.atlantis.mobileapp.utilities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlantis.mobileapp.R;
import com.atlantis.mobileapp.objects.Device;

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
        return i;
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
        int type = d.getType();
        switch (type){
            case 9:
                imageViewIcon.setImageResource(R.drawable.ic_led);
                break;
            case 10:
                imageViewIcon.setImageResource(R.drawable.ic_beeper);
                break;
            case 7:
                imageViewIcon.setImageResource(R.drawable.ic_gpssensor);
                break;
            case 3:
                imageViewIcon.setImageResource(R.drawable.ic_lightsensor);
                break;
            case 8:
                imageViewIcon.setImageResource(R.drawable.ic_co2levelsensor);
                break;
            case 5:
                imageViewIcon.setImageResource(R.drawable.ic_humiditysensor);
                break;
            case 1:
                imageViewIcon.setImageResource(R.drawable.ic_presencesensor);
                break;
            case 6:
                imageViewIcon.setImageResource(R.drawable.ic_soundlevelsensor);
                break;
            case 2:
                imageViewIcon.setImageResource(R.drawable.ic_temperaturesensor);
                break;
            case 4:
                imageViewIcon.setImageResource(R.drawable.ic_pressuremetter);
                break;
            default:
                break;
        }
        return v;
    }
}
