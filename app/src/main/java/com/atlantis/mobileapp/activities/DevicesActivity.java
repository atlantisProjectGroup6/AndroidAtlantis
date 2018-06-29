package com.atlantis.mobileapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;

import com.atlantis.mobileapp.R;
import com.atlantis.mobileapp.objects.Device;
import com.atlantis.mobileapp.objects.SensorType;
import com.atlantis.mobileapp.utilities.CustomAdapter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class DevicesActivity extends AppCompatActivity {
    final static String CLIENT_ID = "358ca400-fdf6-4357-8cca-27caa6699197";
    final static String CLIENT_SECRET = "*d,|`89Jnx/Ea5O8y$T724W4";
    final static String REDIRECT_URI = "https://login.microsoftonline.com/tfp/oauth2/nativeclient";
    final static String KEY_CODE = "KEY_CODE";
    final static String URL_AUTH = "https://partners-login.eliotbylegrand.com/authorize?client_id=" + CLIENT_ID + "&response_type=code&redirect_uri=" + REDIRECT_URI;

    public static final String KEY_USER = "KEY_USER";
    public static final String KEY_DEVICE = "KEY_DEVICE";
    //UI
    ListView listView_devices;
    String token;
    WebView webView;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        //Intent i = getIntent();
        //String token = i.getStringExtra(KEY_CODE);

        webView = (WebView) findViewById(R.id.webView_authBis);
        listView_devices = (ListView)findViewById(R.id.listView_devices);


        webView.setVisibility(View.VISIBLE);
        listView_devices.setVisibility(View.GONE);

        webView.getSettings().setJavaScriptEnabled(true);
        startWebView();
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
        //String userId = i.getStringExtra(KEY_USER);
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

    private void startWebView() {
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearFormData();
        webView.setWebViewClient(new WebViewClient(){
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String fragment = "?code=";
                int start = url.indexOf(fragment);
                if (start > -1) {
                    webView.stopLoading();
                    final String code = url.substring(start + fragment.length(), url.length());
                    Log.d("CODE",code);

                    dialog = ProgressDialog.show(DevicesActivity.this, "Loading","Connecting to Eliot account, please wait...", true);
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getToken(code);
                            } catch (MalformedURLException e) {
                                Log.d("Exception", e.getMessage());
                            }
                        }
                    });
                    t.start();
                }
            }
        });
        webView.loadUrl(URL_AUTH);
    }

    private void getToken(String code) throws MalformedURLException {
        URL url = new URL("https://partners-login.eliotbylegrand.com/token");
        HttpURLConnection conn;
        try {
            String data = "client_id=" + URLEncoder.encode(CLIENT_ID, "UTF-8");
            data += "&grant_type=" + URLEncoder.encode("authorization_code", "UTF-8");
            data += "&code=" + URLEncoder.encode(code, "UTF-8");
            data += "&client_secret=" + URLEncoder.encode(CLIENT_SECRET, "UTF-8");
            conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            wr.close();
            InputStream is = null;
            try {
                is = conn.getInputStream();
            }
            catch (IOException e) {
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line).append("\n");
            }
            reader.close();
            token = sb.toString();
            Log.d("D","ATA : " + token);
        }  catch (IOException e) {
            Log.d("Exception", e.getMessage());
        }
        dialog.cancel();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.setVisibility(View.GONE);
                listView_devices.setVisibility(View.VISIBLE);
            }
        });
//        Intent intent = new Intent(DevicesActivity.this,DevicesActivity.class);
//        intent.putExtra(KEY_CODE,token);
//        startActivity(intent);
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
